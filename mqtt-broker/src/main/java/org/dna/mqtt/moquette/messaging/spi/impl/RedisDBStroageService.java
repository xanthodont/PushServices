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
import org.dna.mqtt.moquette.messaging.spi.impl.subscriptions.SubConstants;
import org.dna.mqtt.moquette.messaging.spi.impl.subscriptions.Subscription;
import org.dna.mqtt.moquette.proto.messages.AbstractMessage.QOSType;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisDBStroageService implements IStorageService {
	private JedisPoolConfig config;
	private JedisPool jedisPool;
	/** 离线消息存储 */
	private Map<String, List<PublishEvent>> persistentMessageStore;
	
	public RedisDBStroageService() {
		// TODO Auto-generated constructor stub
		config = new JedisPoolConfig();
		config.setMaxIdle(100);
		config.setMaxTotal(500);
		jedisPool = new JedisPool(config, "127.0.0.1");  
	}

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
		List<StoredMessage> results = new ArrayList<StoredMessage>();
		return results;
	}

	@Override
	public void storePublishForFuture(PublishEvent evt) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.lpush(evt.getClientID().getBytes(), SerializationUtil.serialize(evt));
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			jedisPool.returnResourceObject(jedis);
		}
		
	}

	@Override
	public List<PublishEvent> retrivePersistedPublishes(String clientID) {
		// TODO Auto-generated method stub
		List<PublishEvent> evts = new ArrayList<PublishEvent>();
		byte[] clientKey = clientID.getBytes();
		byte[] value = null;
		Jedis jedis = jedisPool.getResource();
		try {
			while ((value = jedis.rpop(clientKey)) != null) {
				evts.add((PublishEvent) SerializationUtil.deserialize(value));
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			jedisPool.returnResourceObject(jedis);
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
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.sadd(SubConstants.getSubKeyBytes(clientID), SerializationUtil.serialize(newSubscription));
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			jedisPool.returnResourceObject(jedis);
		}
		/*
		if (!m_persistentSubscriptions.containsKey(clientID)) {
            m_persistentSubscriptions.put(clientID, new HashSet<Subscription>());
        }

        Set<Subscription> subs = m_persistentSubscriptions.get(clientID);
        if (!subs.contains(newSubscription)) {
            subs.add(newSubscription);
            m_persistentSubscriptions.put(clientID, subs);
        }*/
	}

	private void fillSubs(HashSet<Subscription> subs, Set<byte[]> subBytes) {
		// TODO Auto-generated method stub
		if (subBytes != null) {
			for (byte[] bs : subBytes) {
				Subscription subscription = (Subscription) SerializationUtil.deserialize(bs);
				subscription.setActive(false);
				subs.add(subscription);
			}
		}
	}

	@Override
	public void removeAllSubscriptions(String clientID) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.del(SubConstants.getSubKeyBytes(clientID));
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			jedisPool.returnResourceObject(jedis);
		}
	}

	@Override
	public List<Subscription> retrieveAllSubscriptions() {
		// TODO Auto-generated method stub
		List<Subscription> allSubscriptions = new ArrayList<Subscription>();
		Jedis jedis = jedisPool.getResource();
		try {
			Set<byte[]> clients = jedis.keys(SubConstants.getSubPrefixParttenBytes());
			for (byte[] client : clients) {
				Set<byte[]> subBytes = jedis.smembers(client);
				HashSet<Subscription> subs = new HashSet<Subscription>();
				fillSubs(subs, subBytes); // 转换
				allSubscriptions.addAll(subs);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			jedisPool.returnResourceObject(jedis);
		}
        return allSubscriptions;
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
