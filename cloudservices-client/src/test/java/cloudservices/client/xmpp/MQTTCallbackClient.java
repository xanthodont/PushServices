package cloudservices.client.xmpp;

import java.net.ProtocolException;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.hawtdispatch.Dispatch;
import org.fusesource.hawtdispatch.DispatchQueue;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import org.fusesource.mqtt.client.Tracer;
import org.fusesource.mqtt.codec.MQTTFrame;
import org.fusesource.mqtt.codec.PUBLISH;

import cloudservices.client.packets.TextPacket;

public class MQTTCallbackClient {
    private final static String CONNECTION_STRING = "tcp://172.21.4.64:1883";  
    private final static boolean CLEAN_START = true;  
    private final static short KEEP_ALIVE = 30;// 低耗网络，但是又需要及时获取数据，心跳30s  
    private final static String CLIENT_ID = "publishService";  
    public  static Topic[] topics = {  
                    new Topic("china/beijing", QoS.EXACTLY_ONCE),  
                    new Topic("china/tianjin", QoS.AT_LEAST_ONCE),  
                    new Topic("common", QoS.AT_MOST_ONCE)};  
    public final  static long RECONNECTION_ATTEMPT_MAX=6;  
    public final  static long RECONNECTION_DELAY=2000;  
      
    public final static int SEND_BUFFER_SIZE=2*1024*1024;//发送最大缓冲为2M  
	
