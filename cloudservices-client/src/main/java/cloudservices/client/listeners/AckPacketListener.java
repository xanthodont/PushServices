package cloudservices.client.listeners;

import cloudservices.client.ClientService;
import cloudservices.client.packets.AckPacket;
import cloudservices.client.packets.Packet;
import cloudservices.client.packets.PacketListener;

public class AckPacketListener implements PacketListener{
	ClientService client;
	public AckPacketListener(ClientService client) {
		// TODO Auto-generated constructor stub
		this.client = client;
	}
	
	@Override
	public void processPacket(Packet packet) {
		// 回发Ack回执消息
		AckPacket ack = new AckPacket();
		ack.setAckId(packet.getMessageId()); // 回发原消息Id
		client.sendPacket(ack, client.getConfiguration().getTopic()+"/"+packet.getUsername());
	}
}
