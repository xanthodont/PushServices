package cloudservices.client.packets;

/**
 * 分段消息
 * @author xanthondont
 *
 */
public class SubPacket extends Packet{

	public SubPacket(Packet packet) {
		// TODO Auto-generated constructor stub
		super(packet);
		this.packetType = Packet.SUB;
		this.setSub(true);
	}

	@Override
	protected byte[] processSubData() {
		// TODO Auto-generated method stub
		return this.remainBytes;
	}

	@Override
	protected void subDecode(byte[] remain) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		return String.format("{%s, isSub: %b, totla: %d, no: %d}", super.toString(), this.isSub(), this.getTotal(), this.getNo());
	}
}
