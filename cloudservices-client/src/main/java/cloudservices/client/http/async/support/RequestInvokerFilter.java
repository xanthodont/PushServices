package cloudservices.client.http.async.support;

/**
 * author : 桥下一粒砂 (chenyoca@gmail.com)
 * date   : 2013-06-06
 * 在requestInvoker被执行前的处理接口
 */
public interface RequestInvokerFilter {

	/**
	 * 在requestInvoker被调度执行时回调。
	 * @param requestInvoker 将要被调度执行的对象。
	 */
	void onRequestInvoke(RequestInvoker requestInvoker);

}
