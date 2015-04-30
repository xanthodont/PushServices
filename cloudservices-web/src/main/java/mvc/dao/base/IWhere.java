package mvc.dao.base;

public interface IWhere<TEntity> {
	IWhere<TEntity> and(ICondition and);
	IWhere<TEntity> or(ICondition or);
}
