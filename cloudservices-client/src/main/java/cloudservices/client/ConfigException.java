package cloudservices.client;

public class ConfigException extends Exception {
	private String message;
	
	public ConfigException(String message) {
		super(message);
	}
}
