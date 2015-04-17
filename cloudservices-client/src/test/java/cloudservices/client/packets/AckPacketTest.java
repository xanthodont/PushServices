package cloudservices.client.packets;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cloudservices.client.http.async.support.ParamsWrapper;

public class AckPacketTest {
	private AckPacket packet;
	@Before
	public void init() {
		packet = new AckPacket();
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
		int ackId = 111;
		packet.setAckId(ackId);
		String returnText = "return text";
		packet.setText(returnText);
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
}
