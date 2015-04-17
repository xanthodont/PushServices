package cloudservices.client.http.async.support;

import cloudservices.client.http.async.ResponseCallback;
import cloudservices.client.http.async.support.RequestInvoker.HttpMethod;

/**
 * @author : 桥下一粒砂 chenyoca@gmail.com
 * date   : 2012-10-23
 * Invoker实现类创建工厂
 */
public class RequestInvokerFactory {
	
	private static Class<? extends RequestInvoker> InvokerType = SimpleHttpInvoker.class;
	
	public static RequestInvoker obtain(HttpMethod method,String url,ParamsWrapper params,ResponseCallback callback){
		RequestInvoker invoker;
		if(!InvokerType.equals(SimpleHttpInvoker.class)){
			try {
				invoker = InvokerType.newInstance();
			} catch (Exception exp) {
				System.err.println(String.format("Cannot instance from %s, used default SimpleHttpInvoker.",InvokerType.getName()));
				invoker = new SimpleHttpInvoker();
			}
		}else{
			invoker = new SimpleHttpInvoker();
		}
		invoker.init(method, url, params, callback);
		return invoker;
	}
	
	public static RequestInvoker obtain(HttpMethod method,String url,String paramsString,ResponseCallback callback){
		RequestInvoker invoker;
		if(!InvokerType.equals(SimpleHttpInvoker.class)){
			try {
				invoker = InvokerType.newInstance();
			} catch (Exception exp) {
				System.err.println(String.format("Cannot instance from %s, used default SimpleHttpInvoker.",InvokerType.getName()));
				invoker = new SimpleHttpInvoker();
			}
		}else{
			invoker = new SimpleHttpInvoker();
		}
		invoker.init(method, url, paramsString, callback);
		return invoker;
	}
	
	/**
	 * 向工厂注册一个Invoker实现类
	 * @param clazz 实现类的类对象
	 */
	public static void register(Class<? extends RequestInvoker> clazz){
		InvokerType = clazz;
	}
	
}
