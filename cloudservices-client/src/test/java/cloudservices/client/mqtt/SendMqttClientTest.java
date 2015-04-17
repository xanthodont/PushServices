package cloudservices.client.mqtt;

import cloudservices.client.ClientConfiguration;
import cloudservices.client.ClientService;
import cloudservices.client.ConfigException;
import cloudservices.client.ConnectException;
import cloudservices.client.TestBase;
import cloudservices.client.packets.TextPacket;

public class SendMqttClientTest extends TestBase {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientConfiguration config = new ClientConfiguration(SERVER_IP,
				MQTT_PORT);
		config.setUsername("mqtt_send");
		config.setPassword(DEFAULT_PASSWORD);
		config.setTopic(TOPIC);
		config.setSendUrl(SEND_URL);
		config.setReceiveUrl(RECEIVE_URL);
		config.setConnectUrl(CONNECT_URL);
		config.setConnectType(2);

		ClientService client = ClientService.getInstance();
		try {
			client.config(config);
			client.startup();
			client.connect();
		} catch (ConfigException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
			System.out.println(e1.getMessage());
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		int i = 0;
		while (true) {
			// client.sendPacket(new Packet());
			try {
				Thread.sleep(4000);
				TextPacket t2 = new TextPacket();
				t2.setText(String.format("to_http %s -- %d", config.getUsername(), i));
				client.sendPacket(t2, "beidou/R");
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
