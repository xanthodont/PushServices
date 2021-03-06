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
	
	public HttpPacket() {
		this.packetType = Packet.HTTP;
		this.method = HttpMethod.GET;
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

	@Override
	public String toString() {
		return String.format("{%s, url: %s, param: %s}", super.toString(), this.getUrl(), this.getParams());
	}

	@Override
	protected byte[] processSubData() {
		byte[] urlData = encodingString(url);
		byte[] paramsData = encodingString(getParams().toString());
		ByteBuffer buffer = ByteBuffer.allocate(2 +  urlData.length + 2 + paramsData.length);
		buffer.putShort((short) urlData.length);
		buffer.put(urlData);
		buffer.putShort((short) paramsData.length);
		buffer.put(paramsData);
		return buffer.array();
	}

	@Override
	protected void subDecode(byte[] remain) {
		// TODO Auto-generated method stub
		ByteBuffer remainBuffer = ByteBuffer.wrap(remain);
		this.url = this.getString(remainBuffer);
		this.paramsString = this.getString(remainBuffer);
	}
	
	
}
