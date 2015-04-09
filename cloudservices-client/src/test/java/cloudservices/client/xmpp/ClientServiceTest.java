package cloudservices.client.xmpp;

import cloudservices.client.ClientConfiguration;
import cloudservices.client.ClientService;
import cloudservices.client.ConfigException;
import cloudservices.client.ConnectException;
import cloudservices.client.packets.Packet;
import cloudservices.client.packets.TextPacket;

public class ClientServiceTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientConfiguration config = new ClientConfiguration("172.21.4.64", 1883);
		config.setResourceName("android");
		config.setUsername("123456789012121");
		config.setPassword("kk-xtd-push");
		config.setTopic("common");
		config.setSendUrl("http://127.0.0.1:8080/cloudservices-web/api/send");
		config.setReceiveUrl("http://127.0.0.1:8080/cloudservices-web/api/receive");
		config.setConnectType(2);
		
		ClientService client = ClientService.getInstance();
		try {
			client.config(config);
			client.startup();
			client.connect();
		} catch (ConfigException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			System.out.println(e1.getMessage());
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		while(true) {
			//client.sendPacket(new Packet());
			try {
				Thread.sleep(2000);
				TextPacket t = new TextPacket();
				t.setText("123123");
				client.sendPacket(t);
				//break;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
