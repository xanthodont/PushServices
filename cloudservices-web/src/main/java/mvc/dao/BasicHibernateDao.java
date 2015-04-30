package mvc.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mvc.dao.base.SqlConstants;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;



/**
 * DAO的Hibernate基类
 * 
 * @author jan
 * @param <T>
 *            pojo的类型
 * @para <ID> id的类型
 * 
 */
public class BasicHibernateDao<T, ID extends Serializable> extends HibernateDaoSupport implements IBasicDao<T, ID> {
	private Class<T> persistentClass;
	private JdbcTemplate jdbcTemplate ;
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@SuppressWarnings("unchecked")
	public BasicHibernateDao() {
		// 获取持久化对象的类型
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	/**
	 * 增加
	 */
	public void add(T entity) {
		this.getHibernateTemplate().save(entity);
	}

	/**
	 * 新增或更新
	 */
	public void saveOrUpdate(T entity) {
		this.getHibernateTemplate().saveOrUpdate(entity);
	}

	/**
	 * 更新
	 */
	public void update(T entity) {
		this.getHibernateTemplate().update(entity);
	}

	/**
	 * 删除
	 */
	public void delete(T entity) {
		this.getHibernateTemplate().delete(entity);
	}

	/**
	 * 通过属性删除
	 */
	public void deleteByProperty(String propertyName, Object value) {
		Session session = this.getSession();
		String queryString = "delete from " + getPersistentClass().getName() + " as model where model." + propertyName + "= ?";
		Query query = session.createQuery(queryString);
		query.setParameter(0, value);
		query.executeUpdate();
		this.releaseSession(session);
	}

	/**
	 * 通过id查找
	 * 
	 * @param id
	 * @return
	 */
	public T findById(ID id) {
		return (T) this.getHibernateTemplate().get(getPersistentClass(), id);
	}

	/**
	 * find By Example
	 * 
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByExample(T entity) {
		return this.getHibernateTemplate().findByExample(entity);
	}

	/**
	 * 查找所有记录
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return (List<T>) this.getHibernateTemplate().find("from " + getPersistentClass().getName());
	}
	
	/**
	 * 分页查询
	 * 
	 * @param propertyList
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<T> findByPropertys(List<SqlProperty> propertyList, int page, int pageSize, String orderbyName, boolean isSequence) {
		Session session = this.getSession();
		List paramValueList = null;
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("from " + getPersistentClass().getName());
		strBuffer.append(" as model where 1=1");
		SqlProperty sqlProperty = null;
		if (propertyList != null && !propertyList.isEmpty()) {
			paramValueList = new ArrayList();
			for (int i = 0; i < propertyList.size(); i++) {
				sqlProperty = propertyList.get(i);
				strBuffer.append(" and");
				strBuffer.append(" model.");
				strBuffer.append(sqlProperty.getName());
				if (SqlConstants.conditionType_equal.equals(sqlProperty.getType())) {
					strBuffer.append(" =");
					strBuffer.append(" ?");
				} else if (SqlConstants.conditionType_like.equals(sqlProperty.getType())) {
					strBuffer.append(" like");
					strBuffer.append(" ?");
				} else if (SqlConstants.conditionType_gt.equals(sqlProperty.getType())) {
					strBuffer.append(" >=");
					strBuffer.append(" ?");
				} else if (SqlConstants.conditionType_lt.equals(sqlProperty.getType())) {
					strBuffer.append(" <=");
					strBuffer.append(" ?");
				}
				paramValueList.add(sqlProperty.getValue());

			}
		}
		String orderbyString = " order by model." + orderbyName;
		if (isSequence == false) {
			orderbyString = orderbyString + " DESC";
		}
		strBuffer.append(orderbyString);
		Query query = session.createQuery(strBuffer.toString());
		if (paramValueList != null && !paramValueList.isEmpty()) {
			for (int i = 0; i < paramValueList.size(); i++) {
				query.setParameter(i, paramValueList.get(i));
			}
		}
		int firstResult = (page - 1) * pageSize;
		query.setFirstResult(firstResult);
		query.setMaxResults(pageSize);
		List<T> list = query.list();
		this.releaseSession(session);
		return list;
	}

	/**
	 * 分页查找所有的记录
	 * 
	 * @param page
	 *            要返回的页数
	 * @param pageSize
	 *            没有记录数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAll(int page, int pageSize) {
		Session session = this.getSession();
		String queryString = "from " + getPersistentClass().getName();
		Query query = session.createQuery(queryString);
		int firstResult = (page - 1) * pageSize;
		query.setFirstResult(firstResult);
		query.setMaxResults(pageSize);
		List<T> list = query.list();
		this.releaseSession(session);
		return list;
	}

	/**
	 * 通过属性查找
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性的值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByProperty(String propertyName, Object value) {
		String queryString = "from " + getPersistentClass().getName() + " as model where model." + propertyName + "= ?";
		return (List<T>) this.getHibernateTemplate().find(queryString, value);

	}

	/**
	 * 通过多个属性组合查询
	 * 
	 * @param propertyNames
	 *            属性名称数组
	 * @param values
	 *            对应于propertyNames的值 return 匹配的结果
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByPropertys(String[] propertyNames, Object[] values) {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("from " + getPersistentClass().getName());
		strBuffer.append(" as model where ");
		for (int i = 0; i < propertyNames.length; i++) {
			if (i != 0) {
				strBuffer.append(" and");
			}
			strBuffer.append(" model.");
			strBuffer.append(propertyNames[i]);
			strBuffer.append("=");
			strBuffer.append("? ");
		}
		String queryString = strBuffer.toString();
		return (List<T>) this.getHibernateTemplate().find(queryString, values);
	}

	/**
	 * 通过多个属性组合查询(重载方法)
	 * 
	 * @param propertyNames
	 *            属性名称
	 * @param values
	 *            对应于propertyNames的值 return 匹配的结果
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByPropertys(List<String> propertyNames, List<String> propertyValues) {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("from " + getPersistentClass().getName());
		strBuffer.append(" as model where 1=1");
		if (propertyNames != null && !propertyNames.isEmpty()) {
			for (int i = 0; i < propertyNames.size(); i++) {
				strBuffer.append(" and");
				strBuffer.append(" model.");
				strBuffer.append(propertyNames.get(i));
				strBuffer.append("=");
				strBuffer.append("? ");
			}
		}

		String queryString = strBuffer.toString();
		return (List<T>) this.getHibernateTemplate().find(queryString, propertyValues);
	}

	/**
	 * 通过属性查找并分页
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 * @param page
	 *            页数
	 * @param pageSize
	 *            每页显示条数
	 */
	public List<T> findByProperty(String propertyName, Object value, int page, int pageSize) {
		return this.findByPropertys(new String[] { propertyName }, new Object[] { value }, page, pageSize);
	}

	/**
	 * 通过多个属性组合查询
	 * 
	 * @param propertyNames
	 *            属性名称数组
	 * @param values
	 *            对应于propertyNames的值
	 * @param page
	 *            页数
	 * @param pageSize
	 *            每页显示数 return 匹配的结果 return 匹配的结果
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByPropertys(String[] propertyNames, Object[] values, int page, int pageSize) {
		Session session = this.getSession();
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("from " + getPersistentClass().getName());
		strBuffer.append(" as model where 1=1");
		for (int i = 0; i < propertyNames.length; i++) {
			strBuffer.append(" and");
			strBuffer.append(" model.");
			strBuffer.append(propertyNames[i]);
			strBuffer.append("=");
			strBuffer.append("? ");
		}
		String queryString = strBuffer.toString();

		int firstResult = (page - 1) * pageSize;

		Query query = session.createQuery(queryString);
		query.setFirstResult(firstResult);
		query.setMaxResults(pageSize);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}

		List<T> list = query.list();
		this.releaseSession(session);
		return list;
	}

	/**
	 * 查找T并通过某一属性排序
	 * 
	 * @param property
	 *            排序依据的顺序
	 * @param isSequence
	 *            是否顺序排序,false为倒序
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAndOrderByProperty(int firstResult, int fetchSize, String propertyName, boolean isSequence) {
		Session session = this.getSession();
		String queryString = "from " + getPersistentClass().getName() + " as model order by model." + propertyName;
		if (isSequence == false) {
			queryString = queryString + " DESC";
		}

		Query queryObject = session.createQuery(queryString);
		queryObject.setFirstResult(firstResult);
		queryObject.setMaxResults(fetchSize);
		List<T> list = queryObject.list();
		this.releaseSession(session);
		return list;

	}

	/**
	 * 查找所有并通过某个属性排序
	 * 
	 * @param propertyName
	 *            排序依据的属性名称
	 * @param isSequence
	 *            是否顺序排列
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAllAndOrderByProperty(String propertyName, boolean isSequence) {
		Session session = this.getSession();
		String queryString = "from " + getPersistentClass().getName() + " as model order by model." + propertyName;
		if (isSequence == false) {
			queryString = queryString + " DESC";
		}

		Query queryObject = session.createQuery(queryString);
		List<T> list = queryObject.list();
		this.releaseSession(session);
		return list;
	}

	/**
	 * 分页查询
	 * 
	 * @param propertyList
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<T> findByPropertys(List<SqlProperty> propertyList, int page, int pageSize) {
		Session session = this.getSession();
		List paramValueList = null;
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("from " + getPersistentClass().getName());
		strBuffer.append(" as model where 1=1");
		SqlProperty sqlProperty = null;
		if (propertyList != null && !propertyList.isEmpty()) {
			paramValueList = new ArrayList();
			for (int i = 0; i < propertyList.size(); i++) {
				sqlProperty = propertyList.get(i);
				strBuffer.append(" and");
				strBuffer.append(" model.");
				strBuffer.append(sqlProperty.getName());
				if (SqlConstants.conditionType_equal.equals(sqlProperty.getType())) {
					strBuffer.append(" =");
					strBuffer.append(" ?");
				} else if (SqlConstants.conditionType_like.equals(sqlProperty.getType())) {
					strBuffer.append(" like");
					strBuffer.append(" ?");
				} else if (SqlConstants.conditionType_gt.equals(sqlProperty.getType())) {
					strBuffer.append(" >=");
					strBuffer.append(" ?");
				} else if (SqlConstants.conditionType_lt.equals(sqlProperty.getType())) {
					strBuffer.append(" <=");
					strBuffer.append(" ?");
				}
				paramValueList.add(sqlProperty.getValue());

			}
		}
		Query query = session.createQuery(strBuffer.toString());
		if (paramValueList != null && !paramValueList.isEmpty()) {
			for (int i = 0; i < paramValueList.size(); i++) {
				query.setParameter(i, paramValueList.get(i));
			}
		}
		int firstResult = (page - 1) * pageSize;
		query.setFirstResult(firstResult);
		query.setMaxResults(pageSize);
		List<T> list = query.list();
		this.releaseSession(session);
		return list;
	}

	/**
	 * 不分页的查询
	 * 
	 * @param propertyList
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<T> findByPropertys(List<SqlProperty> propertyList) {
		Session session = this.getSession();
		List paramValueList = null;
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("from " + getPersistentClass().getName());
		strBuffer.append(" as model where 1=1");
		SqlProperty sqlProperty = null;
		if (propertyList != null && !propertyList.isEmpty()) {
			paramValueList = new ArrayList();
			for (int i = 0; i < propertyList.size(); i++) {
				sqlProperty = propertyList.get(i);

				strBuffer.append(" and");

				strBuffer.append(" model.");
				strBuffer.append(sqlProperty.getName());
				if (SqlConstants.conditionType_equal.equals(sqlProperty.getType())) {
					strBuffer.append(" =");
					strBuffer.append(" ?");
				} else if (SqlConstants.conditionType_like.equals(sqlProperty.getType())) {
					strBuffer.append(" like");
					strBuffer.append(" ?");
				} else if (SqlConstants.conditionType_gt.equals(sqlProperty.getType())) {
					strBuffer.append(" >=");
					strBuffer.append(" ?");
				} else if (SqlConstants.conditionType_lt.equals(sqlProperty.getType())) {
					strBuffer.append(" <=");
					strBuffer.append(" ?");
				}
				paramValueList.add(sqlProperty.getValue());

			}
		}
		Query query = session.createQuery(strBuffer.toString());
		if (paramValueList != null && !paramValueList.isEmpty()) {
			for (int i = 0; i < paramValueList.size(); i++) {
				query.setParameter(i, paramValueList.get(i));
			}
		}
		List<T> list = query.list();
		this.releaseSession(session);
		return list;
	}

	/**
	 * 统计所有记录的总数
	 * 
	 * @return 总数
	 */
	@SuppressWarnings("rawtypes")
	public int countAll() {
		Session session = this.getSession();
		String queryString = "select count(*) from " + getPersistentClass().getName();
		Query query = session.createQuery(queryString);
		List list = query.list();
		Long result = (Long) list.get(0);
		int count = result.intValue();
		this.releaseSession(session);
		return count;
	}

	/**
	 * 通过属性统计数量
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 */
	public int countByProperty(String propertyName, Object value) {
		String[] propertyNames = new String[] { propertyName };
		Object[] values = new Object[] { value };
		return this.countByPropertys(propertyNames, values);
	}

	/**
	 * 查找符合条件的记录总数
	 * 
	 * @param propertyList
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int countByPropertys(List<SqlProperty> propertyList) {
		Session session = this.getSession();
		List paramValueList = null;
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("select count(*) from " + getPersistentClass().getName());
		strBuffer.append(" as model where 1=1");
		SqlProperty sqlProperty = null;
		if (propertyList != null && !propertyList.isEmpty()) {
			paramValueList = new ArrayList();
			for (int i = 0; i < propertyList.size(); i++) {
				sqlProperty = propertyList.get(i);
				strBuffer.append(" and");
				strBuffer.append(" model.");
				strBuffer.append(sqlProperty.getName());
				if (SqlConstants.conditionType_equal.equals(sqlProperty.getType())) {
					strBuffer.append(" =");
					strBuffer.append(" ?");
				} else if (SqlConstants.conditionType_like.equals(sqlProperty.getType())) {
					strBuffer.append(" like");
					strBuffer.append(" ?");
				} else if (SqlConstants.conditionType_gt.equals(sqlProperty.getType())) {
					strBuffer.append(" >=");
					strBuffer.append(" ?");
				} else if (SqlConstants.conditionType_lt.equals(sqlProperty.getType())) {
					strBuffer.append(" <=");
					strBuffer.append(" ?");
				}
				paramValueList.add(sqlProperty.getValue());

			}
		}
		Query query = session.createQuery(strBuffer.toString());
		if (paramValueList != null && !paramValueList.isEmpty()) {
			for (int i = 0; i < paramValueList.size(); i++) {
				query.setParameter(i, paramValueList.get(i));
			}
		}

		Object countObject = query.uniqueResult();
		int count = Integer.parseInt(countObject.toString());

		this.releaseSession(session);
		return count;
	}

	/**
	 * 通过多个属性统计数量
	 * 
	 * @param propertyNames
	 *            属性名称数组
	 * @param values
	 *            对应的属性值数组 return
	 */
	@SuppressWarnings("rawtypes")
	public int countByPropertys(String[] propertyNames, Object[] values) {
		Session session = this.getSession();
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("select count(*) from " + getPersistentClass().getName());
		strBuffer.append(" as model where ");
		for (int i = 0; i < propertyNames.length; i++) {
			if (i != 0) {
				strBuffer.append(" and");
			}
			strBuffer.append(" model.");
			strBuffer.append(propertyNames[i]);
			strBuffer.append("=");
			strBuffer.append("? ");
		}

		String queryString = strBuffer.toString();
		Query query = session.createQuery(queryString);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}

		List list = query.list();
		Long result = (Long) list.get(0);
		int count = result.intValue();
		this.releaseSession(session);
		return count;
	}

	/**
	 * 执行sql
	 * 
	 * @param sql
	 * @param paramList
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void excuteSql(final String sql, final List<Object> paramList) {
		getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery(sql);
				if (paramList != null && !paramList.isEmpty()) {
					for (int i = 0; i < paramList.size(); i++) {
						sqlQuery.setParameter(i, paramList.get(i));
					}
				}

				sqlQuery.executeUpdate();
				return null;
			}
		});
	}

}