	public static void main(String[] args) {
		try {
			MQTT mqtt = new MQTT();
			//设置mqtt broker的ip和端口  
            mqtt.setHost(CONNECTION_STRING);  
            //连接前清空会话信息  
            mqtt.setCleanSession(CLEAN_START);  
            //设置重新连接的次数  
            mqtt.setReconnectAttemptsMax(RECONNECTION_ATTEMPT_MAX);  
            //设置重连的间隔时间  
            mqtt.setReconnectDelay(RECONNECTION_DELAY);  
            //设置心跳时间  
            mqtt.setKeepAlive(KEEP_ALIVE);  
            //设置缓冲的大小  
            mqtt.setSendBufferSize(SEND_BUFFER_SIZE);
			/*
			// MQTT设置说明
			mqtt.setHost("tcp://172.21.4.64:1883");
			mqtt.setClientId("123876543210"); // 用于设置客户端会话的ID。在setCleanSession(false);被调用时，MQTT服务器利用该ID获得相应的会话。此ID应少于23个字符，默认根据本机地址、端口和时间自动生成
			mqtt.setCleanSession(false); // 若设为false，MQTT服务器将持久化客户端会话的主体订阅和ACK位置，默认为true
			mqtt.setKeepAlive((short) 60);// 定义客户端传来消息的最大时间间隔秒数，服务器可以据此判断与客户端的连接是否已经断开，从而避免TCP/IP超时的长时间等待
			//mqtt.setUserName("admin");// 服务器认证用户名
			//mqtt.setPassword("admin");// 服务器认证密码

			mqtt.setWillTopic("willTopic");// 设置“遗嘱”消息的话题，若客户端与服务器之间的连接意外中断，服务器将发布客户端的“遗嘱”消息
			mqtt.setWillMessage("willMessage");// 设置“遗嘱”消息的内容，默认是长度为零的消息
			mqtt.setWillQos(QoS.AT_LEAST_ONCE);// 设置“遗嘱”消息的QoS，默认为QoS.ATMOSTONCE
			mqtt.setWillRetain(true);// 若想要在发布“遗嘱”消息时拥有retain选项，则为true
			//mqtt.setVersion("3.1.1");

			// 失败重连接设置说明
			mqtt.setConnectAttemptsMax(10L);// 客户端首次连接到服务器时，连接的最大重试次数，超出该次数客户端将返回错误。-1意为无重试上限，默认为-1
			mqtt.setReconnectAttemptsMax(3L);// 客户端已经连接到服务器，但因某种原因连接断开时的最大重试次数，超出该次数客户端将返回错误。-1意为无重试上限，默认为-1
			mqtt.setReconnectDelay(10L);// 首次重连接间隔毫秒数，默认为10ms
			mqtt.setReconnectDelayMax(30000L);// 重连接间隔毫秒数，默认为30000ms
			mqtt.setReconnectBackOffMultiplier(2);// 设置重连接指数回归。设置为1则停用指数回归，默认为2

			// Socket设置说明
			mqtt.setReceiveBufferSize(65536);// 设置socket接收缓冲区大小，默认为65536（64k）
			mqtt.setSendBufferSize(65536);// 设置socket发送缓冲区大小，默认为65536（64k）
			mqtt.setTrafficClass(8);// 设置发送数据包头的流量类型或服务类型字段，默认为8，意为吞吐量最大化传输

			// 带宽限制设置说明
			mqtt.setMaxReadRate(0);// 设置连接的最大接收速率，单位为bytes/s。默认为0，即无限制
			mqtt.setMaxWriteRate(0);// 设置连接的最大发送速率，单位为bytes/s。默认为0，即无限制
*/
			// 选择消息分发队列
			mqtt.setDispatchQueue(Dispatch.createQueue("foo"));// 若没有调用方法setDispatchQueue，客户端将为连接新建一个队列。如果想实现多个连接使用公用的队列，显式地指定队列是一个非常方便的实现方法
			//DispatchQueue queue = mqtt.getDispatchQueue();
			
			// 设置跟踪器
			
			mqtt.setTracer(new Tracer() {
				@Override
				public void onReceive(MQTTFrame frame) {
					if (frame.messageType() == PUBLISH.TYPE) {
						try {
							PUBLISH b = new PUBLISH().decode(frame);
							System.out.println("recv Publish: " + b);
						} catch (ProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("recv Publish: " + frame);
					}
					else 
						System.out.println("recv: " + frame);
				}

				@Override
				public void onSend(MQTTFrame frame) {
					System.out.println("send: " + frame);
				}

				@Override
				public void debug(String message, Object... args) {
					System.out.println(String.format("debug: " + message, args));
				}
			});

			// 使用回调式API
			final CallbackConnection callbackConnection = mqtt
					.callbackConnection();
			
			// 连接监听
			callbackConnection.listener(new Listener() {

				// 连接失败
				@Override
				public void onFailure(Throwable value) {
					System.out.println("===========connect failure===========");
					callbackConnection.disconnect(null);
				}

				// 连接断开
				@Override
				public void onDisconnected() {
					System.out.println("====mqtt disconnected=====");

				}

				// 连接成功
				@Override
				public void onConnected() {
					System.out.println("====mqtt connected=====");

				}

				@Override
				public void onPublish(UTF8Buffer topic, Buffer payload,
						Runnable ack) {
					// TODO Auto-generated method stub
					System.out.println("=============receive msg================"
									+ new String(payload.toByteArray()));
					// onComplete.run();
				}
			});

			// 连接
			callbackConnection.connect(new Callback<Void>() {

				// 连接失败
				public void onFailure(Throwable value) {
					System.out.println("============连接失败：" + value.getLocalizedMessage() + "============");
				}

				// 连接成功
				public void onSuccess(Void v) {
					// 订阅主题
					//Topic[] topics = { new Topic("china/beijing", QoS.AT_LEAST_ONCE) };
					callbackConnection.subscribe(topics,
							new Callback<byte[]>() {
								// 订阅主题成功
								public void onSuccess(byte[] qoses) {
									System.out.println("========订阅成功=======" + new String(qoses));
								}

								// 订阅主题失败
								public void onFailure(Throwable value) {
									System.out.println("========订阅失败=======");
									callbackConnection.disconnect(null);
								}
							});

					// 发布消息
					TextPacket tp = new TextPacket();
					tp.setMessageId((short) 101);
					tp.setBytes("vvv".getBytes());
					
					callbackConnection.publish("china/shenzhen", tp.toArrayBytes(),
							QoS.EXACTLY_ONCE, true, new Callback<Void>() {
								public void onSuccess(Void v) {
									System.out.println("===========消息发布成功============");
								}

								public void onFailure(Throwable value) {
									System.out.println("========消息发布失败=======");
									callbackConnection.disconnect(null);
								}
							});

				}
			});

			while(true){  
				TextPacket tp = new TextPacket();
				tp.setMessageId((short) 101);
				tp.setBytes("vvv".getBytes());
				//callbackConnection.
				callbackConnection.publish("common", tp.toArrayBytes(), QoS.AT_LEAST_ONCE, false, null);
				Thread.sleep(3000);
            }  

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
