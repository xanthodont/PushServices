package cloudservices.client.packets;

import java.nio.ByteBuffer;

public class TextPacket extends Packet {
	private String text;
	
	public TextPacket() {
		this.packetType = Packet.TEXT;
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
	protected byte[] processSubData() {
		// TODO Auto-generated method stub
		ByteBuffer buffer = ByteBuffer.allocate(text.getBytes().length);
		buffer.put(text.getBytes());
		return buffer.array();
	}

	@Override
	protected void subDecode(byte[] remain) {
		// TODO Auto-generated method stub
		this.text = new String(remain);
	}
}
