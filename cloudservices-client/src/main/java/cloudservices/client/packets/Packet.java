package cloudservices.client.packets;

import java.nio.ByteBuffer;

public class Packet {
	
	protected int packetType;
	
	protected String public2Topic;
	
	protected short messageId;
	
	protected byte[] bytes;
	
	/*
	public Packet(Packet packet) {
		this.messageId = packet.messageId;
		this.packetType = packet.packetType;
		this.bytes = packet.bytes;
	}*/
	
	public static Packet parse(ByteBuffer buffer) {
		Packet packet = new Packet();
		int type = buffer.getInt();
		packet.setPacketType(type);
		byte[] remain = new byte[buffer.remaining()];
		buffer.get(remain);
		packet.setBytes(remain);
		return packet;
	}
	
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


	public String getPublic2Topic() {
		return public2Topic;
	}

	public void setPublic2Topic(String public2Topic) {
		this.public2Topic = public2Topic;
	}

	public short getMessageId() {
		return messageId;
	}


	public void setMessageId(short messageId) {
		this.messageId = messageId;
	}
	
	public byte[] toByteArray() {
		return new byte[] {};
	}
	
	private String decodeType(int packetType) {
		switch (packetType) {
		case 0:
			return "TEXT"; 
		case 1:
			return "FILE";
		case 2:
			return "HTTP";
		case 3: 
			return "ACK";
		default: 
			return "UNDEFINED";
		}
	
	}
	
	@Override 
	public String toString() {
		return String.format("type: %s, content: %s", packetType, new String(bytes)).toString(); 
	}
}
