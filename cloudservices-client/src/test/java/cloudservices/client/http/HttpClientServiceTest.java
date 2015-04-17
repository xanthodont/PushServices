package cloudservices.client.http;

import java.io.IOException;
import java.net.URL;






import org.junit.Test;

import cloudservices.client.ClientConfiguration;
import cloudservices.client.ClientService;
import cloudservices.client.ConfigException;
import cloudservices.client.ConnectException;
import cloudservices.client.TestBase;
import cloudservices.client.http.async.AsyncHttpConnection;
import cloudservices.client.http.async.StringResponseHandler;
import cloudservices.client.http.async.support.ParamsWrapper;
import cloudservices.client.packets.TextPacket;

public class HttpClientServiceTest extends TestBase {
	private AsyncHttpConnection http = AsyncHttpConnection.getInstance();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientConfiguration config = new ClientConfiguration(SERVER_IP, MQTT_PORT);
		config.setUsername("http_admin");
		config.setPassword(DEFAULT_PASSWORD);
		config.setTopic(TOPIC);
		config.setSendUrl(SEND_URL);
		config.setReceiveUrl(RECEIVE_URL);
		config.setConnectUrl(CONNECT_URL);
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
		int i = 0;
		while(true) {
			//client.sendPacket(new Packet());
			try {
				Thread.sleep(8000);
				TextPacket t = new TextPacket();
				t.setText(String.valueOf(i++));
				client.sendPacket(t, "213");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void sendUrlTest() {
		http.get(SEND_URL, new StringResponseHandler() {
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
