package cloudservices.client.packets;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;

public class PacketFactory {

	public static Packet getPacket(ByteBuffer buffer) {
		// TODO Auto-generated method stub
		buffer.mark();
		byte header = buffer.get();
		int type = header >> 4;
		Packet packet = null;
		switch (type) {
			case Packet.TEXT:
				packet = new TextPacket();
				break;
			case Packet.FILE:
				packet = new FilePacket();
				break;
			case Packet.HTTP:
				packet = new HttpPacket();
				break;
			case Packet.ACK: 
				packet = new AckPacket();
				break;
			case Packet.SUB:
				packet = new SubPacket();
				break;
			default: 
				packet = new Packet(){
					@Override
					protected byte[] processSubData() {
						// TODO Auto-generated method stub
						return this.getRemainBytes();
					}

					@Override
					protected void subDecode(byte[] remain) {
						// TODO Auto-generated method stub
						
					}};
				break;
		}
		buffer.reset();
		packet.decode(buffer);
		
		return packet;
	}
	
	
	public static Packet getParentPacket(ByteBuffer buffer) {
		// TODO Auto-generated method stub
		Packet packet = new Packet(){
			@Override
			protected byte[] processSubData() {
				// TODO Auto-generated method stub
				return this.getRemainBytes();
			}
 
			@Override
			protected void subDecode(byte[] remain) {
				// TODO Auto-generated method stub
				
			}};
		packet.decode(buffer);
		
		return packet;
	}
}
