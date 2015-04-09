package cloudservices.client.http;

import java.io.IOException;
import java.net.URL;





import org.junit.Test;

import cloudservices.client.ClientConfiguration;
import cloudservices.client.ClientService;
import cloudservices.client.ConfigException;
import cloudservices.client.ConnectException;
import cloudservices.client.http.async.AsyncHttpConnection;
import cloudservices.client.http.async.StringResponseHandler;
import cloudservices.client.http.async.support.ParamsWrapper;
import cloudservices.client.packets.TextPacket;

public class ReceiveHttpClientServiceTest {
	private AsyncHttpConnection http = AsyncHttpConnection.getInstance();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientConfiguration config = new ClientConfiguration("127.0.0.1", 1883);
		config.setResourceName("android");
		config.setUsername("publishService2");
		config.setPassword("kk-xtd-push");
		config.setTopic("common");
		config.setSendUrl("http://127.0.0.1:8080/cloudservices-web/api/send");
		config.setReceiveUrl("http://127.0.0.1:8080/cloudservices-web/api/receive");
		config.setConnectUrl("http://127.0.0.1:8080/cloudservices-web/api/connect");
		config.setConnectType(1);
		
		ClientService client = ClientService.getInstance();
		try {
			client.config(config);
			client.startup();
			client.connect();
		} catch (ConfigException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			System.out.println(e1.getMessage());
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//TextPacket t = new TextPacket();
		//t.setText("123123");
		//client.sendPacket(t);
		while(true) {
			//client.sendPacket(new Packet());
			try {
				Thread.sleep(4000);
				TextPacket t = new TextPacket();
				t.setText("123123");
				//client.sendPacket(t);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void sendUrlTest() {
		http.get("http://127.0.0.1:8080/cloudservices-web/api/receive", null, new StringResponseHandler() {
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
