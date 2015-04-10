package cloudservices.client;

public class TestBase {
	protected static final String SERVER_IP = "127.0.0.1";
	protected static final int MQTT_PORT = 1883;
	protected static final String SERVER_URL = "http://127.0.0.1:8080/cloudservices-web";
	protected static final String RECEIVE_URL = SERVER_URL + "/api/receive";
	protected static final String SEND_URL = SERVER_URL + "/api/send";
	protected static final String CONNECT_URL = SERVER_URL + "/api/connect";
	
	protected static final String TOPIC = "beidou";
	protected static final String DEFAULT_PASSWORD = "kk-xtd-push";
	
}
