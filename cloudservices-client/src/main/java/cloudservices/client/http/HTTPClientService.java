package cloudservices.client.http;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cloudservices.client.ClientConfiguration;
import cloudservices.client.ClientService;
import cloudservices.client.ConfigException;
import cloudservices.client.ConnectException;
import cloudservices.client.ISender;
import cloudservices.client.http.async.AsyncHttpConnection;
import cloudservices.client.http.async.BinaryResponseHandler;
import cloudservices.client.http.async.support.ParamsWrapper;
import cloudservices.client.http.async.support.RequestInvokerFactory;
import cloudservices.client.packets.Packet;
import cloudservices.utils.StringUtil;

public class HTTPClientService implements ISender {
	private ClientService clientService;
	private AsyncHttpConnection http;
	private String sendUrl = "";
	private String receiveUrl = "";
	/** 轮询时间，单位：秒 */
	private long scheduleDelay = 30; 
	/** 接收消息定时器 */
	private ScheduledExecutorService receiveScheduler;
	final Runnable receiveDeamon = new Runnable() {
        public void run() {
            // http_log 轮询读取消息
            http.post(receiveUrl, null, new BinaryResponseHandler() {
				@Override
				public void onSubmit(URL url, ParamsWrapper params) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void onConnectError(IOException exp) {
					// http_log 请求连接失败
					
				}
				@Override
				public void onStreamError(IOException exp) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void onResponse(byte[] data, URL url) {
					// TODO Auto-generated method stub
					ByteBuffer buffer = ByteBuffer.wrap(data);
					Packet packet = Packet.parse(buffer);
					clientService.getPacketReader().putPacket(packet);
				}});
        }
    };
	
	CountDownLatch connLatch = new CountDownLatch(2);
	volatile boolean sendInteface = false;
	volatile boolean receiveInteface = false;

	public HTTPClientService(ClientService clientService) {
		// TODO Auto-generated constructor stub
		this.clientService = clientService;
		http = AsyncHttpConnection.getInstance();
		receiveScheduler = Executors.newScheduledThreadPool(1);
	}

	public void config(ClientConfiguration config) throws ConfigException {
		// TODO Auto-generated method stub
		if (StringUtil.isEmpty(config.getSendUrl())) throw new ConfigException("未设置Http发送消息的接口地址");
		sendUrl = config.getSendUrl();
		if (StringUtil.isEmpty(config.getReceiveUrl())) throw new ConfigException("未设置Http接收消息的接口地址");
		receiveUrl = config.getReceiveUrl();
	}

	@Override
	public void send(Packet packet) {
		// TODO Auto-generated method stub
		// http_log 发送消息
		
		//http.post(sendUrl, null, null);
	}

	@Override
	public void connect() throws ConnectException {
		// TODO Auto-generated method stub
		http.get(sendUrl, new BinaryResponseHandler() {
			@Override
			public void onSubmit(URL url, ParamsWrapper params) {}
			
			@Override
			public void onStreamError(IOException exp) {
				// TODO Auto-generated method stub
				connLatch.countDown();
			}
			
			@Override
			public void onConnectError(IOException exp) {
				// TODO Auto-generated method stub
				connLatch.countDown();
			}
			
			@Override
			public void onResponse(byte[] data, URL url) {
				// TODO Auto-generated method stub
				System.out.println("send response:" + new String(data));
				sendInteface = true;
				connLatch.countDown();
			}
		});
		http.get(sendUrl, new BinaryResponseHandler() {
			@Override
			public void onSubmit(URL url, ParamsWrapper params) {}
			
			@Override
			public void onStreamError(IOException exp) {
				// TODO Auto-generated method stub
				connLatch.countDown();
			}
			
			@Override
			public void onConnectError(IOException exp) {
				// TODO Auto-generated method stub
				connLatch.countDown();
			}
			
			@Override
			public void onResponse(byte[] data, URL url) {
				// TODO Auto-generated method stub
				receiveInteface = true;
				connLatch.countDown();
			}
		});
		
		try {
			connLatch.await(clientService.getConfiguration().getConnectTimeout(), TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (!sendInteface) throw new ConnectException("发送消息接口验证失败");
			if (!receiveInteface) throw new ConnectException("接收消息接口验证失败");
		} 
		// http_log 连接成功，开启定时获取消息
		System.out.printf("连接成功，开启轮询");
		receiveScheduler.scheduleWithFixedDelay(receiveDeamon, scheduleDelay, scheduleDelay, TimeUnit.SECONDS);
	}

	
}
