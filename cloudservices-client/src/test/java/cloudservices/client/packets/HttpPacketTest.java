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
import cloudservices.client.http.async.support.ParamsWrapper;
import cloudservices.client.http.async.support.RequestInvoker.HttpMethod;

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
		config.setBufferSize(2000);
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
				t.setUrl("http://www.baidu.com");
				t.setMethod(HttpMethod.POST);
				ParamsWrapper params = new ParamsWrapper();
				params.put("imei", "351372098135419");
				params.put("sn", "15811375356");
				t.setParams(params);
				client.sendPacket(t, "beidou/MR");
				Thread.sleep(10000);
				break;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private HttpPacket packet;
	@Before
	public void init() {
		packet = new HttpPacket();
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
		String url = "http://www.baidu.com";
		ParamsWrapper params = new ParamsWrapper();
		params.put("tt", "test");
		packet.setUrl(url);
		packet.setParams(params);
		System.out.printf("Packet:%s\n", packet);
		
		byte[] data = packet.toByteArray();
		HttpPacket result = (HttpPacket) PacketFactory.getPacket(ByteBuffer.wrap(data));
		
		Assert.assertEquals(Packet.HTTP, result.getPacketType());
		Assert.assertEquals(ack, result.isAck());
		Assert.assertEquals(sub, result.isSub());
		Assert.assertEquals(mId, result.getMessageId());
		Assert.assertEquals(total, result.getTotal());
		Assert.assertEquals(no, result.getNo());
		Assert.assertEquals(username, result.getUsername());
		Assert.assertEquals(url, result.getUrl());
		Assert.assertEquals(params.toString(), result.getParamsString());
		System.out.printf("Result:%s\n", result);
	}

}
