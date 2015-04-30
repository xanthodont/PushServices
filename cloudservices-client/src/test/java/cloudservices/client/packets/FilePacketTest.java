package cloudservices.client.packets;

import java.io.File;
import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cloudservices.client.ClientConfiguration;
import cloudservices.client.ClientService;
import cloudservices.client.ConfigException;
import cloudservices.client.ConnectException;
import cloudservices.client.TestBase;
import cloudservices.client.packets.filters.PacketAckFilter;

public class FilePacketTest extends TestBase {
	static String[] filepaths = {"D://t.txt", "D://t-1.txt", "D://t-2.txt", "D://t-3.txt", "D://t-4.txt"};

	public static void main(String[] args) throws ConfigException, ConnectException {
		// TODO Auto-generated method stub
		
		ClientConfiguration sendConfig = getInitConfig();
		sendConfig.setUsername("File");
		sendConfig.setConnectType(1);
		sendConfig.setBufferSize(2000); // 测试 -- 这个大小关系着在发送大文件数据时
		
		ClientService client = ClientService.getInstance();
		client.config(sendConfig);
		client.startup();
		client.connect();
		
		FilePacket filePacket = new FilePacket();
		filePacket.setAck(true);
		for (int i = 0; i < filepaths.length; i++) {
			String filename = filepaths[i];
			File file = new File(filename);
			filePacket.addFile(file.getName(), file);
		}
		System.out.printf("begin send Time:%d\n", System.currentTimeMillis());
		client.sendPacket(filePacket, "beidou/MR");
		
		PacketCollector collector = client.createPacketCollector(new PacketAckFilter(filePacket.getMessageId()));
		Packet r = collector.nextResult(20000); // 等待超时时间设置为10秒
		collector.cancel();
		// do your job
		if (r != null) { 
			AckPacket ack = (AckPacket)r;
			System.out.printf("ack=%s\n", ack);
		}
		System.out.printf("send to writer Time:%d\n", System.currentTimeMillis());
		
	}
	
	private FilePacket packet;
	@Before
	public void init() {
		packet = new FilePacket();
	}
	

	@Test
	public void toByteArrayTest() {
		boolean ack = false;
		boolean sub = false;
		int mId = 10;
		String username = "testUser";
		packet.setAck(ack);
		packet.setSub(sub);
		packet.setMessageId(mId);
		packet.setUsername(username);
		for (int i = 0; i < filepaths.length; i++) {
			String filename = filepaths[i];
			File file = new File(filename);
			packet.addFile(file.getName(), file);
		}
		System.out.printf("Packet:%s\n", packet);
		
		byte[] data = packet.toByteArray();
		FilePacket result = (FilePacket) PacketFactory.getPacket(ByteBuffer.wrap(data));
		
		Assert.assertEquals(Packet.FILE, result.getPacketType());
		Assert.assertEquals(ack, result.isAck());
		Assert.assertEquals(sub, result.isSub());
		Assert.assertEquals(mId, result.getMessageId());
		Assert.assertEquals(username, result.getUsername());
		System.out.printf("Result:%s\n", result);
	}
	
}
