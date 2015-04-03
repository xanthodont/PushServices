package cloudservices.client.http.async;

import cloudservices.client.http.async.utility.StreamUtility;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieStore;
import java.net.URL;

/**
 * @author : 桥下一粒砂 chenyoca@gmail.com
 * date   : 2012-10-24
 * desc   : 字节型数据处理类
 */
public abstract class BinaryResponseHandler implements ResponseCallback {

    protected CookieStore cookieStore;

	@Override
	final public void onResponse(CookieStore cookieStore,InputStream response,URL url) {
        this.cookieStore = cookieStore;
		byte[] data = null;
		try {
			data = StreamUtility.convertToByteArray(response);
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

	public abstract void onResponse(byte[] data,URL url);
	
}
