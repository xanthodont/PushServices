package mvc.service.impl;

import java.util.List;

import javax.annotation.Resource;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import utils.StringUtil;
import mvc.dao.IPushUserDao;
import mvc.model.OnlineFormat;
import mvc.model.PushUser;
import mvc.service.IPushUserService;

public class PushUserService implements IPushUserService {
	private JedisPool jedisPool; 
	private IPushUserDao pushuserDao;

	@Override
	public List<PushUser> getUserList() {
		// TODO Auto-generated method stub
		List<PushUser> list = pushuserDao.findAll();
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			for (PushUser user : list) {
				int status = 0;
				String v = jedis.get("online-"+user.getUsername());
				if (!StringUtil.isEmpty(v)) {
					OnlineFormat of = OnlineFormat.parseValue(v);
					status = Integer.parseInt(of.getStatus());
				}
				user.setStatus(status);
			}
		} catch (Exception e) {
			
		} finally {
			jedisPool.returnResourceObject(jedis);
		}
		return list;
	}

	public IPushUserDao getPushuserDao() {
		return pushuserDao;
	}

	public void setPushuserDao(IPushUserDao pushuserDao) {
		this.pushuserDao = pushuserDao;
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	
	
}
