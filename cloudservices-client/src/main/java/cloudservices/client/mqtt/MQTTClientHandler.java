package cloudservices.client.mqtt;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.dna.mqtt.moquette.proto.messages.*;

import static org.dna.mqtt.moquette.proto.messages.AbstractMessage.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andrea
 */
public class MQTTClientHandler extends IoHandlerAdapter {
    
    //private static final Logger LOG = LoggerFactory.getLogger(MQTTClientHandler.class);
    
    MQTTClientService callback;

    MQTTClientHandler(MQTTClientService callback)  {
        this.callback = callback;
    } 
    
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        AbstractMessage msg = (AbstractMessage) message;
        //LOG.debug("Received a message of type " + msg.getMessageType());
        switch (msg.getMessageType()) {
            case CONNACK:
                handleConnectAck(session, (ConnAckMessage) msg);
                break;
            case SUBACK:
                handleSubscribeAck(session, (SubAckMessage) msg);
                break;
            case UNSUBACK:
                handleUnsubscribeAck(session, (UnsubAckMessage) msg);
                break;
//            case SUBSCRIBE:
//                handleSubscribe(session, (SubscribeMessage) msg);
//        break;
            case PUBLISH:
                handlePublish(session, (PublishMessage) msg);
                break;
            case PUBACK:
                handlePublishAck(session, (PubAckMessage) msg);
                break;
            case PINGRESP:    
                break;
        }
    }

    private void handlePublishAck(IoSession session, PubAckMessage msg) {
        callback.publishAckCallback(msg.getMessageID());
    }

    private void handleConnectAck(IoSession session, ConnAckMessage connAckMessage) {
        callback.connectionAckCallback(connAckMessage.getReturnCode());
    }

    private void handleSubscribeAck(IoSession session, SubAckMessage subAckMessage) {
        callback.subscribeAckCallback(subAckMessage.getMessageID());
    }
    
    private void handlePublish(IoSession session, PublishMessage pubMessage) {
        callback.publishCallback(pubMessage.getTopicName(), pubMessage.getPayload());
    }

    private void handleUnsubscribeAck(IoSession session, UnsubAckMessage unsubAckMessage) {
        callback.unsubscribeAckCallback(unsubAckMessage.getMessageID());
    }
}
