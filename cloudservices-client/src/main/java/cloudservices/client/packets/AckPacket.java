package cloudservices.client.packets;

import java.nio.ByteBuffer;
import java.util.BitSet;

import cloudservices.utils.Bits;

/**
 * 回执消息
 * @author xanthodont
 *
 */
public class AckPacket extends Packet {
	private int ackId = -1;
	
	public static AckPacket encode(Packet packet) {
		AckPacket ackPacket = new AckPacket(packet);
		return ackPacket;
	}
	
	public AckPacket() {
		this.packetType = Packet.ACK;
	}
	
	public AckPacket(Packet packet) {
		this.packetType = Packet.TEXT;
		this.username = packet.getUsername();
		this.ack = false; // 回执消息不能再设置是否回执的标识位为true，不然会死循环的
		if (this.remainBytes.length != 4) { // 验证回执消息内容是否正确
			this.ackId = Bits.getInt(remainBytes, 0, true); // 大端字节序 
		}
	}

	public int getAckId() {
		return ackId;
	}

	public void setAckId(int ackId) {
		this.ackId = ackId;
	}
	
	@Override
	public String toString() {
		return String.format("{%s, ackId: %d}", super.toString(), this.getAckId());
	}

	@Override
	public byte[] toByteArray() {
		byte[] header = super.toByteArray();
		ByteBuffer buffer = ByteBuffer.allocate(header.length + 4);
		buffer.put(header);
		buffer.putInt(ackId);
		return buffer.array();
	}
}
