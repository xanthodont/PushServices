package mvc.dao;

import java.util.ArrayList;
import java.util.List;

public class PageList<TEntity> {
	private List rows;
	private int total;

	public PageList() {
	}
	public PageList(List rows, int total) {
		this.rows = rows;
		this.total = total;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
}
