package cloudservices.client.packets;

import java.nio.ByteBuffer;

import cloudservices.client.http.async.support.ParamsWrapper;
import cloudservices.client.http.async.support.RequestInvoker.HttpMethod;
import cloudservices.utils.Bits;

/**
 * Http代理消息
 * 由于网络原因，服务器和客户端只能通过一个端口进行通信，而此时服务器这边有web应用，web一般需要占用80端口，然后长连接还需要一个通信端口，如1883
 * 这种情况就只能让客户通过长连接端口，将Http请求发到服务端这边，然后服务端这边完成代理请求，然后将结果返回给客户端。
 * @author xanthodont
 *
 */
public class HttpPacket extends Packet {
	private HttpMethod method;
	private String url;
	
	private ParamsWrapper params;
	private String paramsString;
	
	public static HttpPacket encode(Packet packet) {
		HttpPacket httpPacket = new HttpPacket(packet);
		return httpPacket;
	}
	
	public HttpPacket() {
		this.packetType = Packet.HTTP;
		this.method = HttpMethod.GET;
	}
	
	public HttpPacket(Packet packet) {
		this.packetType = Packet.HTTP;
		this.username = packet.getUsername();
		this.messageId = packet.getMessageId();
		this.ack = packet.isAck();
		ByteBuffer remainBuffer = ByteBuffer.wrap(packet.getRemainBytes());
		this.url = this.getString(remainBuffer);
		this.paramsString = this.getString(remainBuffer);
	}
	
	@Override
	public String toString() {
		return String.format("{%s, url: %s, param: %s}", super.toString(), this.getUrl(), this.getParams());
	}

	@Override
	public byte[] toByteArray() {
		byte[] header = super.toByteArray();
		ByteBuffer buffer = ByteBuffer.allocate(header.length + 8 +  getUrl().length() + getParams().toString().length());
		buffer.put(header);
		putString(buffer, getUrl());
		putString(buffer, getParams().toString());
		return buffer.array();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ParamsWrapper getParams() {
		return params;
	}

	public void setParams(ParamsWrapper params) {
		this.params = params;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public String getParamsString() {
		return paramsString;
	}

	public void setParamsString(String paramsString) {
		this.paramsString = paramsString;
	}
	
	
}
