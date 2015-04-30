package mvc.scheduler;

import java.util.Set;

import mvc.model.OnlineFormat;
import mvc.model.OnlineStatus;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import utils.ApplicationContextUtil;
import utils.StringUtil;


public class OnlineStatusUpdateJob {
	private static Logger logger = Logger.getLogger(OnlineStatusUpdateJob.class);
	private JedisPool jedisPool;
	
	public OnlineStatusUpdateJob() {
		jedisPool = (JedisPool) ApplicationContextUtil.getBeanByName("jedisPool");
	}
	
	public void execute() {
		// TODO Auto-generated method stub
		logger.info("-update online status-");
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> onlineUsers = jedis.keys(OnlineStatus.STATUS_PREFIX + "*");
			long now = System.currentTimeMillis();
			for (String onlineKey : onlineUsers) {
				String val = jedis.get(onlineKey);
				if (StringUtil.isEmpty(val)) continue;
				OnlineFormat of = OnlineFormat.parseValue(val);
				if (of.getStatus().equals(OnlineStatus.STATUS_ONLINE_SHORT)) { // 短连接在线情况
					// 检测是否过期
					int iCircle = Integer.parseInt(of.getCircle()) * 1000;
					if (now - of.getUpdateTime() > iCircle) {
						of.setStatus(OnlineStatus.STATUS_OFFLINE);
						of.setUpdateTime(now);
						jedis.set(onlineKey, of.toString());
					}
				}
			}
		} catch (Exception e) {
			logger.info("-update online status- exception", e);
		} finally {
			jedisPool.returnResourceObject(jedis);
		}
	}

	
}
