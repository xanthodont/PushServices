package cloudservices.client.http;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import cloudservices.client.ClientConfiguration;
import cloudservices.client.ClientService;
import cloudservices.client.ConfigException;
import cloudservices.client.ConnectException;
import cloudservices.client.ISender;
import cloudservices.client.http.async.AsyncHttpConnection;
import cloudservices.client.http.async.BinaryResponseHandler;
import cloudservices.client.http.async.StringResponseHandler;
import cloudservices.client.http.async.support.ParamsWrapper;
import cloudservices.client.http.async.support.RequestInvokerFactory;
import cloudservices.client.mqtt.MQTTCallbackClient;
import cloudservices.client.packets.Packet;
import cloudservices.client.packets.PacketFactory;
import cloudservices.utils.StringUtil;

public class HTTPClientService implements ISender {
	private static Logger logger = Logger.getLogger(HTTPClientService.class);
	
	private ClientService clientService;
	private AsyncHttpConnection http;
	private String sendUrl = "";
	private String receiveUrl = "";
	private String connectUrl = "";
	/** 轮询时间，单位：秒 */
	private long scheduleDelay = 5; 
	/** 接收消息定时器 */
	private ScheduledExecutorService receiveScheduler;
	final Runnable receiveDeamon = new Runnable() {
        public void run() {
            // http_log 轮询读取消息
        	ParamsWrapper params = new ParamsWrapper();
    		params.put("username", clientService.getConfiguration().getUsername());
    		//params.put("topic", String.format("%s/admin", clientService.getConfiguration().getTopic()));
            http.post(receiveUrl, params, new BinaryResponseHandler() {
				@Override
				public void onSubmit(URL url, ParamsWrapper params) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void onConnectError(IOException exp) {
					// http_log 请求连接失败
					logger.error("接收消息连接异常", exp);
				}
				@Override
				public void onStreamError(IOException exp) {
					// TODO Auto-generated method stub
					logger.error("接收消息流异常", exp);
				}
				@Override
				public void onResponse(byte[] data, URL url) {
					// TODO Auto-generated method stub
					ByteBuffer buffer = ByteBuffer.wrap(data);
					int packetSize = buffer.getInt();
					for (int i = 0; i < packetSize; i++) {
						int length = buffer.getInt();
						byte[] packetData = new byte[length];
						buffer.get(packetData);
						Packet packet = PacketFactory.getPacket(ByteBuffer.wrap(packetData));
						clientService.getPacketReader().putPacket(packet);
					}
				}});
        }
    };
	
	CountDownLatch connLatch = new CountDownLatch(3);
	volatile boolean sendInteface = false;
	volatile boolean receiveInteface = false;
	volatile boolean connectInteface = false;

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
		if (StringUtil.isEmpty(config.getConnectUrl())) throw new ConfigException("未设置Http连接Connect的接口地址");
		connectUrl = config.getConnectUrl();
		scheduleDelay = config.getHttpCircle();
	}

	@Override
	public void send(Packet packet) {
		// TODO Auto-generated method stub
		// http_log 发送消息
		ParamsWrapper params = new ParamsWrapper();
		params.put("username", clientService.getConfiguration().getUsername());
		params.put("topic", packet.getPublic2Topic());
		params.put("packet", new String(packet.toByteArray()));
		
		http.post(sendUrl, params, new StringResponseHandler() {
			@Override
			public void onSubmit(URL url, ParamsWrapper params) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onConnectError(IOException exp) {
				// TODO Auto-generated method stub
				logger.error("发送消息连接异常", exp);
			}

			@Override
			public void onStreamError(IOException exp) {
				// TODO Auto-generated method stub
				logger.error("发送消息流异常", exp);
			}

			@Override
			protected void onResponse(String content, URL url) {
				// xtd-log Htpp log
				//client.getClientService().getPacketReader().putPacket(packet);
				System.out.printf("send response：%s\n", content);
			}});
	}

	@Override
	public void connect() throws ConnectException {
		// TODO Auto-generated method stub	
		// 发送消息接口验证
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
		// 接收消息接口验证
		http.get(receiveUrl, new BinaryResponseHandler() {
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
				System.out.println("receive response:" + new String(data));
				receiveInteface = true;
				connLatch.countDown();
			}
		});
		// 连接接口验证
		ParamsWrapper params = new ParamsWrapper();
		params.put("username", clientService.getConfiguration().getUsername());
		params.put("password", clientService.getConfiguration().getPassword());
		params.put("resource", clientService.getConfiguration().getTopic());
		http.get(connectUrl, params, new StringResponseHandler() {
			@Override
			public void onSubmit(URL url, ParamsWrapper params) {
				// TODO Auto-generated method stub
				
			}
			
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
			protected void onResponse(String content, URL url) {
				// TODO Auto-generated method stub
				System.out.println("connect response:" + content);
				connectInteface = true;
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
			if (!connectInteface) throw new ConnectException("连接接口验证失败");
		} 
		// http_log 连接成功，开启定时获取消息
		System.out.printf("连接成功，开启轮询\n");
		receiveScheduler.scheduleWithFixedDelay(receiveDeamon, scheduleDelay, scheduleDelay, TimeUnit.SECONDS);
	}

	
}
