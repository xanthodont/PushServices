package org.dna.mqtt.moquette.messaging.spi.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dna.mqtt.moquette.messaging.spi.IMatchingCondition;
import org.dna.mqtt.moquette.messaging.spi.IStorageService;
import org.dna.mqtt.moquette.messaging.spi.impl.HawtDBStorageService.StoredMessage;
import org.dna.mqtt.moquette.messaging.spi.impl.events.PublishEvent;
import org.dna.mqtt.moquette.messaging.spi.impl.subscriptions.Subscription;
import org.dna.mqtt.moquette.proto.messages.AbstractMessage.QOSType;

import redis.clients.jedis.Jedis;

public class RedisDBStroageService implements IStorageService {
	
	private Jedis jedis = new Jedis("127.0.0.1");
	/** 离线消息存储 */
	private Map<String, List<PublishEvent>> persistentMessageStore;

	@Override
	public void initStore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void storeRetained(String topic, byte[] message, QOSType qos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<StoredMessage> searchMatching(IMatchingCondition condition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void storePublishForFuture(PublishEvent evt) {
		// TODO Auto-generated method stub
		jedis.lpush(evt.getClientID().getBytes(), SerializationUtil.serialize(evt));
	}

	@Override
	public List<PublishEvent> retrivePersistedPublishes(String clientID) {
		// TODO Auto-generated method stub
		List<PublishEvent> evts = new ArrayList<PublishEvent>();
		byte[] clientKey = clientID.getBytes();
		while (jedis.llen(clientKey) > 0L) {
			byte[] value = jedis.rpop(clientKey);
			evts.add((PublishEvent) SerializationUtil.deserialize(value));
		}
		return evts;
	}

	@Override
	public void cleanPersistedPublishes(String clientID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cleanInFlight(String msgID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addInFlight(PublishEvent evt, String publishKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addNewSubscription(Subscription newSubscription, String clientID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAllSubscriptions(String clientID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Subscription> retrieveAllSubscriptions() {
		// TODO Auto-generated method stub
		List<Subscription> subs = new ArrayList<Subscription>();
		return subs;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persistQoS2Message(String publishKey, PublishEvent evt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeQoS2Message(String publishKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PublishEvent retrieveQoS2Message(String publishKey) {
		// TODO Auto-generated method stub
		return null;
	}

}
