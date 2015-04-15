package cloudservices.client.packets;

import java.nio.ByteBuffer;

public class TextPacket extends Packet {
	private String text;
	
	public static TextPacket encode(Packet packet) {
		TextPacket textPacket = new TextPacket(packet);
		textPacket.text = new String(packet.remainBytes);
		return textPacket;
	}
	
	public TextPacket() {
		this.packetType = Packet.TEXT;
	}
	
	public TextPacket(Packet packet) {
		// TODO Auto-generated constructor stub
		this.packetType = Packet.TEXT;
		this.username = packet.getUsername();
		this.messageId = packet.messageId;
		this.ack = packet.ack;
		this.text = new String(packet.getRemainBytes()); 
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return String.format("{%s, mId: %s, text: %s}", super.toString(), this.getMessageId(), this.getText());
	}

	@Override
	public byte[] toByteArray() {
		byte[] header = super.toByteArray();
		ByteBuffer buffer = ByteBuffer.allocate(header.length + text.getBytes().length);
		buffer.put(header);
		buffer.put(text.getBytes());
		return buffer.array();
	}
	
	
}
