package cloudservices.client.packets;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

	private TextPacket packet;
	@Before
	public void init() {
		packet = new TextPacket();
	}
	
	@Test 
	public void toByteArrayTest() {
		boolean ack = false;
		boolean sub = false;
		int mId = 10;
		short total = 10;
		short no = 4;
		String username = "testUser";
		packet.setAck(ack);
		packet.setSub(sub);
		packet.setMessageId(mId);
		packet.setTotal(total);
		packet.setNo(no);
		packet.setUsername(username);
		String text = "textString";
		packet.setText(text);
		System.out.printf("Packet:%s\n", packet);
		
		byte[] data = packet.toByteArray();
		TextPacket result = (TextPacket) PacketFactory.getPacket(ByteBuffer.wrap(data));
		
		Assert.assertEquals(Packet.TEXT, result.getPacketType());
		Assert.assertEquals(ack, result.isAck());
		Assert.assertEquals(sub, result.isSub());
		Assert.assertEquals(mId, result.getMessageId());
		Assert.assertEquals(total, result.getTotal());
		Assert.assertEquals(no, result.getNo());
		Assert.assertEquals(username, result.getUsername());
		Assert.assertEquals(text, result.getText());
		System.out.printf("Result:%s\n", result);
	}
	
}
