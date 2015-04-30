package mvc.dao.base;

public class LimitCondition {
	private int page;
	private int size;
	
	public LimitCondition(int page, int size) {
		this.page = page;
		this.size = size;
	}

	public int getPage() {
		// TODO Auto-generated method stub
		return this.page;
	}
	
	public int getSize() {
		return this.size;
	}
}
