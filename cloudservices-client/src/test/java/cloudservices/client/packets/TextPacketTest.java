package cloudservices.client.packets;

import java.nio.ByteBuffer;

import org.junit.Test;

import junit.framework.Assert;
import cloudservices.client.ClientConfiguration;
import cloudservices.client.ClientService;
import cloudservices.client.ConfigException;
import cloudservices.client.ConnectException;
import cloudservices.client.TestBase;

public class TextPacketTest extends TestBase{

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
				
				TextPacket t = new TextPacket();
				t.setAck(true);
				t.setText(String.format("msg -- %d", i));
				client.sendPacket(t, "beidou/R");
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	@Test 
	public void toByteArrayTest() {
		TextPacket p = new TextPacket();
		p.setUsername("abc");
		p.setAck(true);
		p.setPacketType(1);
		p.setText("test");
		byte[] bs = p.toByteArray();
		TextPacket r = new TextPacket(Packet.parse(ByteBuffer.wrap(bs)));
		System.out.printf("%s", r);
		Assert.assertEquals(p.getUsername(), r.getUsername());
		Assert.assertEquals(p.isAck(), r.isAck());
		Assert.assertEquals(p.getPacketType(), r.packetType);
	}
	
}
