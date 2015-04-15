package cloudservices.client.packets;

import java.nio.ByteBuffer;
import org.junit.Assert;
import org.junit.Test;

public class PacketTest {
	@Test
	public void getHeaderTest() {
		Packet p = new Packet();
		p.setPacketType(Packet.TEXT);
		p.setAck(true);
		byte b = p.getHeader();
		Assert.assertEquals(0x11, b);
	}
	
	@Test
	public void parseTest() {
		ByteBuffer buffer = ByteBuffer.allocate(11);
		buffer.put((byte) 0x11);
		buffer.putShort((short) 4);
		buffer.put("test".getBytes());
		buffer.putInt(10);
		buffer.flip();
		Packet p = Packet.parse(buffer);
		Assert.assertEquals(true, p.isAck());
		Assert.assertEquals(Packet.TEXT, p.getPacketType());
		Assert.assertEquals(10, p.getMessageId());
		System.out.println(p);
	}
	
	@Test 
	public void toByteArrayTest() {
		Packet p = new Packet();
		p.setUsername("abc");
		p.setAck(true);
		p.setPacketType(1);
		p.messageId = 10;
		byte[] bs = p.toByteArray();
		Packet r = Packet.parse(ByteBuffer.wrap(bs));
		System.out.printf("%s", r);
		Assert.assertEquals(p.getUsername(), r.getUsername());
		Assert.assertEquals(p.isAck(), r.isAck());
		Assert.assertEquals(p.getPacketType(), r.packetType);
		Assert.assertEquals(p.getMessageId(), r.getMessageId());
	}
}
