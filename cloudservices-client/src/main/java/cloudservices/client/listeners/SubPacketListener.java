package cloudservices.client.listeners;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cloudservices.client.ClientService;
import cloudservices.client.packets.Packet;
import cloudservices.client.packets.PacketFactory;
import cloudservices.client.packets.PacketListener;

public class SubPacketListener implements PacketListener {
	ClientService client;
	private Map<Integer, SubPacketArray> packetMaps = new ConcurrentHashMap<Integer, SubPacketArray>(); 
	public SubPacketListener(ClientService client) {
		// TODO Auto-generated constructor stub
		this.client = client;
	}
	
	@Override
	public void processPacket(Packet packet) {
		// 组装分段消息
		putInMap(packet);
	}

	private void putInMap(Packet packet) {
		// TODO Auto-generated method stub
		if (!packetMaps.containsKey(packet.getMessageId())) {
			packetMaps.put(packet.getMessageId(), new SubPacketArray(packet.getTotal()));
		}
		SubPacketArray packets = packetMaps.get(packet.getMessageId());
		packets.put(packet.getNo(), packet);
		
		if (packets.size() == packet.getTotal()) { // 分段消息接收完成
			Packet[] subs = packets.getPackets();
			byte[] datas = new byte[packet.getRemainBytes().length * packet.getTotal()]; 
			for (int i = 0; i < subs.length; i++) {
				Packet sub = subs[i];
				System.arraycopy(sub.getRemainBytes(), 0, datas, i*sub.getRemainBytes().length, sub.getRemainBytes().length);
			}
			// 重组完成，将消息投递给reader，重新派发
			ByteBuffer buffer = ByteBuffer.wrap(datas);
			Packet newPacket = PacketFactory.getPacket(buffer);
			client.getPacketReader().putPacket(newPacket);
			// 移除
			packetMaps.remove(packet.getMessageId());
		}
	}

	/**
	 * 分段消息重组辅助类
	 * @author xanthondont
	 *
	 */
	class SubPacketArray {
		int total;
		int size = 0;
		Packet[] packets;
		
		public SubPacketArray(int total) {
			// TODO Auto-generated constructor stub
			this.total = total;
			packets = new Packet[total];
		}
		
		void put(short i, Packet packet) {
			packets[i] = packet;
			size++;
		}
		
		public int size() {
			return size;
		}
		Packet[] getPackets() {
			return packets;
		}
	}
}
