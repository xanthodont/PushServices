package cloudservices.client.packets;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cloudservices.utils.StringUtil;

public class FilePacket extends Packet{
	private Map<String, File> files = new HashMap<String, File>();
	
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
		for (String filename : files.keySet()) {
			File file = files.get(filename);
			
		}
		return null;
	}

	@Override
	protected void subDecode(byte[] remain) {
		// TODO Auto-generated method stub
		ByteBuffer buffer = ByteBuffer.wrap(remain);
	}
}
