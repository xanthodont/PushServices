package mvc.dao.base;

import java.util.List;

import mvc.dao.PageList;

public interface IToList<TEntity> {
	List<TEntity> toList();
	
	PageList<TEntity> toPageList(String actionLink, int pageIndex, int pageSize); 
	PageList<TEntity> toPageList(int pageIndex); 
}
