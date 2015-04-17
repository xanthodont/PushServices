package cloudservices.client.mqtt;

import java.util.concurrent.ArrayBlockingQueue;

import org.fusesource.mqtt.codec.PUBLISH;

import cloudservices.client.ClientConfiguration;
import cloudservices.client.mqtt.MQTTClientService;

public class MQTTClientServiceTest {
	public static void main(String[] args) {
		ClientConfiguration config = new ClientConfiguration("127.0.0.1", 1883);
		config.setResourceName("android");
		config.setUsername("12345678901234");
		
		/*
		MQTTClientService client = new MQTTClientService(null);
		
		client.connect();
		
		while(true) {
			client.send(null);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
	}
}
