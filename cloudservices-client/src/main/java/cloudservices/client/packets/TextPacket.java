package cloudservices.client.packets;

import java.nio.ByteBuffer;

public class TextPacket extends Packet {
	private String text;
	
	public TextPacket encode(Packet packet) {
		TextPacket textPacket = new TextPacket(packet);
		this.text = new String(packet.bytes);
		return textPacket;
	}
	
	public TextPacket() {
		this.packetType = 0;
	}
	
	public TextPacket(Packet packet) {
		// TODO Auto-generated constructor stub
		this.packetType = 0;
		this.messageId = packet.messageId;
		this.text = new String(packet.getBytes()); 
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return String.format("messageId:%d text:%s", this.getMessageId(), this.getText());
	}

	@Override
	public byte[] toByteArray() {
		ByteBuffer buffer = ByteBuffer.allocate(4+text.getBytes().length);
		buffer.putInt(packetType);
		buffer.put(text.getBytes());
		return buffer.array();
	}
}
