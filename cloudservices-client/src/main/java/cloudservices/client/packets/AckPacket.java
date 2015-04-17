package cloudservices.client.packets;

import java.nio.ByteBuffer;
import java.util.BitSet;

import cloudservices.utils.Bits;
import cloudservices.utils.StringUtil;

/**
 * 回执消息
 * @author xanthodont
 *
 */
public class AckPacket extends Packet {
	private int ackId = -1;
	private String text;
	
	public static AckPacket encode(Packet packet) {
		AckPacket ackPacket = new AckPacket(packet);
		return ackPacket;
	}
	
	public AckPacket() {
		this.packetType = Packet.ACK;
	}
	
	public AckPacket(Packet packet) {
		this.packetType = Packet.ACK;
		this.username = packet.getUsername();
		this.ack = false; // 回执消息不能再设置是否回执的标识位为true，不然会死循环的
		ByteBuffer buffer = ByteBuffer.wrap(packet.getRemainBytes());
		this.ackId = buffer.getInt();
		if (buffer.remaining() > 0) {
			byte[] tx = new byte[buffer.remaining()];
			buffer.get(tx);
			this.text = new String(tx);
		}
	}

	public int getAckId() {
		return ackId;
	}

	public void setAckId(int ackId) {
		this.ackId = ackId;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return String.format("{%s, ackId: %d, text: %s}", super.toString(), this.getAckId(), this.getText());
	}

	@Override
	public byte[] toByteArray() {
		byte[] header = super.toByteArray();
		ByteBuffer buffer = ByteBuffer.allocate(header.length + 4 + text.length());
		buffer.put(header);
		buffer.putInt(ackId);
		if (!StringUtil.isEmpty(text)) buffer.put(text.getBytes());
		return buffer.array();
	}
}
