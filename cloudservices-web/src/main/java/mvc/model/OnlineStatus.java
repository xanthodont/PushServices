package mvc.model;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class OnlineStatus {
	public static final String STATUS_PREFIX = "online-";
	/** 离线 */
	public static final String STATUS_OFFLINE = "0";
	/** 短连接在线 */
	public static final String STATUS_ONLINE_SHORT = "1";
	/** 长连接在线 */
	public static final String STATUS_ONLINE_LONG = "2";
	
	public static String getOnlineKey(String username) {
		return STATUS_PREFIX + username;
	}
	
	/**
	 * 短连接在线状态更新
	 * @param jedisPool
	 * @param username
	 * @param circle
	 */
	public static void updateShortOnlineStatus(JedisPool jedisPool, String username, String circle) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(OnlineStatus.getOnlineKey(username), String.format("%s_%d_%s", OnlineStatus.STATUS_ONLINE_SHORT, System.currentTimeMillis(), circle));
		} catch (Exception e) {
			
		} finally {
			jedisPool.returnResourceObject(jedis);
		}
	}
	public static void updateOfflineStatus(JedisPool jedisPool, String username, String circle) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(OnlineStatus.getOnlineKey(username), String.format("%s_%d_%s", OnlineStatus.STATUS_OFFLINE, System.currentTimeMillis(), circle));
		} catch (Exception e) {
			
		} finally {
			jedisPool.returnResourceObject(jedis);
		}
	}
	
	
	/**
	 * 短连接在线状态更新
	 * @param jedisPool
	 * @param username
	 * @param circle
	 */
	public static void updateLongOnlineStatus(JedisPool jedisPool, String username, String circle) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			OnlineFormat of = new OnlineFormat(OnlineStatus.STATUS_ONLINE_LONG, System.currentTimeMillis(), circle);
			jedis.set(OnlineStatus.getOnlineKey(username), of.toString());
		} catch (Exception e) {
			
		} finally {
			jedisPool.returnResourceObject(jedis);
		}
	}
	
	
	
	
}
