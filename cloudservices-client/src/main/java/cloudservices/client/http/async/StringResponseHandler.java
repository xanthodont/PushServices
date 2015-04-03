package cloudservices.client.http.async;

import cloudservices.client.http.async.utility.StreamUtility;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieStore;
import java.net.URL;


/**
 * @author : 桥下一粒砂 chenyoca@gmail.com
 * date   : 2012-10-23
 * 字符型响应处理类
 */
public abstract class StringResponseHandler implements ResponseCallback {

    protected CookieStore cookieStore;

	@Override
	final public void onResponse(CookieStore cookieStore,InputStream response,URL url) {
        this.cookieStore = cookieStore;
		String data = null;
		try {
			data = StreamUtility.convertToString(response);
		} catch (IOException exp) {
			exp.printStackTrace();
			onStreamError(exp);
		}catch (Throwable exp) {
            exp.printStackTrace();
            onUncatchedError(exp);
        } finally{
			StreamUtility.closeSilently(response);
		}
		onResponse(data,url);
	}

    @Override
    public void onUncatchedError(Throwable exp) {

    }

    protected abstract void onResponse(String content,URL url);
	
}
