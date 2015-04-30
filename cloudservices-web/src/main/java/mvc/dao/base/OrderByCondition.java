package mvc.dao.base;

import java.util.Collection;

public class OrderByCondition {
	private boolean order;
	private String[] properties;
	
	public OrderByCondition(boolean order, String[] properties) {
		this.order = order;
		this.properties = properties;
	}

	public String getProperties() {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < properties.length; i++) {
			String p = properties[i];
			if (i > 0) {
				builder.append(",");
			}
			builder.append(p);
		}
		return builder.toString();
	}
	
	public boolean getOrder() {
		return this.order;
	}
}
