package mvc.dao;

import java.util.ArrayList;
import java.util.List;

public class PageList<TEntity> {
	private List list;
	private int total;

	public PageList(List list) {
		this.list = list;
	}
	public PageList(List list, int total) {
		this.list = list;
		this.total = total;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
}
