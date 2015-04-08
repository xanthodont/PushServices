package request;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dna.mqtt.moquette.messaging.spi.IConnectCallback;
import org.dna.mqtt.moquette.messaging.spi.impl.events.PublishEvent;
import org.dna.mqtt.moquette.proto.messages.ConnectMessage;
import org.dna.mqtt.moquette.proto.messages.PublishMessage;
import org.dna.mqtt.moquette.server.IAuthenticator;
import org.dna.mqtt.moquette.server.Server;

import request.writer.ConnectInfo;
import request.writer.ConnectWriter;
import utils.ApplicationContextUtil;

public class MqttServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(MqttServlet.class);
	private Server mqttServer;
	private ConnectWriter connectWriter;
	
	@Override
	public void init(ServletConfig config) {
		mqttServer = (Server) ApplicationContextUtil.getBeanByName("mqttServer");
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
			}

			@Override
			public void onSendMessageSuccess(PublishMessage pubMessage) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSendMessageTimeout() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStoreOfflineMessage(PublishEvent newPublishEvt) {
				// TODO Auto-generated method stub
				logger.info(String.format("store offline message:%s", newPublishEvt));
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
