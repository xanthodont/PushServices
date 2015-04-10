package cloudservices.client.mqtt;

import java.net.ProtocolException;
import java.nio.ByteBuffer;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.mqtt.client.Tracer;
import org.fusesource.mqtt.codec.MQTTFrame;
import org.fusesource.mqtt.codec.PUBLISH;

import cloudservices.client.packets.Packet;

public class MqttTracer extends Tracer{
	private MQTTClientService client;

	public MqttTracer(MQTTClientService client) {
		// TODO Auto-generated constructor stub
		this.client = client;
	}
	
	@Override
	public void onReceive(MQTTFrame frame) {
		if (frame.messageType() == PUBLISH.TYPE) {
			try {
				PUBLISH publish = new PUBLISH().decode(frame);
				//System.out.println(Thread.currentThread().getName() + "recv Publish: " + publish);
				Packet packet = parsePacketByPublish(publish);
				client.getClientService().getPacketReader().putPacket(packet);
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			//System.out.println("recv Publish: " + frame);
			
		}
		else {}
		//System.out.println(Thread.currentThread().getName() + "recv: " + frame);
	}

	private Packet parsePacketByPublish(PUBLISH publish) {
		// TODO Auto-generated method stub
		ByteBuffer buffer = publish.payload().toByteBuffer();
		Packet packet = Packet.parse(buffer);
		return packet;
	}

	@Override
	public void onSend(MQTTFrame frame) {
		System.out.println(Thread.currentThread().getName() + "send: " + frame);
	}

	@Override
	public void debug(String message, Object... args) {
		System.out.println(String.format("debug: " + message, args));
	}
}
