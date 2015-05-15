package org.dna.mqtt.moquette.server;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.dna.mqtt.moquette.messaging.spi.IMessaging;
import org.dna.mqtt.moquette.proto.Utils;
import org.dna.mqtt.moquette.proto.messages.AbstractMessage;

import static org.dna.mqtt.moquette.proto.messages.AbstractMessage.*;

import org.dna.mqtt.moquette.proto.messages.PingRespMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MINA MQTT Handler used to route messages to protocol logic
 *
 * @author andrea
 */
public class MQTTHandler extends IoHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(MQTTHandler.class);
    private IMessaging m_messaging;

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        AbstractMessage msg = (AbstractMessage) message;
        LOG.info(String.format("Received a message of type %s", Utils.msgType2String(msg.getMessageType())));
        try {
            switch (msg.getMessageType()) {
                case CONNECT:
                case SUBSCRIBE:
                case UNSUBSCRIBE:
                case PUBLISH:
                case PUBREC:
                case PUBCOMP:
                case PUBREL:
                case DISCONNECT:
                    m_messaging.handleProtocolMessage(session, msg);
                    break;
                case PINGREQ:
                    PingRespMessage pingResp = new PingRespMessage();
                    session.write(pingResp);
                    break;
            }
        } catch (Exception ex) {
            LOG.error("Bad error in processing the message", ex);
        }
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
    	String username = (String) session.getAttribute(Constants.ATTR_CLIENTID);
    	LOG.info("session idle -- "+username);
        if (status == IdleStatus.READER_IDLE) {
            session.close(false);
            //TODO send a notification to messaging part to remove the bining clientID-ConnConfig
        }
    }
    
    @Override 
    public void exceptionCaught(IoSession session, Throwable throwable) {
    	String username = (String) session.getAttribute(Constants.ATTR_CLIENTID);
    	LOG.info("session exception caught-- "+username);
    	session.close(false);
    }

    public void setMessaging(IMessaging messaging) {
        m_messaging = messaging;
    }

    public void sessionClosed(IoSession session) throws Exception {
        // Empty handler
    	String username = (String) session.getAttribute(Constants.ATTR_CLIENTID);
    	ConnectionDescriptor client = m_messaging.getCliendIds().get(username);
    	if (client != null && client.getSession() == session) { 
	    	m_messaging.getSubscriptions().deactivate(username);
	    	m_messaging.getCliendIds().remove(username);
	    	m_messaging.getConnCallback().disconnect(username);
    	} else {
    		LOG.info("关闭旧Session:"+username);
    	}
    }
}
