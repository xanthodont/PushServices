package mvc.dao;

import java.io.Serializable;
import java.util.List;

/**
 * DAO 基类接口
 * 
 * @param <T>T pojo类型
 * @param <ID>ID类型
 */
public interface IBasicDao<T, ID extends Serializable> 
{

	public void add(T entity);
	public void delete(T entity);
	public void saveOrUpdate(T entity);
	public void update(T entity);
	public T findById(ID id);
	/**
	 * 查所有
	 * @return
	 */
	public List<T> findAll();

	/**
	 * 查找所有，并分页
	 * 
	 * @param page
	 *            要返回的页数
	 * @param pageSize
	 *            没有记录数
	 * @return
	 */
	public List<T> findAll(int page, int pageSize);

	

	/**
	 * 与findByProperty相似，当properyName == value 时把相应的记录删除
	 */
	public void deleteByProperty(String propertyName, Object value);

	public List<T> findByExample(T example);

	/**
	 * 通过属性查找
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性的值
	 * @return
	 */
	public List<T> findByProperty(String propertyName, Object value);

	/**
	 * 通过多个属性查找
	 * 
	 * @param propertyNames
	 *            属性名称数组
	 * @param values
	 *            属性值数组
	 * @return
	 */
	public List<T> findByPropertys(String[] propertyNames,Object[] values);

	/**
	 * 通过多个属性查找，并分页， 属性名称数组和属性值数组的序列要对应
	 * 
	 * @param propertyNames
	 *            属性名称数组
	 * @param values
	 *            属性值数组
	 * @param page
	 *            页码
	 * @param pageSize
	 *            每页内容条数
	 * @return
	 */
	public List<T> findByPropertys(String[] propertyNames, Object[] values,int page, int pageSize);

	/**
	 * 通过属性查找，并分页， 属性名称数组和属性值数组的序列要对应
	 * 
	 * @param propertyNames
	 *            属性名称
	 * @param values
	 *            属性值
	 * @param page
	 *            页码
	 * @param pageSize
	 *            每页内容条数
	 * @return
	 */
	public List<T> findByProperty(String propertyName, Object value, int page,int pageSize);

	/**
	 * 统计所有记录的总数
	 * 
	 * @return 总数
	 */
	public int countAll();

	/**
	 * 统计数据库中当propertyName=value时的记录总数
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public int countByProperty(String propertyName, Object value);

	/**
	 * 统计数据库中当多个propertyName=value时的记录总数
	 * 
	 * @param propertyNames
	 * @param values
	 * @return
	 */
	public int countByPropertys(String[] propertyNames, Object[] values);

	

	/**
	 * 获得持久化对象的类型
	 * 
	 * @return
	 */
	public Class<T> getPersistentClass();

	/**
	 * 查找并通过某一属性排序
	 * 
	 * @param property
	 *            排序依据的顺序
	 * @param isSequence
	 *            是否顺序排序
	 */
	public List<T> findAndOrderByProperty(int firstResult, int fetchSize,String propertyName, boolean isSequence);

	/**
	 * 查找并通过某一属性排序
	 * 
	 * @param property
	 *            排序依据的顺序
	 * @param isSequence
	 *            是否顺序排序
	 */
	public List<T> findAllAndOrderByProperty(String propertyName,boolean isSequence);

}