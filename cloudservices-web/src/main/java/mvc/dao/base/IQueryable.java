package mvc.dao.base;

import java.util.List;

import mvc.dao.PageList;

public interface IQueryable<TEntity> extends List {
	IQueryable where(ICondition w);
	
	String toSqlString();
	
	long toCount();
	long toCount(boolean isClosed);
	int exec();
	
	IQueryable<TEntity> and(ICondition and);
	IQueryable<TEntity> or(ICondition or);
	IQueryable<TEntity> orderBy(boolean order, String... properties);
	
	List<TEntity> toList();
	
	TEntity toEntity();
	
	PageList<TEntity> toPageList(String actionLink, int pageIndex, int pageSize); 
	PageList<TEntity> toPageList(int pageIndex); 
}
