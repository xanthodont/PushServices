package cloudservices.client;


import cloudservices.client.packets.AckPacket;
import cloudservices.client.packets.Packet;
import cloudservices.client.packets.PacketCollector;
import cloudservices.client.packets.TextPacket;
import cloudservices.client.packets.filters.PacketAckFilter;

/**
 * 消息回执测试
 * @author xanthodont
 *
 */
public class AckListenerTest extends TestBase {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientConfiguration config = new ClientConfiguration(SERVER_IP,
				MQTT_PORT);
		config.setUsername("send");
		config.setPassword(DEFAULT_PASSWORD);
		config.setTopic(TOPIC);
		config.setSendUrl(SEND_URL);
		config.setReceiveUrl(RECEIVE_URL);
		config.setConnectUrl(CONNECT_URL);
		config.setConnectType(2);
		config.setHttpCircle(10);

		ClientService client = ClientService.getInstance();
		try {
			client.config(config);
			client.startup();
			client.connect();
		} catch (ConfigException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
			System.out.println(e1.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		int i = 0;
		while (true) {
			// client.sendPacket(new Packet());
			try {
				TextPacket t = new TextPacket();
				t.setText(String.format("to_R %s -- %d", config.getUsername(), i++));
				t.setAck(true);
				PacketCollector collector = client.createPacketCollector(new PacketAckFilter(t.getMessageId()));
				client.sendPacket(t, "beidou/R");
				Packet r = collector.nextResult(20000); // 等待超时时间设置为10秒
				collector.cancel();
				// do your job
				if (r != null) { 
					AckPacket ack = (AckPacket)r;
					System.out.printf("ack=%s\n", ack);
				}
				
				Thread.sleep(1000);
				//break;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
