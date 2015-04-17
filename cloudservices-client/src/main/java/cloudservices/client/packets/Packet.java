package cloudservices.client.packets;

import java.nio.ByteBuffer;

/**
 * 自定义消息基类
 * @author xanthodont
 *
 */
public abstract class Packet {
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
	
	/** 是否是分段消息 */
	protected boolean sub;
	/** 消息总数 */
	protected short total = 1;
	/** 分段序号 */
	protected short no = 1;
	/** 是否需要回执 */
	protected boolean ack;
	/** 发送消息的用户 */
	protected String username;
	
	/** 发送的主题 */
	protected String public2Topic;
	/** 消息Id */
	protected int messageId;
	/** 除去自定义消息头之后，剩余的字节数组 */
	protected byte[] remainBytes;
	
	static int nextMessageId = 1;
    private synchronized static int getNextMessageId() {
        return nextMessageId++;
    }
	public Packet() {
		//this.messageId = getNextMessageId();
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

	void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public boolean isAck() {
		return ack;
	}

	public void setAck(boolean ack) {
		this.ack = ack;
	}

	public boolean isSub() {
		return sub;
	}
	public void setSub(boolean sub) {
		this.sub = sub;
	}
	public short getTotal() {
		return total;
	}
	public void setTotal(short total) {
		this.total = total;
	}
	public short getNo() {
		return no;
	}
	public void setNo(short no) {
		this.no = no;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	

	/**
	 * 消息编码
	 * @return
	 */
	public byte[] toByteArray() {
		// 子类处理消息
		byte[] data = processSubData();
		ByteBuffer buffer = ByteBuffer.allocate(9 + 2 + getUsername().length() + data.length);
		buffer.put(getHeader());
		buffer.putInt(getMessageId());
		buffer.putShort(getTotal());
		buffer.putShort(no);
		putString(buffer, getUsername());
		buffer.put(data);
		return buffer.array();
	}
	
	/**
	 * 子类编码自己的数据
	 */
	protected abstract byte[] processSubData();
	
	/**
	 * 消息解码
	 * @param buffer
	 */
	public void decode(ByteBuffer buffer) {
		// 设置消息的基本信息
		byte header = buffer.get();
		int type = header >> 4;
		
		boolean ack = (header & 0x01) == 0x01;
		boolean sub = (header & 0x02) == 0x02;
		
		this.setPacketType(type);
		this.setAck(ack);
		this.setSub(sub);
		this.setMessageId(buffer.getInt());
		this.total = buffer.getShort();
		this.no = buffer.getShort();
		
		this.setUsername(getString(buffer));
		byte[] remain = new byte[buffer.remaining()];
		buffer.get(remain);
		this.setRemainBytes(remain);
		/** 子类解码自己的数据 */
		subDecode(remain);
	}
	/** 
	 * 子类解码自己的数据 
	 */
	protected abstract void subDecode(byte[] remain);
	
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
		if (isAck()) b = (byte) (b | 0x01);  // 设置回执标识
		if (isSub()) b = (byte) (b | 0x02);  // 设置分段标识
		return b;
	}
	
	/** 
	 * 往buffer中输入str字符串
	 * 	先插入串长度，再插入串内容
	 * @param buffer
	 * @param str
	 */
	protected void putString(ByteBuffer buffer, String str) {
		buffer.putShort((short) str.length());
		buffer.put(str.getBytes());
	}
	protected String getString(ByteBuffer buffer) {
		short length = buffer.getShort();
		byte[] data = new byte[length];
		buffer.get(data);
		return new String(data);
	}
	
	@Override 
	public String toString() {
		return String.format("type: %s, isAck: %b, user: %s, topic: %s", decodeType(packetType), isAck(), getUsername(), getPublic2Topic()); 
	}
	
	/**
	 * 消息分段
	 * @param packet
	 * @param size
	 * @return
	 */
	public static Packet[] subsection(Packet packet, int size) {
		//npacket 
		Packet[] packets = new Packet[2];
		return packets;
	}
}
