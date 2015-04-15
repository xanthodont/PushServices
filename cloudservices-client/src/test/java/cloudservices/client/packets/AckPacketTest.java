package cloudservices.client.packets;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

public class AckPacketTest {
	@Test
	public void parseTest() {
		AckPacket p = new AckPacket();
		p.setUsername("abc");
		p.setAck(false);
		p.setAckId(1);
		p.messageId = 10;
		System.out.printf("%s\n", p);
		byte[] bs = p.toByteArray();
		System.out.printf("%h-%h%h%h%h -- %d\n", bs[0], bs[1], bs[2], bs[3], bs[4], bs.length);
		AckPacket r = new AckPacket(Packet.parse(ByteBuffer.wrap(bs)));
		System.out.printf("%s\n", r);
		//Assert.assertEquals(p.getUsername(), r.getUsername());
		Assert.assertEquals(p.isAck(), r.isAck());
		Assert.assertEquals(p.getPacketType(), r.packetType);
		Assert.assertEquals(p.getAckId(), r.getAckId());
	}
}
