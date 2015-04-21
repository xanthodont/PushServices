package cloudservices.client.packets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cloudservices.utils.FileUtil;
import cloudservices.utils.StringUtil;

public class FilePacket extends Packet{
	/** 接收文件的存储路径 */
	private String filePath = "D://tmp//";
	private Map<String, File> files = new HashMap<String, File>();
	
	public FilePacket() {
		this.packetType = Packet.FILE;
	}
	
	
	@Override
	public String toString() {
		return String.format("{%s, length: %d}", super.toString(), this.toByteArray().length);
	}

	@Override
	protected byte[] processSubData() {
		// TODO Auto-generated method stub
		int totalSize = 4;
		int totalFile = files.size();
		ByteBuffer buffer = ByteBuffer.allocate(totalSize);
		buffer.putInt(totalFile);
		for (String filename : files.keySet()) {
			File file = files.get(filename);
			int length = (int) file.length();
			ByteBuffer oldBuffer = buffer;
			
			byte[] filenameDatas = encodingString(filename);
			totalSize += 2 + filenameDatas.length + 4 + length; // 文件名 + 文件大小
			buffer = ByteBuffer.allocate(totalSize);
			buffer.put(oldBuffer.array());
			buffer.putShort((short) filenameDatas.length);
			buffer.put(filenameDatas);
			
			byte[] fileDatas = new byte[length];
			try {
				FileInputStream in = new FileInputStream(file);
				in.read(fileDatas);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			buffer.putInt(length); 
			buffer.put(fileDatas);
		}
		
		return buffer.array();
	}

	@Override
	protected void subDecode(byte[] remain) {
		// TODO Auto-generated method stub
		ByteBuffer buffer = ByteBuffer.wrap(remain);
		int totalFile = buffer.getInt();
		for (int i = 0; i < totalFile; i++) {
			String filename = getString(buffer);
			int length = buffer.getInt();
			byte[] fileDatas = new byte[length];
			buffer.get(fileDatas);
			File saveFile = new File(getFilePath() + String.format("%d_%s", getMessageId(), filename));
			try {
				FileOutputStream out = new FileOutputStream(saveFile);
				out.write(fileDatas);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	public String getFilePath() {
		if (!StringUtil.isEmpty(filePath))
			FileUtil.mkdirs(filePath);
		return filePath;
	}


	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	public Map<String, File> getFiles() {
		return files;
	}


	public void addFile(String filename, File file) {
		// TODO Auto-generated method stub
		files.put(filename, file);
	}
	
}
