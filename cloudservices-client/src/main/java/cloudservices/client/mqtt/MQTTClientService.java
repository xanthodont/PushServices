package cloudservices.client.mqtt;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.demux.DemuxingProtocolDecoder;
import org.apache.mina.filter.codec.demux.DemuxingProtocolEncoder;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.dna.mqtt.commons.Constants;
import org.dna.mqtt.commons.MessageIDGenerator;
import org.dna.mqtt.moquette.ConnectionException;
import org.dna.mqtt.moquette.MQTTException;
import org.dna.mqtt.moquette.PublishException;
import org.dna.mqtt.moquette.SubscribeException;
import org.dna.mqtt.moquette.proto.ConnAckDecoder;
import org.dna.mqtt.moquette.proto.ConnectEncoder;
import org.dna.mqtt.moquette.proto.DisconnectEncoder;
import org.dna.mqtt.moquette.proto.PingReqEncoder;
import org.dna.mqtt.moquette.proto.PingRespDecoder;
import org.dna.mqtt.moquette.proto.PubAckDecoder;
import org.dna.mqtt.moquette.proto.PublishDecoder;
import org.dna.mqtt.moquette.proto.PublishEncoder;
import org.dna.mqtt.moquette.proto.SubAckDecoder;
import org.dna.mqtt.moquette.proto.SubscribeEncoder;
import org.dna.mqtt.moquette.proto.UnsubAckDecoder;
import org.dna.mqtt.moquette.proto.UnsubscribeEncoder;
import org.dna.mqtt.moquette.proto.messages.AbstractMessage;
import org.dna.mqtt.moquette.proto.messages.ConnAckMessage;
import org.dna.mqtt.moquette.proto.messages.ConnectMessage;
import org.dna.mqtt.moquette.proto.messages.DisconnectMessage;
import org.dna.mqtt.moquette.proto.messages.MessageIDMessage;
import org.dna.mqtt.moquette.proto.messages.PingReqMessage;
import org.dna.mqtt.moquette.proto.messages.PublishMessage;
import org.dna.mqtt.moquette.proto.messages.SubscribeMessage;
import org.dna.mqtt.moquette.proto.messages.UnsubscribeMessage;

import cloudservices.client.ClientConfiguration;

public class MQTTClientService {
	private ClientConfiguration config;
	private IoConnector nioConnector;
	private ScheduledExecutorService scheduler;
	private IoSession ioSession;
	private CountDownLatch connectBarrier;
	private byte returnCode;
	private String clientID;
	private static final long CONNECT_TIMEOUT = 3 * 1000L; // 3 seconds
    private static final long SUBACK_TIMEOUT = 4 * 1000L;
    private static final int KEEPALIVE_SECS = 3;
    final static int RETRIES_QOS_GT0 = 3;
    
    private ScheduledFuture pingerHandler;
    private CountDownLatch subscribeBarrier;
    private int receivedSubAckMessageID;
    private Map<String, IPublishCallback> subscribersList = new HashMap<String, IPublishCallback>();
    private MessageIDGenerator messageIDGenerator = new MessageIDGenerator();
	
	public MQTTClientService(ClientConfiguration config) {
		this.config = config;
		init();
	}
	
	public ClientConfiguration getConfiguration() {
		return config;
	}
	
	protected void init() {
		/**
		 * 解码类
		 */
        DemuxingProtocolDecoder decoder = new DemuxingProtocolDecoder();
        decoder.addMessageDecoder(new ConnAckDecoder());
        decoder.addMessageDecoder(new SubAckDecoder());
        decoder.addMessageDecoder(new UnsubAckDecoder());
        decoder.addMessageDecoder(new PublishDecoder());
        decoder.addMessageDecoder(new PubAckDecoder());
        decoder.addMessageDecoder(new PingRespDecoder());

        /**
         * 编码类
         */
        DemuxingProtocolEncoder encoder = new DemuxingProtocolEncoder();
        encoder.addMessageEncoder(ConnectMessage.class, new ConnectEncoder());
        encoder.addMessageEncoder(PublishMessage.class, new PublishEncoder());
        encoder.addMessageEncoder(SubscribeMessage.class, new SubscribeEncoder());
        encoder.addMessageEncoder(UnsubscribeMessage.class, new UnsubscribeEncoder());
        encoder.addMessageEncoder(DisconnectMessage.class, new DisconnectEncoder());
        encoder.addMessageEncoder(PingReqMessage.class, new PingReqEncoder());

        nioConnector = new NioSocketConnector();

        nioConnector.getFilterChain().addLast("codec", new ProtocolCodecFilter(encoder, decoder));
        nioConnector.setHandler(new MQTTClientHandler(this));
        
        nioConnector.getSessionConfig().setReadBufferSize(2048);
        nioConnector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, Constants.DEFAULT_CONNECT_TIMEOUT);

