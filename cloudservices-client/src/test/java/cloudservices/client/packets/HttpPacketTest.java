package cloudservices.client.packets;

import cloudservices.client.ClientConfiguration;
import cloudservices.client.ClientService;
import cloudservices.client.ConfigException;
import cloudservices.client.ConnectException;
import cloudservices.client.TestBase;
import cloudservices.client.http.async.support.ParamsWrapper;

public class HttpPacketTest extends TestBase {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientConfiguration config = new ClientConfiguration(SERVER_IP,
				MQTT_PORT);
		config.setUsername("Text_send");
		config.setPassword(DEFAULT_PASSWORD);
		config.setTopic(TOPIC);
		config.setSendUrl(SEND_URL);
		config.setReceiveUrl(RECEIVE_URL);
		config.setConnectUrl(CONNECT_URL);
		config.setConnectType(1);

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
				i++;
				
				HttpPacket t = new HttpPacket();
				t.setAck(true);
				t.setUrl("");
				ParamsWrapper params = new ParamsWrapper();
				t.setParams(params);
				client.sendPacket(t, "beidou/R");
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
