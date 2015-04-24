package cloudservices.client.packets;

import java.io.File;
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

	public static void main(String[] args) throws ConfigException, ConnectException {
		// TODO Auto-generated method stub
		ClientConfiguration config = getInitConfig();
		config.setUsername("TextSend");
		config.setConnectType(ClientConfiguration.SHORT_HTTP);
		//config.setConnectType(ClientConfiguration.LONG_MQTT);
		config.setHttpCircle(2);
		//sendConfig.setBufferSize(1000); // 测试
		
		ClientService client = ClientService.getInstance();
		client.config(config);
		client.startup();
		client.connect();

		int i = 0;
		while (true) {
			// client.sendPacket(new Packet());
			try {
				i++;
				
				TextPacket t = new TextPacket();
				t.setAck((i % 2) == 0);
				t.setText(String.format("中文--%d--", i));
				client.sendPacket(t, "beidou/MR");
				Thread.sleep(10000);
				//break;
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
