package cloudservices.client.packets;

import java.nio.ByteBuffer;

public class Packet {
	public static final int TEXT = 1;
	public static final int HTTP = 2;
	public static final int ACK  = 3;
	public static final int FILE = 4; 
	/**
	 * 消息类型：<br/>
	 * 1 -- 文本消息<br/>
	 * 2 -- Http消息<br/>
	 * 3 -- 回执消息<br/>
	 * 4 -- 文件消息<br/>
	 */
	protected int packetType;
	
	/** 是否需要回执 */
	protected boolean ack;
	/** 发送消息的用户 */
	protected String username;
	
	/** 发送的主题 */
	protected String public2Topic;
	
	protected int messageId;
	
	protected byte[] remainBytes;
	
	static int nextMessageId = 1;
    private synchronized static int getNextMessageId() {
        return nextMessageId++;
    }
	public Packet() {
		//this.messageId = getNextMessageId();
	}
	
	public static Packet parse(ByteBuffer buffer) {
		Packet packet = new Packet();
		byte header = buffer.get();
		int type = header >> 4;
		boolean ack = (header & 0x01) == 0x01;
		packet.setPacketType(type);
		packet.setAck(ack);
		if (ack) { // 消息需要回执, 读取发送消息的用户信息
			short s = buffer.getShort();
			byte[] fu = new byte[s]; 
			buffer.get(fu);
			packet.setUsername(new String(fu));
			packet.setMessageId(buffer.getInt());
		}
		byte[] remain = new byte[buffer.remaining()];
		buffer.get(remain);
		packet.setRemainBytes(remain);
		return packet;
	}

	public byte[] getRemainBytes() {
		return remainBytes;
	}

	public void setRemainBytes(byte[] remainBytes) {
		this.remainBytes = remainBytes;
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

	public int getMessageId() {
		if (messageId < 1) {
			messageId = getNextMessageId();
		} 
		return messageId;
	}

	private void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public boolean isAck() {
		return ack;
	}

	public void setAck(boolean ack) {
		this.ack = ack;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public byte[] toByteArray() {
		if (isAck()) {
			ByteBuffer header = ByteBuffer.allocate(7 + getUsername().length());
			header.put(getHeader());
			header.putShort((short)getUsername().length());
			header.put(getUsername().getBytes());
			header.putInt(getMessageId());
			return header.array();
		} else {
			ByteBuffer header = ByteBuffer.allocate(1);
			header.put(getHeader());
			return header.array();
		}
	}
	
	private String decodeType(int packetType) {
		switch (packetType) {
		case Packet.TEXT:
			return "TEXT"; 
		case Packet.FILE:
			return "FILE";
		case Packet.HTTP:
			return "HTTP";
		case Packet.ACK: 
			return "ACK";
		default: 
			return "UNDEFINED";
		}
	
	}
	
	protected byte getHeader() {
		byte b = (byte) (packetType << 4);
		if (isAck()) b = (byte) (b | 0x01);
		return b;
	}
	
	@Override 
	public String toString() {
		return String.format("type: %s, isAck: %b, user: %s", decodeType(packetType), isAck(), getUsername()); 
	}
}
