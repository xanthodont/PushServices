package cloudservices.client.http;

import java.io.IOException;
import java.net.URL;




import org.junit.Test;

import cloudservices.client.http.async.AsyncHttpConnection;
import cloudservices.client.http.async.StringResponseHandler;
import cloudservices.client.http.async.support.ParamsWrapper;

public class HttpClientServiceTest {
	private AsyncHttpConnection http = AsyncHttpConnection.getInstance();
	
	@Test
	public void sendUrlTest() {
		http.get("http://172.21.4.64:8080/cloudservices-web/api/receive", null, new StringResponseHandler() {
			@Override
			public void onSubmit(URL url, ParamsWrapper params) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStreamError(IOException exp) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onConnectError(IOException exp) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			protected void onResponse(String content, URL url) {
				// TODO Auto-generated method stub
				System.out.printf("response:%s\n", content);
			}
		});
	}
}
