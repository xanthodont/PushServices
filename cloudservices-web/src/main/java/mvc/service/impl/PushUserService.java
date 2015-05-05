package mvc.service.impl;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import utils.StringUtil;
import mvc.dao.IPushUserDao;
import mvc.dao.PageList;
import mvc.model.OnlineFormat;
import mvc.model.PushUser;
import mvc.service.IPushUserService;

public class PushUserService implements IPushUserService {
	private JedisPool jedisPool; 
	private IPushUserDao pushuserDao;

	@Override
	public PageList<PushUser> getUserList(int page, int size) {
		// TODO Auto-generated method stub
		int firstFetch = (page-1)*size;
		PageList<PushUser> pl = new PageList<PushUser>();
		List<PushUser> list = pushuserDao.findAndOrderByProperty(firstFetch, size, "updateTime", false);
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
		pl.setRows(list);
		pl.setTotal(pushuserDao.countAll());
		return pl;
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

	@Override
	public int getTotalOnline() {
		// TODO Auto-generated method stub
		int count = 0;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> list = jedis.keys("online-*");
			for (String user : list) {
				String v = jedis.get(user);
				if (!StringUtil.isEmpty(v)) {
					OnlineFormat of = OnlineFormat.parseValue(v);
					int status = Integer.parseInt(of.getStatus());
					if (status > 0) count++; 
				}
			}
		} catch (Exception e) {
			
		} finally {
			jedisPool.returnResourceObject(jedis);
		}
		return count;
	}

	
	
}
