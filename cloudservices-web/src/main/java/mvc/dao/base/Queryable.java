package mvc.dao.base;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;

import mvc.dao.SqlProperty;
import mvc.dao.PageList;

public class Queryable<TEntity> extends ArrayList implements IQueryable<TEntity> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Session session;
	private Class<TEntity> persistentClass;
	
	private List<SqlProperty> andCondition;
	private List<SqlProperty> orCondition;
	private LimitCondition limitCondition;
	private OrderByCondition orderByCondition; 
	
	public enum OperateType { all, select, delete, update, first, count; }
	private OperateType opType;
	
	private List<Object> paramValueList;

	public Queryable(Session session, Class<TEntity> persistentClass, OperateType opType) {
		// TODO Auto-generated constructor stub
		this.session = session;
		this.persistentClass = persistentClass;
		this.andCondition = new ArrayList<SqlProperty>();
		this.orCondition = new ArrayList<SqlProperty>();
		this.paramValueList = new ArrayList<Object>();
		
		this.opType = opType;
	}
	
	public Queryable append(String s) {
		return this;
	}
	
	public Queryable<TEntity> where(ICondition and) {
		//sql.append(" WHERE ").append("");//predicate.
		if (and != null) {
			SqlProperty sp = new SqlProperty();
			and.condition(sp);
			andCondition.add(sp);
		}
		return this;
	}
	
	public Queryable<TEntity> and(ICondition and) {
		//sql.append(" WHERE ").append("");//predicate.
		if (and != null) {
			SqlProperty sp = new SqlProperty();
			and.condition(sp);
			andCondition.add(sp);
		}
		return this;
	}
	public Queryable<TEntity> or(ICondition or) {
		//sql.append(" WHERE ").append("");//predicate.
		SqlProperty sp = new SqlProperty();
		or.condition(sp);
		orCondition.add(sp);
		return this;
	}
	
	public Queryable<TEntity> limit(int page, int size) {
		limitCondition = new LimitCondition(page, size);
		return this;
	}
	
	public Queryable<TEntity> orderBy(boolean order, String... properties) {
		this.orderByCondition = new OrderByCondition(order, properties);
		return this;
	}
	
	private Query getQuery() {
		System.out.println(toSqlString());
		Query query = session.createQuery(toSqlString());
		query.setCacheMode(CacheMode.IGNORE);  // 不和二级缓存交换数据
		if (paramValueList != null && !paramValueList.isEmpty()) {
			for (int i = 0; i < paramValueList.size(); i++) {
				query.setParameter(i, paramValueList.get(i));
			}
		}
		if (limitCondition != null) {
			query.setFirstResult((limitCondition.getPage()-1)*limitCondition.getSize());
			query.setMaxResults(limitCondition.getSize());
		}
		return query;
	}
	
	public int exec() {
		int r = getQuery().executeUpdate();
		session.close();
		return r;
	}
	
	public long toCount() {
		return toCount(true);
	}
	public long toCount(boolean isClosed) {
		/*
		List list = getQuery().list();
		if (list.size() == 0) return 0; 
		long count = (long) list.get(0);
		if (isClosed) session.close();
		*/
		return 0;
	}

	@Override
	public List<TEntity> toList() {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		List<TEntity> list = getQuery().list();
		session.getTransaction().commit();
		session.close();
		return list;
	}
	

	@Override
	public PageList<TEntity> toPageList(String actionLink, int pageIndex, int pageSize) {
		// TODO Auto-generated method stub
		if (pageIndex < 1) pageIndex = 1;
		if (pageSize < 1) pageSize = 10;
		OperateType oldType = this.opType;
		this.opType = OperateType.count;
		long count = this.toCount(false);
		this.opType = oldType; 
		this.limit(pageIndex, pageSize);
		List<TEntity> list = this.toList();
		PageList<TEntity> pageList = null;//new PageList<TEntity>(list, new Pager(pageIndex, pageSize));
		/*
		long totalPage = (count-1)/pageSize + 1;
		totalPage = totalPage < 1 ? 1 : totalPage;
		pageList.getPager().setTotalPage(totalPage);
		int prev = (pageIndex - 1) > 0 ? (pageIndex - 1) : 1;
		int next = (pageIndex + 1) <= pageList.getPager().getTotalPage() ? (pageIndex + 1) : (int)totalPage; 
		pageList.getPager().setPrev(prev);
		pageList.getPager().setNext(next);
		pageList.getPager().setActionLink(actionLink);*/
		
		return pageList; 
	}
	@Override
	public PageList<TEntity> toPageList(int pageIndex) {
		return toPageList("link", pageIndex, 10);
	}

	@Override
	public String toSqlString() {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder();
		switch (opType) {
		case all:
			builder.append("FROM ").append(persistentClass.getName());
			break;
		case select:
			builder.append("FROM ").append(persistentClass.getName());
			break;	
		case delete:
			builder.append("DELETE FROM ").append(persistentClass.getName());
			break;
		case update:
			break;
		case first:
			builder.append("FROM ").append(persistentClass.getName());
			break;
		case count:
			builder.append("SELECT COUNT(*) FROM ").append(persistentClass.getName());
			break;
		}
		paramValueList = new ArrayList<Object>();
		if (andCondition.size() > 0 || orCondition.size() > 0) {
			builder.append(" WHERE ");
			for (int i = 0, size = andCondition.size(); i < size; i++) {
				SqlProperty sqlProperty = andCondition.get(i);
				if (sqlProperty != null) {
					if (i != 0) {
						builder.append(" AND ");
					}
					builder.append(sqlProperty.getName())
						   .append(" ").append(sqlProperty.getType()).append(" ")
						   .append("?");
					paramValueList.add(sqlProperty.getValue());
				}
			}
			for (int i = 0, size = orCondition.size(); i < size; i++) {
				SqlProperty sqlProperty = orCondition.get(i);
				if (sqlProperty != null) {
					if (paramValueList.size() > 0 || i != 0) {
						builder.append(" OR ");
					}
					builder.append(sqlProperty.getName())
					   	   .append(" ").append(sqlProperty.getType()).append(" ")
					       .append("?");
					paramValueList.add(sqlProperty.getValue());
				}
			}
		}
		if (orderByCondition != null) {
			builder.append(" ORDER BY ")
				   .append(orderByCondition.getProperties())
				   .append(orderByCondition.getOrder() ? " ASC " : " DESC ");
		}
		
		
		return builder.toString();
	}

	@Override
	public TEntity toEntity() {
		// TODO Auto-generated method stub
		List<TEntity> list = getQuery().list();
		session.close();
		if (list.size() < 1) return null;
		return list.get(0);
	}

}
