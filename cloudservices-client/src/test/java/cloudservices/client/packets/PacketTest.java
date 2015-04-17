package cloudservices.client.packets;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PacketTest {
	private Packet packet;
	
	@Before
	public void init() {
		packet = new Packet() {
			@Override
			protected void subDecode(byte[] remain) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			protected byte[] processSubData() {
				// TODO Auto-generated method stub
				return new byte[]{};
			}
		};
	}
	
	@Test 
	public void get2putStringTest() {
		String testString = "test";
		ByteBuffer buffer = ByteBuffer.allocate(2+testString.length());
		packet.putString(buffer, testString);
		buffer.flip();
		String result = packet.getString(buffer);
		Assert.assertEquals(testString, result);
	}
	
	@Test
	public void getHeaderTest() {
		packet.setPacketType(Packet.TEXT);
		packet.setSub(true);
		packet.setAck(true);
		byte h1 = packet.getHeader();
		Assert.assertEquals(0x13, h1);
		
		packet.setPacketType(Packet.HTTP);
		packet.setSub(true);
		packet.setAck(false);
		byte h2 = packet.getHeader();
		Assert.assertEquals(0x22, h2);
		
		packet.setPacketType(Packet.ACK);
		packet.setSub(false);
		packet.setAck(true);
		byte h3 = packet.getHeader();
		Assert.assertEquals(0x31, h3);
		
		packet.setPacketType(Packet.FILE);
		packet.setSub(false);
		packet.setAck(false);
		byte h4 = packet.getHeader();
		Assert.assertEquals(0x40, h4);
	}
	
	
	@Test 
	public void toByteArrayTest() {
		int type = 0;
		boolean ack = false;
		boolean sub = false;
		int mId = 10;
		short total = 10;
		short no = 4;
		String username = "testUser";
		packet.setPacketType(type);
		packet.setAck(ack);
		packet.setSub(sub);
		packet.setMessageId(mId);
		packet.setTotal(total);
		packet.setNo(no);
		packet.setUsername(username);
		System.out.printf("Packet:%s\n", packet);
		
		byte[] data = packet.toByteArray();
		Packet result = PacketFactory.getPacket(ByteBuffer.wrap(data));
		
		Assert.assertEquals(type, result.getPacketType());
		Assert.assertEquals(ack, result.isAck());
		Assert.assertEquals(sub, result.isSub());
		Assert.assertEquals(mId, result.getMessageId());
		Assert.assertEquals(total, result.getTotal());
		Assert.assertEquals(no, result.getNo());
		Assert.assertEquals(username, result.getUsername());
		System.out.printf("Result:%s\n", result);
	}
}
