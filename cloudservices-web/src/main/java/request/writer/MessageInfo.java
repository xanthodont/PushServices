package request.writer;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import cloudservices.client.packets.AckPacket;
import cloudservices.client.packets.FilePacket;
import cloudservices.client.packets.HttpPacket;
import cloudservices.client.packets.Packet;
import cloudservices.client.packets.TextPacket;

public class MessageInfo extends Packet {
	
	private boolean status;
	private String description;
	
	@Override
	protected byte[] processSubData() {
		// TODO Auto-generated method stub
		return new byte[]{};
	}
	@Override
	protected void subDecode(byte[] remain) {
		// TODO Auto-generated method stub
		ByteBuffer buffer = ByteBuffer.wrap(remain);
		switch(this.getPacketType()) {
		case Packet.TEXT:
			try {
				description = new String(remain, Packet.ENCODING);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case Packet.FILE:
			int totalFile = buffer.getInt();
			description = String.format("文件数：%d, 总大小：%d", totalFile, remain.length);
			break;
		case Packet.HTTP:
			String url = "";
			short length = buffer.getShort();
			if (length > 0) {
				byte[] data = new byte[length];
				buffer.get(data);
				url = new String(data);
			}
			description = String.format("url：%s", url);
			break;
		case Packet.ACK: 
			int ackId = buffer.getInt();
			/*
			if (buffer.remaining() > 0) {
				byte[] tx = new byte[buffer.remaining()];
				buffer.get(tx);
				text = new String(tx);
			}*/
			description = String.format("ackId：%d, length：%d", ackId, buffer.remaining());
			break;
		case Packet.SUB:
			break;
		default:
			break;
		}
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
}
