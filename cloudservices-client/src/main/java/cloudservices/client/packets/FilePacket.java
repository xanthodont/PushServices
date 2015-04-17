package cloudservices.client.packets;

import java.io.File;
import java.nio.ByteBuffer;

import cloudservices.utils.StringUtil;

public class FilePacket extends Packet{
	private File file;
	
	public FilePacket() {
		this.packetType = Packet.FILE;
	}
	
	
	@Override
	public String toString() {
		return String.format("{%s, remain: %d}", super.toString(), this.getRemainBytes().length);
	}

	@Override
	protected byte[] processSubData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void subDecode(byte[] remain) {
		// TODO Auto-generated method stub
		ByteBuffer buffer = ByteBuffer.wrap(remain);
	}
}
