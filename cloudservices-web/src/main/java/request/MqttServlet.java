package request;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.model.PushUser;
import mvc.model.OnlineStatus;

import org.apache.log4j.Logger;


import org.dna.mqtt.moquette.messaging.spi.IConnectCallback;
import org.dna.mqtt.moquette.messaging.spi.impl.events.PublishEvent;
import org.dna.mqtt.moquette.proto.messages.ConnectMessage;
import org.dna.mqtt.moquette.proto.messages.PublishMessage;
import org.dna.mqtt.moquette.server.IAuthenticator;
import org.dna.mqtt.moquette.server.Server;

import cloudservices.client.ClientConfiguration;
import cloudservices.client.ClientService;
import cloudservices.client.ConfigException;
import cloudservices.client.ConnectException;
import cloudservices.client.packets.Packet;
import cloudservices.client.packets.PacketFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import request.writer.ConnectInfo;
import request.writer.ConnectWriter;
import request.writer.MessageInfo;
import request.writer.SendMessageWriter;
import utils.ApplicationContextUtil;
import utils.StringUtil;

public class MqttServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(MqttServlet.class);
	private Server mqttServer;
	private ConnectWriter connectWriter;
	private SendMessageWriter sendMessageWriter;
	private JedisPool jedisPool;
	
	@Override
	public void init(ServletConfig config) {
		jedisPool = (JedisPool) ApplicationContextUtil.getBeanByName("jedisPool");
		mqttServer = Server.getInstance();
		/** 设置账号密码验证器 */
		mqttServer.setAuthenticator(new IAuthenticator() {
			@Override
			public boolean checkValid(String username, String password) {
				// TODO Auto-generated method stub
				return password.equals("kk-xtd-push");
			}
		});
		/** 数据库异步写入类 */
		connectWriter = ConnectWriter.getInstance();
		sendMessageWriter = SendMessageWriter.getInstance();
		/** 添加连接监听器 */
		mqttServer.setConnectCallback(new IConnectCallback() {
			@Override
			public void onSubscribeSuccess() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onConnectionArrive(String username, String willTopic) {
				// TODO Auto-generated method stub
				logger.info(String.format("username:%s connect", username));
			}
			
			@Override
			public void onAuthorizeSuccess(ConnectMessage conn) {
				// mqtt_log 保存用户信息
				logger.info(String.format("username:%s connect success, begin update database", conn.getUsername()));
				ConnectInfo connInfo = new ConnectInfo();
				connInfo.setUsername(conn.getUsername());
				connInfo.setPassword(conn.getPassword());
				connInfo.setResource(conn.getWillTopic());
				connectWriter.putInfo(connInfo);
				// 更新在线状态
				OnlineStatus.updateLongOnlineStatus(jedisPool, conn.getUsername(), "30");
			}

			@Override
			public void onSendMessageSuccess(PublishMessage pubMessage) {
				// TODO Auto-generated method stub
				MessageInfo info = new MessageInfo();
				info.decode(ByteBuffer.wrap(pubMessage.getPayload()));
				info.setPublic2Topic(pubMessage.getTopicName());
				info.setStatus(true);
				sendMessageWriter.putInfo(info);
			}

			@Override
			public void onSendMessageTimeout() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStoreOfflineMessage(PublishEvent newPublishEvt) {
				// TODO Auto-generated method stub
				logger.info(String.format("store offline message:%s", newPublishEvt));
				MessageInfo info = new MessageInfo();
				info.decode(ByteBuffer.wrap(newPublishEvt.getMessage()));
				info.setPublic2Topic(newPublishEvt.getTopic());
				info.setStatus(false);
				sendMessageWriter.putInfo(info);
			}

			@Override
			public void disconnect(String username) {
				// TODO Auto-generated method stub
				// 更新在线状态
				OnlineStatus.updateOfflineStatus(jedisPool, username, "30");
			}

			@Override
			public void onServerStarted() {
				// 服务启动成功之后，启动一个admin客户端
				ClientConfiguration config = ConfigConstants.getInitConfig();
				config.setUsername("admin");
				config.setConnectType(ClientConfiguration.LONG_MQTT);
				config.setBufferSize(1024*1024); // 
				config.setReconnectDelay(5);
				config.setReconnectAttemptsMax(1000); // 服务器端口重连次数要多
				
				ClientService client = ClientService.getInstance();
				try {
					client.config(config);
					//client.startup();  
					client.connect();
				} catch (ConfigException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		});
		
		try {
			mqttServer.startServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
	/**
	 * 注：多线程环境下运行
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
	}
}
