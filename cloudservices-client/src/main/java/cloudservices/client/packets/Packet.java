package cloudservices.client.packets;

public class Packet {
	
	protected int packetType;
	
	protected short messageId;
	
	protected byte[] bytes;
	
	/*
	public Packet(Packet packet) {
		this.messageId = packet.messageId;
		this.packetType = packet.packetType;
		this.bytes = packet.bytes;
	}*/
	
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	public byte[] getBytes() {
		return bytes;
	}
	
	public void setPacketType(int packetType) {
		this.packetType = packetType;
	}
	public int getPacketType() {
		return packetType;
	}


	public short getMessageId() {
		return messageId;
	}


	public void setMessageId(short messageId) {
		this.messageId = messageId;
	}
	
	public byte[] toByteArray() {
		return null;
	}
}