        scheduler = Executors.newScheduledThreadPool(config.getNumberOfSchedulerThread());
    }
	
	public void connect() throws MQTTException {
        connect(true);
    }
    
    
    public void connect(boolean cleanSession) throws MQTTException {
        try {
            ConnectFuture future = nioConnector.connect(new InetSocketAddress(config.getHost(), config.getPort()));
            //LOG.debug("Client waiting to connect to server");
            future.awaitUninterruptibly();
            ioSession = future.getSession();
        } catch (RuntimeIoException e) {
            //LOG.debug("Failed to connect, retry " + retries + " of (" + m_connectRetries + ")", e);
        }

        connectBarrier = new CountDownLatch(1);

        //send a message over the session
        ConnectMessage connMsg = new ConnectMessage();
        connMsg.setKeepAlive(KEEPALIVE_SECS);
        if (clientID == null) {
            clientID = generateClientID();
        }
        connMsg.setClientID(clientID);
        connMsg.setCleanSession(cleanSession);
        
        ioSession.write(connMsg);

        //suspend until the server respond with CONN_ACK
        boolean unlocked = false;
        try {
            unlocked = connectBarrier.await(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS); //TODO parametrize
        } catch (InterruptedException ex) {
            throw new ConnectionException(ex);
        }

        //if not arrive into certain limit, raise an error
        if (!unlocked) {
            throw new ConnectionException("Connection timeout elapsed unless server responded with a CONN_ACK");
        }

        //also raise an error when CONN_ACK is received with some error code inside
        if (returnCode != ConnAckMessage.CONNECTION_ACCEPTED) {
            String errMsg;
            switch (returnCode) {
                case ConnAckMessage.UNNACEPTABLE_PROTOCOL_VERSION:
                    errMsg = "Unacceptable protocol version";
                    break;
                case ConnAckMessage.IDENTIFIER_REJECTED:
                    errMsg = "Identifier rejected";
                    break;
                case ConnAckMessage.SERVER_UNAVAILABLE:
                    errMsg = "Server unavailable";
                    break;
                case ConnAckMessage.BAD_USERNAME_OR_PASSWORD:
                    errMsg = "Bad username or password";
                    break;
                case ConnAckMessage.NOT_AUTHORIZED:
                    errMsg = "Not authorized";
                    break;
                default:
                    errMsg = "Not idetified erro code " + returnCode;
            }
            throw new ConnectionException(errMsg);
        }

        updatePinger();
    }
    
    public void publish(String topic, byte[] payload) throws PublishException {
        publish(topic, payload, false);
    }

    /**
     * Publish by default with QoS 0
     * */
    public void publish(String topic, byte[] payload, boolean retain) throws PublishException {
        publish(topic, payload, AbstractMessage.QOSType.MOST_ONE, retain);
    }

    public void publish(String topic, byte[] payload, AbstractMessage.QOSType qos, boolean retain) throws PublishException {
        PublishMessage msg = new PublishMessage();
        msg.setRetainFlag(retain);
        msg.setTopicName(topic);
        msg.setPayload(payload);

        //Untill the server could handle all the Qos 2 level
        if (qos != AbstractMessage.QOSType.MOST_ONE) {
            msg.setQos(AbstractMessage.QOSType.LEAST_ONE);
            int messageID = messageIDGenerator.next();
            msg.setMessageID(messageID);

            try {
                manageSendQoS1(msg);
            } catch (Throwable ex) {
                throw new MQTTException(ex);
            }
        } else {
            //QoS 0 case
            msg.setQos(AbstractMessage.QOSType.MOST_ONE);
            WriteFuture wf = ioSession.write(msg);
            try {
                wf.await();
            } catch (InterruptedException ex) {
                //LOG.debug(null, ex);
                throw new PublishException(ex);
            }

            Throwable ex = wf.getException();
            if (ex != null) {
                throw new PublishException(ex);
            }
        }

        updatePinger();
    }

    public void subscribe(String topic, IPublishCallback publishCallback) {
        //LOG.info("subscribe invoked");
        SubscribeMessage msg = new SubscribeMessage();
        msg.addSubscription(new SubscribeMessage.Couple((byte) AbstractMessage.QOSType.MOST_ONE.ordinal(), topic));
        msg.setQos(AbstractMessage.QOSType.LEAST_ONE);
        int messageID = messageIDGenerator.next();
        msg.setMessageID(messageID);
//        m_inflightIDs.add(messageID);
        register(topic, publishCallback);
        
        try {
            manageSendQoS1(msg);
        } catch(Throwable ex) {
            //in case errors arise, remove the registration because the subscription
            // hasn't get well
            unregister(topic);
            throw new MQTTException(ex);
        }

        updatePinger();
    }
    
    
    public void unsubscribe(String... topics) {
        //LOG.info("unsubscribe invoked");
        UnsubscribeMessage msg = new UnsubscribeMessage();
        for (String topic : topics) {
            msg.addTopic(topic);
        }
        msg.setQos(AbstractMessage.QOSType.LEAST_ONE);
        int messageID = messageIDGenerator.next();
        msg.setMessageID(messageID);
//        m_inflightIDs.add(messageID);
//        register(topic, publishCallback);
        try {
            manageSendQoS1(msg);
        } catch(Throwable ex) {
            //in case errors arise, remove the registration because the subscription
            // hasn't get well
//            unregister(topic);
            throw new MQTTException(ex);
        }
        
        for (String topic : topics) {
            unregister(topic);
        }

//        register(topic, publishCallback);
        updatePinger();
    }
    
    private void manageSendQoS1(MessageIDMessage msg) throws Throwable{
        int messageID = msg.getMessageID();
        boolean unlocked = false;
        for (int retries = 0; retries < RETRIES_QOS_GT0 || !unlocked; retries++) {
            //LOG.debug("manageSendQoS1 retry " + retries);
            if (retries > 0) {
                msg.setDupFlag(true);
            }

            WriteFuture wf = ioSession.write(msg);
            wf.await();
            //LOG.info("message sent");

            Throwable ex = wf.getException();
            if (ex != null) {
                throw ex;
            }

            //wait for the SubAck
            subscribeBarrier = new CountDownLatch(1);

            //suspend until the server respond with CONN_ACK
            //LOG.info("subscribe waiting for suback");
            unlocked = subscribeBarrier.await(SUBACK_TIMEOUT, TimeUnit.MILLISECONDS); //TODO parametrize
        }

        //if not arrive into certain limit, raise an error
        if (!unlocked) {
            throw new SubscribeException(String.format("Server doesn't replyed with a SUB_ACK after %d replies", RETRIES_QOS_GT0));
        } else {
            //check if message ID match
            if (receivedSubAckMessageID != messageID) {
                throw new SubscribeException(String.format("Server replyed with "
                + "a broken MessageID in SUB_ACK, expected %d but received %d", 
                messageID, receivedSubAckMessageID));
            }
        }
    }
	
    /**
     * 获取客户端Id
     * 注：客户端ID是资源名+用户名
     * @return
     */
    private String generateClientID() {
        String id =  config.getResourceName() + config.getUsername();
        //LOG.debug("Generated ClientID " + id);
        return id;
    }
    
    /**
     * 更新心跳线程
     */
    private void updatePinger() {
        if (pingerHandler != null) {
            pingerHandler.cancel(false);
        }
        pingerHandler = scheduler.scheduleWithFixedDelay(pingerDeamon, KEEPALIVE_SECS, KEEPALIVE_SECS, TimeUnit.SECONDS);
    }
    // 发送心跳包
    final Runnable pingerDeamon = new Runnable() {
        public void run() {
            //LOG.debug("Pingreq sent");
            ioSession.write(new PingReqMessage());
        }
    };
	
    /**
     * 关闭
     */
    public void close() {
        // 停掉心跳线程
        pingerHandler.cancel(false);

        // 发送断开连接的消息
        ioSession.write(new DisconnectMessage());

        // wait until the summation is done
        ioSession.getCloseFuture().awaitUninterruptibly();
    }
    
    protected void connectionAckCallback(byte returnCode) {
        //LOG.info("connectionAckCallback invoked");
        this.returnCode = returnCode;
        connectBarrier.countDown();
    }

    protected void subscribeAckCallback(int messageID) {
        //LOG.info("subscribeAckCallback invoked");
        subscribeBarrier.countDown();
        receivedSubAckMessageID = messageID;
    }
    
    void unsubscribeAckCallback(int messageID) {
        //LOG.info("unsubscribeAckCallback invoked");
        //NB we share the barrier because in futur will be a single barrier for all
        subscribeBarrier.countDown();
        receivedSubAckMessageID = messageID;
    }

    void publishAckCallback(Integer messageID) {
        //LOG.info("publishAckCallback invoked");
        subscribeBarrier.countDown();
        receivedSubAckMessageID = messageID;
    }
    
    protected void publishCallback(String topic, byte[] payload) {
        IPublishCallback callback = subscribersList.get(topic);
        if (callback == null) {
            String msg = String.format("Can't find any publish callback fr topic %s", topic);
            //LOG.error(msg);
            throw new MQTTException(msg);
        }
        callback.published(topic, payload);
    }
    
    public void register(String topic, IPublishCallback publishCallback) {
        //register the publishCallback in some registry to be notified
        subscribersList.put(topic, publishCallback);
    }
    
    
    //Remove the registration of the callback from the topic
    private void unregister(String topic) {
        subscribersList.remove(topic);
    }
}
