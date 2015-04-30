package mvc.dao;

/**
 * sql属�?
 * @author jan
 *
 */
public class SqlProperty 
{
	private final String EQUAL = "="; 
	private final String NOT_EQUAL = "!=";
	private final String LIKE = "like";
	private final String IN = "in";
	
	private String name;
	private Object value;
	private String type;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getType() {
		
		return type;
	}
	public void setType(String type) {
		this.type = type;
		if (type.equals(LIKE)) {
			String value = (String) this.getValue();
			setValue("%"+value+"%");
		} else if (type.equals(IN)) {
			
		}
	}
	
	public void equals(String name, Object value) {
		this.setName(name);
		this.setValue(value);
		this.setType(EQUAL);
	}
	
	public void notEquals(String name, Object value) {
		this.setName(name);
		this.setValue(value);
		this.setType(NOT_EQUAL);
	}
	
	public void like(String name, Object value) {
		this.setName(name);
		this.setValue(value);
		this.setType(LIKE);
	}
}
