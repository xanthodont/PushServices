package cloudservices.client.http.async.support;

import cloudservices.client.http.async.ResponseCallback;

import java.net.CookieStore;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;


/**
 * @author : 桥下一粒砂
 * email  : chenyoca@gmail.com
 * date   : 2012-10-23
 * Http请求执行者，Http请求由此接口的实现类处理。
 */
public abstract class RequestInvoker implements Runnable{
	
	public enum HttpMethod{
		GET,POST,PUT,DELETE,OPTIONS,HEAD,TRACE,CONNECT;
	}

	private final static int HTTP_CONNECT_TIMEOUT = 5 * 1000;
	private final static int HTTP_SOCKET_TIMEOUT = 5 * 1000;

	protected HttpMethod method;
	protected String url;
	protected ParamsWrapper params;
	protected ResponseCallback callback;
	protected Proxy httpProxy;
    protected CookieStore customCookieStore;

    protected int httpConnectTimeout = HTTP_CONNECT_TIMEOUT;
	protected int httpSocketTimeout = HTTP_SOCKET_TIMEOUT;

	/**
	 * 设置Http代理
	 * @param httpProxy Http代理
	 */
	public void setHttpProxy (Proxy httpProxy) {
		this.httpProxy = httpProxy;
	}

    /**
     * 设置Cookie
     * @param customCookieStore
     */
    public void setCustomCookieStore(CookieStore customCookieStore) {
        this.customCookieStore = customCookieStore;
    }

	/**
	 * 设置Http代理
	 * @param host 代理主机
	 * @param port 代理端口
	 */
	public void setProxy(String host,int port){
		SocketAddress socketAddr = new InetSocketAddress(host, port);
		this.httpProxy = new java.net.Proxy(Proxy.Type.HTTP, socketAddr);
	}

	/**
	 * 设置连接超时
	 * @param httpConnectTimeout 超时时间，单位ms.
	 */
	public void setHttpConnectTimeout (int httpConnectTimeout) {
		this.httpConnectTimeout = httpConnectTimeout;
	}

	/**
	 * 设置Socket读取超时
	 * @param httpSocketTimeout 超时时间，单位ms.
	 */
	public void setHttpSocketTimeout (int httpSocketTimeout) {
		this.httpSocketTimeout = httpSocketTimeout;
	}

	public void init (HttpMethod method, String url, ParamsWrapper params,ResponseCallback callback){
		this.method = method;
		this.url = url;
		this.params = params;
		this.callback = callback;
	}

	/**
	 * 隐藏Runnable的实现而已
	 */
	@Override
	public final void run() {
		execute();
	}

	protected abstract void execute();
	
}
