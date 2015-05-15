package org.dna.mqtt.moquette.messaging.spi;

import java.util.Map;

import org.apache.mina.core.session.IoSession;
import org.dna.mqtt.moquette.messaging.spi.impl.events.MessagingEvent;
import org.dna.mqtt.moquette.messaging.spi.impl.events.PubAckEvent;
import org.dna.mqtt.moquette.messaging.spi.impl.subscriptions.SubscriptionsStore;
import org.dna.mqtt.moquette.proto.messages.AbstractMessage;
import org.dna.mqtt.moquette.proto.messages.AbstractMessage.QOSType;
import org.dna.mqtt.moquette.proto.messages.ConnectMessage;
import org.dna.mqtt.moquette.server.ConnectionDescriptor;

/**
 * Interface to the underling messaging system used to publish, subscribe.
 * 
 * @author andrea
 */
public interface IMessaging {

    void stop();

    void disconnect(IoSession session);

    void republishStored(String clientID);

    void handleProtocolMessage(IoSession session, AbstractMessage msg);
    
    IConnectCallback getConnCallback();
    SubscriptionsStore getSubscriptions();
    Map<String, ConnectionDescriptor> getCliendIds();
}
