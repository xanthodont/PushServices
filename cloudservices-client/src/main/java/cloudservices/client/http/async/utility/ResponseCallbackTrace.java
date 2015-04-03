package cloudservices.client.http.async.utility;

import cloudservices.client.http.async.ResponseCallback;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ResponseCallbackTrace implements InvocationHandler {
	
	private Object object;       
	
    public ResponseCallback trace(Object object){         
        this.object = object;
        Class<?>[] interfaces = new Class<?>[]{ResponseCallback.class};
        return (ResponseCallback)Proxy.newProxyInstance(object.getClass().getClassLoader(), interfaces,this);         
    }

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)throws Throwable {
		String methodName = method.getName();
		Object result = method.invoke(object, args);
		if(methodName.equalsIgnoreCase("onSubmit")){
			ThreadWaiter.threadStarted();
		}else if(methodName.equalsIgnoreCase("onResponse")){
			ThreadWaiter.threadFinished();
		}else if(methodName.equalsIgnoreCase("onConnectError")){
			ThreadWaiter.threadFinished();
		}else if(methodName.equalsIgnoreCase("onStreamError")){
			ThreadWaiter.threadFinished();
		}
		return result;
	}

}
