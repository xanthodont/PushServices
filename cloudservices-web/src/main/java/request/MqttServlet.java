package request;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dna.mqtt.moquette.messaging.spi.IConnectCallback;
import org.dna.mqtt.moquette.proto.messages.ConnectMessage;
import org.dna.mqtt.moquette.server.IAuthenticator;
import org.dna.mqtt.moquette.server.Server;

import utils.ApplicationContextUtil;

public class MqttServlet extends HttpServlet {
	private Server mqttServer;
	
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
		/** 添加连接监听器 */
		mqttServer.setConnectCallback(new IConnectCallback() {
			@Override
			public void subscribeSuccess() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void connectionArrive(String username, String willTopic) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void authorizeSuccess(ConnectMessage msg) {
				// mqtt_log 保存用户信息
				
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
