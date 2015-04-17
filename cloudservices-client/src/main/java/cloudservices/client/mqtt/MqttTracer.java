package cloudservices.client.mqtt;

import java.net.ProtocolException;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.mqtt.client.Tracer;
import org.fusesource.mqtt.codec.MQTTFrame;
import org.fusesource.mqtt.codec.PUBLISH;

import cloudservices.client.packets.Packet;
import cloudservices.client.packets.PacketFactory;

public class MqttTracer extends Tracer{
	private static Logger logger = Logger.getLogger(MqttTracer.class);
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
				ByteBuffer buffer = publish.payload().toByteBuffer();
				Packet packet = PacketFactory.getPacket(buffer);
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

	@Override
	public void onSend(MQTTFrame frame) {
		if (frame.messageType() == PUBLISH.TYPE) {
			try {
				PUBLISH publish = new PUBLISH().decode(frame);
				//logger.info("send: -- mesageId:" + publish.messageId());
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			//logger.info("send: " + frame);
		}
	}

	@Override
	public void debug(String message, Object... args) {
		System.out.println(String.format("debug: " + message, args));
	}
}
