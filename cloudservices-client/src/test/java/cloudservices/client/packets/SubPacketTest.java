package cloudservices.client.packets;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cloudservices.client.ClientConfiguration;
import cloudservices.client.ClientService;
import cloudservices.client.ConfigException;
import cloudservices.client.ConnectException;
import cloudservices.client.TestBase;

public class SubPacketTest extends TestBase {
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ConfigException, ConnectException {
		ClientConfiguration sendConfig = getInitConfig();
		sendConfig.setUsername("SubSend");
		sendConfig.setConnectType(2);
		sendConfig.setBufferSize(1000); // 测试
		
		ClientService sendClient = ClientService.getInstance();
		sendClient.config(sendConfig);
		sendClient.startup();
		sendClient.connect();
		
		
		int i = 0;
		while (true) {
			// client.sendPacket(new Packet());
			try {
				i++;
				Packet p = new Packet() {
					private byte[] datas = new byte[10000];
					@Override
					protected void subDecode(byte[] remain) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					protected byte[] processSubData() {
						// TODO Auto-generated method stub
						datas[1001] = 0x01;
						datas[2001] = 0x02;
						datas[3001] = 0x03;
						datas[4001] = 0x04;
						datas[5001] = 0x05;
						datas[6001] = 0x06;
						datas[7001] = 0x07;
						datas[8001] = 0x08;
						datas[9001] = 0x09;
						return datas;
					}
				};
				sendClient.sendPacket(p, "beidou/MR");
				Thread.sleep(60000);
				break;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private SubPacket packet;
	@Before
	public void init() {
		FilePacket filePacket = new FilePacket();
		
		packet = new SubPacket(filePacket);
		
	}
	
	//@Test 
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
		int ackId = 111;
		
		String returnText = "return text";
		
		System.out.printf("Packet:%s\n", packet);
		
		byte[] data = packet.toByteArray();
		AckPacket result = (AckPacket) PacketFactory.getPacket(ByteBuffer.wrap(data));
		
		Assert.assertEquals(Packet.ACK, result.getPacketType());
		Assert.assertEquals(ack, result.isAck());
		Assert.assertEquals(sub, result.isSub());
		Assert.assertEquals(mId, result.getMessageId());
		Assert.assertEquals(total, result.getTotal());
		Assert.assertEquals(no, result.getNo());
		Assert.assertEquals(username, result.getUsername());
		Assert.assertEquals(ackId, result.getAckId());
		Assert.assertEquals(returnText, result.getText());
		System.out.printf("Result:%s\n", result);
	}
	
	@Test
	public void listTest() {
		
	}
}
