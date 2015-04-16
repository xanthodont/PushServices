package cloudservices.client.packets.filters;

import cloudservices.client.packets.AckPacket;
import cloudservices.client.packets.Packet;
import cloudservices.client.packets.PacketFilter;

public class PacketAckFilter implements PacketFilter {
	private int messageId;

	public PacketAckFilter(int messageId) {
		// TODO Auto-generated constructor stub
		this.messageId = messageId;
	}

	@Override
	public boolean accept(Packet packet) {
		// TODO Auto-generated method stub
		if (packet.getPacketType() != Packet.ACK) return false;
		AckPacket ack = AckPacket.encode(packet);
		return messageId == ack.getAckId();
	}

}
