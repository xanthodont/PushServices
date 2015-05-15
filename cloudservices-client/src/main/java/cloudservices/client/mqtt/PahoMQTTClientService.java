package cloudservices.client.mqtt;

import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import cloudservices.client.ClientConfiguration;
import cloudservices.client.ClientService;
import cloudservices.client.ConfigException;
import cloudservices.client.ConnectException;
import cloudservices.client.ISender;
import cloudservices.client.packets.Packet;
import cloudservices.client.packets.PacketFactory;
import cloudservices.utils.StringUtil;

public class PahoMQTTClientService implements ISender {
	private ClientService clientService;
	private MqttAsyncClient mqtt;
	private MqttConnectOptions connOpts;
	//private CallbackConnection connection;
	private volatile boolean done = false;
	
	private String[] subscribeTopics;
	private int[] subqos;
	private String publicTopic = "beidou";
	
	CountDownLatch connLatch;// = new CountDownLatch(2);
	volatile boolean connected = false;
	volatile boolean subscribed = false;
	
	public PahoMQTTClientService(ClientService clientService) {
		this.clientService = clientService;
		
	}
	
	public void config(ClientConfiguration config) throws ConfigException {
		try {
			mqtt = new MqttAsyncClient(config.getMqttHost(), config.getUsername(), null);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			e.printStackTrace();
			throw new ConfigException("不能创建Mqtt类："+e.getStackTrace());
		}
		if (mqtt == null) throw new ConfigException("不能创建Mqtt类：");
		connOpts = new MqttConnectOptions();
        // 连接前清空会话信息, false表示保留回话信息，在用户断线的情况下保留要发送给此客户端的消息  
		connOpts.setCleanSession(false);  
        // 设置重新连接的次数  
		//connOpts.setReconnectAttemptsMax(config.getReconnectAttemptsMax());  
        // 设置重连的间隔时间  
		//connOpts.setReconnectDelay(config.getReconnectDelay()*1000);  
        //mqtt.setReconnectDelayMax(config.getReconnectDelayMax()*1000);
        //mqtt.setReconnectBackOffMultiplier(config.getReconnectBackOffMultiplier());
        // 设置心跳时间  		connOpts.setKeepAliveInterval(config.getKeepAlive());  
        // 设置缓冲的大小  
        //mqtt.setSendBufferSize(config.getBufferSize());
        //mqtt.setReceiveBufferSize(config.getBufferSize() + 100); // 多出来的100字节用于分段消息头的存储
        
        // 设置用户名和密码
        if (StringUtil.isEmpty(config.getUsername())) throw new ConfigException("未配置用户名");
        if (StringUtil.isEmpty(config.getPassword())) throw new ConfigException("未配置密码");
        //connOpts.setClientId(config.getUsername());
        connOpts.setUserName(config.getUsername());
        connOpts.setPassword(config.getPassword().toCharArray());
        connOpts.setConnectionTimeout(config.getConnectTimeout());
        connOpts.setKeepAliveInterval(config.getKeepAlive());
        connOpts.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        if (StringUtil.isEmpty(config.getTopic())) throw new ConfigException("未配置主题信息");
        subscribeTopics = new String[2];
        subscribeTopics[0] = config.getTopic();
        subscribeTopics[1] = String.format("%s/%s", config.getTopic(), config.getUsername());
        subqos = new int[]{1, 1};
        publicTopic = String.format("%s/admin", config.getTopic());
        // 设置遗嘱消息
        connOpts.setWill(config.getTopic(), "willtopic".getBytes(), 1, true);
        
        mqtt.setCallback(new MqttCallback() {
			
			@Override
			public void messageArrived(String topic, MqttMessage message)
					throws Exception {
				// TODO Auto-generated method stub
				ByteBuffer buffer = ByteBuffer.wrap(message.getPayload());
				Packet packet = PacketFactory.getPacket(buffer);
				getClientService().getPacketReader().putPacket(packet);
			}
			
			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void connectionLost(Throwable cause) {
				// TODO Auto-generated method stub
				cause.printStackTrace();
				System.out.println("connectionLost" + cause.getMessage());
				
				clientService.shutdown();//(false); // 下线通知
				clientService.reconnect();
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
		// 重置标识位
		connected = false;
		subscribed = false;
		connLatch = new CountDownLatch(2);
		
		try {
			mqtt.connect(connOpts, null, new IMqttActionListener() {
				@Override
				public void onSuccess(IMqttToken asyncActionToken) {
					// TODO Auto-generated method stub
					connected = true;
					connLatch.countDown();
					
					try {
						mqtt.subscribe(subscribeTopics, subqos, null, new IMqttActionListener() {
							@Override
							public void onSuccess(IMqttToken asyncActionToken) {
								// TODO Auto-generated method stub
								subscribed = true;
								connLatch.countDown();
							}
							
							@Override
							public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
								// TODO Auto-generated method stub
								connLatch.countDown();
							}
						});
					} catch (MqttException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						connLatch.countDown();
					}
				}
				
				@Override
				public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
					// TODO Auto-generated method stub
					connLatch.countDown();
					connLatch.countDown();
				}
			});
		} catch (MqttSecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			connLatch.countDown();
			connLatch.countDown();
		} catch (MqttException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			connLatch.countDown();
			connLatch.countDown();
		}
		
		try {
			connLatch.await();
			//connLatch.await(clientService.getConfiguration().getConnectTimeout(), TimeUnit.SECONDS);  // 连接等待10秒
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
			mqtt.publish(publicTopic, packet.toByteArray(), 1, false, null, new IMqttActionListener() {
				@Override
				public void onSuccess(IMqttToken asyncActionToken) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
					// TODO Auto-generated method stub
					
				}
			});
		} catch (Exception e) {
			// xtd_log 发送消息异常，重新发送
			//clientService.sendPacket(packet, publicTopic);
			//logger.info(String.format("发消息异常-- packet:%s  length:%d", packet.toString(), packet.toByteArray().length));
			e.printStackTrace();
		}
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		try {
			mqtt.disconnect();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
