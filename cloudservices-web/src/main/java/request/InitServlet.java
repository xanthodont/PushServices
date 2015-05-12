package request;

import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

import mvc.model.OnlineStatus;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import request.writer.ConnectWriter;
import request.writer.InitConfiguration;
import request.writer.SendMessageWriter;
import utils.ApplicationContextUtil;


public class InitServlet  extends HttpServlet {
	private static Logger logger = Logger.getLogger(Logger.class);
	
	@Override
	public void init(ServletConfig config) {
		InitConfiguration initConfig = InitConfiguration.getInstance();
		//读取配置文件信息
		ResourceBundle bundle = ResourceBundle.getBundle("config");
		initConfig.setCacheQueueSize(Integer.parseInt(bundle.getString("cacheQueueSize")));
		initConfig.setCacheQueueThreadSize(Integer.parseInt(bundle.getString("cacheQueueThreadSize")));
		logger.debug("InitConfig: queueSize:" + initConfig.getCacheQueueSize() + " threadSize:" + initConfig.getCacheQueueThreadSize());
		JedisPool jedisPool = (JedisPool) ApplicationContextUtil.getBeanByName("jedisPool");
		
		// 启动数据库读写类
		ConnectWriter.getInstance().startup();
		SendMessageWriter.getInstance().startup();
		
		initOnlineStatus(jedisPool);
	}

	private void initOnlineStatus(JedisPool jedisPool) {
		logger.info("-- 清除在线信息 --");
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> onlineUsers = jedis.keys(OnlineStatus.STATUS_PREFIX + "*");
			// 删除在线状态信息
			for (String onlineKey : onlineUsers) {
				jedis.del(onlineKey);  
			}
		} catch (Exception e) {
			//logger.info("-update online status- exception", e);
		} finally {
			jedisPool.returnResourceObject(jedis);
		}
	}
}
