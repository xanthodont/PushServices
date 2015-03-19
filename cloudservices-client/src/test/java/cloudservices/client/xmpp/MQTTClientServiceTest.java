package cloudservices.client.xmpp;

import cloudservices.client.ClientConfiguration;
import cloudservices.client.mqtt.IPublishCallback;
import cloudservices.client.mqtt.MQTTClientService;

public class MQTTClientServiceTest {
	public final static int MQTT_PORT = 1883; 

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientConfiguration config = new ClientConfiguration("172.21.4.64", MQTT_PORT);
		config.setResourceName("android");
		config.setUsername("12345678901234");
		
		MQTTClientService client = new MQTTClientService(config);
		client.connect();
		
		String topic = "cmm/"+config.getUsername();
		client.subscribe("cmm", new IPublishCallback() {
			public void published(String topic, byte[] message) {
				// TODO Auto-generated method stub
				System.out.printf("cmm Receive message:%s\n", new String(message));
			}
		});
		client.subscribe(topic, new IPublishCallback() {
			public void published(String topic, byte[] message) {
				// TODO Auto-generated method stub
				System.out.printf("Receive message:%s\n", new String(message));
			}
		});
	}

}
