package cloudservices.client.mqtt;

import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

import cloudservices.client.ClientConfiguration;
import cloudservices.client.ClientService;
import cloudservices.client.ConfigException;
import cloudservices.client.ConnectException;
import cloudservices.client.ISender;
import cloudservices.client.packets.Packet;
import cloudservices.utils.StringUtil;

public class MQTTClientServiceFuture implements ISender{
	private ClientService clientService;
	private MQTT mqtt;
	private FutureConnection connection;
	private volatile boolean done = false;
	
	private Topic[] subscribeTopics;
	private String publicTopic;
	
	public MQTTClientServiceFuture(ClientService clientService) {
		this.clientService = clientService;
		MQTT mqtt = new MQTT();
	}
	
	public void config(ClientConfiguration config) throws ConfigException {
        try {
        	// 设置mqtt broker的ip和端口  
			mqtt.setHost(config.getMqttHost());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ConfigException("mqtt host配置有误!");
		}  
        // 连接前清空会话信息, false表示保留回话信息，在用户断线的情况下保留要发送给此客户端的消息  
        mqtt.setCleanSession(false);  
        // 设置重新连接的次数  
        mqtt.setReconnectAttemptsMax(config.getReconnectAttemptsMax());  
        // 设置重连的间隔时间  
        mqtt.setReconnectDelay(config.getReconnectDelay()*1000);  
        mqtt.setReconnectDelayMax(config.getReconnectDelayMax()*1000);
        mqtt.setReconnectBackOffMultiplier(config.getReconnectBackOffMultiplier());
        // 设置心跳时间  
        mqtt.setKeepAlive(config.getKeepAlive());  
        // 设置缓冲的大小  
        mqtt.setSendBufferSize(config.getBufferSize());
        mqtt.setReceiveBufferSize(config.getBufferSize());
        
        // 设置用户名和密码
        if (StringUtil.isEmpty(config.getUsername())) throw new ConfigException("未设置用户名");
        if (StringUtil.isEmpty(config.getPassword())) throw new ConfigException("未设置密码");
        mqtt.setClientId(config.getUsername());
        mqtt.setUserName(config.getUsername());
        mqtt.setPassword(config.getPassword());
        
        connection = mqtt.futureConnection();
        //BlockingConnection blockingConnection = mqtt.blockingConnection();
        //blockingConnection.connect();
        //blockingConnection.isConnected();
        
        
        
	}
	
	public ClientConfiguration getConfiguration() {
		return clientService.getConfiguration();
	}
	
	public ClientService getClientService() {
		return clientService;
	}
	
	public void connect() throws ConnectException {
		Future<Void> connectFuture = connection.connect();
		try {
			connectFuture.await(10L, TimeUnit.SECONDS);  // 等待连接完成，超时时间为10秒
			if (!connection.isConnected()) throw new ConnectException("未能连上服务器"); 
			// 连接成功，开始订阅频道
			Future<byte[]> subFuture = connection.subscribe(new Topic[]{});
			byte[] sub = subFuture.await(10L, TimeUnit.SECONDS);
			if (sub == null) if (!connection.isConnected()) throw new ConnectException("订阅失败");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (!connection.isConnected()) throw new ConnectException("未能连上服务器");
			else throw new ConnectException("连接异常");
		}
		
		// 启动消息接收线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				receiveMessage();
			}
		}).start();
	}
	
	private void receiveMessage() {
		while (!done) {
			Future<Message> futrueMessage = connection.receive();  
            try {
				Message message = futrueMessage.await();
				message.ack();
				Packet packet = parsePacketByMessage(message);
				this.clientService.getPacketReader().putPacket(packet);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private Packet parsePacketByMessage(Message message) {
		// TODO Auto-generated method stub
		//publish.
		ByteBuffer buffer = ByteBuffer.wrap(message.getPayload());
		int type = buffer.getInt();
		Packet packet = new Packet();
		//packet.setMessageId(publish.messageId());
		packet.setPacketType(type);
		byte[] remain = new byte[buffer.remaining()];
		buffer.get(remain);
		packet.setRemainBytes(remain);
		return packet;
	}

	@Override
	public void send(Packet packet) {
		// TODO Auto-generated method stub
		connection.publish(publicTopic, packet.toByteArray(), QoS.AT_LEAST_ONCE, false);
	}
}
