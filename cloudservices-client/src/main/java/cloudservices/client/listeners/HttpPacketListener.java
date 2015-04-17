package cloudservices.client.listeners;

import java.io.IOException;
import java.net.URL;

import cloudservices.client.ClientService;
import cloudservices.client.http.async.AsyncHttpConnection;
import cloudservices.client.http.async.StringResponseHandler;
import cloudservices.client.http.async.support.ParamsWrapper;
import cloudservices.client.http.async.support.RequestInvoker.HttpMethod;
import cloudservices.client.packets.AckPacket;
import cloudservices.client.packets.HttpPacket;
import cloudservices.client.packets.Packet;
import cloudservices.client.packets.PacketListener;

public class HttpPacketListener implements PacketListener{
	ClientService client;
	public HttpPacketListener(ClientService client) {
		// TODO Auto-generated constructor stub
		this.client = client;
	}

	@Override
	public void processPacket(final Packet packet) {
		// 执行需要代理的Http请求
		AsyncHttpConnection http = AsyncHttpConnection.getInstance();
		HttpPacket httpPacket = (HttpPacket)packet;
		HttpResponseHandler httpResponseHandler = new HttpResponseHandler(httpPacket);
		if (httpPacket.getMethod() != HttpMethod.GET)
			http.post(httpPacket.getUrl(), httpPacket.getParamsString(), httpResponseHandler);
		else
			http.get(httpPacket.getUrl(), httpPacket.getParamsString(), httpResponseHandler);
	}

	private class HttpResponseHandler extends StringResponseHandler {
		private HttpPacket packet;
		
		public HttpResponseHandler(HttpPacket packet) {
			// TODO Auto-generated constructor stub
			this.packet = packet;
		}
		
		@Override
		public void onSubmit(URL url, ParamsWrapper params) {}
		
		@Override
		public void onStreamError(IOException exp) {
			AckPacket ack = new AckPacket();
			ack.setAckId(packet.getMessageId());
			ack.setText("{code: '-1', msg: 'stream error'}");
			client.sendPacket(ack, client.getConfiguration().getTopic() + "/" + packet.getUsername());
		}
		
		@Override
		public void onConnectError(IOException exp) {
			AckPacket ack = new AckPacket();
			ack.setAckId(packet.getMessageId());
			ack.setText("{code: '-2', msg: 'connect error'}");
			client.sendPacket(ack, client.getConfiguration().getTopic() + "/" + packet.getUsername());
		}
		
		@Override
		protected void onResponse(String content, URL url) {
			// 将请求的结果回发给客户端
			AckPacket ack = new AckPacket();
			ack.setAckId(packet.getMessageId());
			ack.setText(content);
			client.sendPacket(ack, client.getConfiguration().getTopic() + "/" + packet.getUsername());
		}
	}
}
