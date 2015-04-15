package cloudservices.client.mqtt;

import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.hawtdispatch.Task;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.Promise;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import org.fusesource.mqtt.codec.PUBLISH;

import cloudservices.client.ClientConfiguration;
import cloudservices.client.ClientService;
import cloudservices.client.ConfigException;
import cloudservices.client.ConnectException;
import cloudservices.client.ISender;
import cloudservices.client.packets.Packet;
import cloudservices.utils.StringUtil;

public class MQTTClientService implements ISender {
	private static Logger logger = Logger.getLogger(MQTTClientService.class);
	private ClientService clientService;
	private MQTT mqtt;
	private CallbackConnection connection;
	private volatile boolean done = false;
	
	private Topic[] subscribeTopics;
	private String publicTopic = "common";
	
	CountDownLatch connLatch = new CountDownLatch(2);
	volatile boolean connected = false;
	volatile boolean subscribed = false;
	
	public MQTTClientService(ClientService clientService) {
		this.clientService = clientService;
		mqtt = new MQTT();
	}
	
	public void config(ClientConfiguration config) throws ConfigException {
        try {
        	// 设置mqtt broker的ip和端口  
			mqtt.setHost(config.getMqttHost());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ConfigException("mqtt host配置有误!");
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new ConfigException("mqtt host配置有误!");	
		}
        // 连接前清空会话信息, false表示保留回话信息，在用户断线的情况下保留要发送给此客户端的消息  
        mqtt.setCleanSession(false);  
        // 设置重新连接的次数  
        mqtt.setReconnectAttemptsMax(config.getReconnectAttemptsMax());  
        // 设置重连的间隔时间  
        mqtt.setReconnectDelay(config.getReconnectDelay()*1000);  
        mqtt.setReconnectDelayMax(config.getReconnectDelayMax()*1000);
        mqtt.setReconnectBackOffMultiplier(config.getReconnectBackOffMultiplier());
        // 设置心跳时间  
        mqtt.setKeepAlive(config.getKeepAlive());  
        // 设置缓冲的大小  
        mqtt.setSendBufferSize(config.getBufferSize());
        mqtt.setReceiveBufferSize(config.getBufferSize());
        
        // 设置用户名和密码
        if (StringUtil.isEmpty(config.getUsername())) throw new ConfigException("未配置用户名");
        if (StringUtil.isEmpty(config.getPassword())) throw new ConfigException("未配置密码");
        mqtt.setClientId(config.getUsername());
        mqtt.setUserName(config.getUsername());
        mqtt.setPassword(config.getPassword());
        
        
        if (StringUtil.isEmpty(config.getTopic())) throw new ConfigException("未配置主题信息");
        subscribeTopics = new Topic[2];
        subscribeTopics[0] = new Topic(config.getTopic(), QoS.AT_LEAST_ONCE);
        subscribeTopics[1] = new Topic(String.format("%s/%s", config.getTopic(), config.getUsername()), QoS.AT_LEAST_ONCE);
        publicTopic = String.format("%s/admin", config.getTopic());
        // 设置遗嘱消息
        mqtt.setWillTopic(publicTopic);
        mqtt.setWillMessage("xxx");
        
        mqtt.setTracer(new MqttTracer(this));
        
        connection = mqtt.callbackConnection();

        /** 监听器 */
        connection.listener(new Listener() {
        	@Override
        	public void onConnected() {
        		// TODO Auto-generated method stub
        		// mqtt_log 连接成功
        		logger.info("connect success\n");
        	}
        	
        	@Override
        	public void onDisconnected() {
        		// TODO Auto-generated method stub
        		// mqtt_log 断开连接完毕
        		logger.info("disconnect \n");
        		connected = false;
        	}
        	
			@Override
			public void onPublish(UTF8Buffer topic, Buffer body, Runnable ack) {
				// TODO Auto-generated method stub
				ack.run();
			}
			
			@Override
			public void onFailure(Throwable value) {
				// TODO Auto-generated method stub
				// mqtt_log 连接异常
				logger.info("connect fail\n");
			}
		});
	}
	
	public ClientConfiguration getConfiguration() {
		return clientService.getConfiguration();
	}
	
	public ClientService getClientService() {
		return clientService;
	}
	
	public void connect() throws ConnectException {
		connection.connect(new Callback<Void>() {
			@Override
			public void onSuccess(Void value) {
				// TODO Auto-generated method stub
				//System.out.println("connect success");
				connected = true;
				connLatch.countDown();
				// 开始订阅主题
				connection.subscribe(subscribeTopics, new Callback<byte[]>() {
					@Override
					public void onSuccess(byte[] value) {
						// TODO Auto-generated method stub
						connLatch.countDown();
						subscribed = true;
					}
					
					@Override
					public void onFailure(Throwable value) {
						// TODO Auto-generated method stub
						connLatch.countDown();
					}
				});
			}
			
			@Override
			public void onFailure(Throwable value) {
				// TODO Auto-generated method stub
				// mqtt_log 连接失败
				// print value.getMessage();
				logger.info("connect fail");
				connLatch.countDown();
				connLatch.countDown();
				// mqtt_log 关闭连接
				connection.disconnect(null);
			}
		});
		
		try {
			connLatch.await(clientService.getConfiguration().getConnectTimeout(), TimeUnit.SECONDS);  // 连接等待10秒
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (!connected) throw new ConnectException("连接服务器失败");
			if (!subscribed) throw new ConnectException("订阅主题失败");
		}
	}
	
	@Override
	public void send(Packet packet) {
		// TODO Auto-generated method stub
		send(packet, packet.getPublic2Topic());
	}
	
	public void send(Packet packet, String publicTopic) {
		// TODO Auto-generated method stub
		try {
			connection.publish(publicTopic, packet.toByteArray(), QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
				
				@Override
				public void onSuccess(Void value) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onFailure(Throwable value) {
					// TODO Auto-generated method stub
					logger.info("发消息异常");
					value.printStackTrace();
				}
			});
		} catch (Exception e) {
			// xtd_log 发送消息异常，重新发送
			clientService.sendPacket(packet, publicTopic);
			logger.info(String.format("发消息异常-- packet:%s  length:%d", packet.toString(), packet.toByteArray().length));
			e.printStackTrace();
		}
	}

}
