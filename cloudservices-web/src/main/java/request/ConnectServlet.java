package request;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dna.mqtt.moquette.messaging.spi.impl.subscriptions.Subscription;
import org.dna.mqtt.moquette.proto.messages.AbstractMessage;
import org.dna.mqtt.moquette.proto.messages.AbstractMessage.QOSType;
import org.dna.mqtt.moquette.server.Server;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

import request.writer.ConnectInfo;
import request.writer.ConnectWriter;
import utils.ApplicationContextUtil;
import utils.StringUtil;

public class ConnectServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(ConnectServlet.class);
	private ConnectWriter connectWriter;
	private Server mqttServer;
	
	@Override
	public void init(ServletConfig config) {
		/** 启动数据库异步写入类 */
		connectWriter = ConnectWriter.getInstance();
		mqttServer = (Server) ApplicationContextUtil.getBeanByName("mqttServer");
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
	/**
	 * 注：多线程环境下运行
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		logger.info("connect Param - username:" + request.getParameter("username"));
		
		// 验证参数有效性
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String resource = request.getParameter("resource");
		if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password) || StringUtil.isEmpty(resource)) {
			response.getWriter().write("connect fail");
			response.flushBuffer();
			return;
		}
		if (!mqttServer.getAuthenticator().checkValid(username, password)) {
			response.getWriter().write("authorization fail");
			response.flushBuffer();
			return;
		}
		
		// 将消息存储数据库写入器
		ConnectInfo connInfo = new ConnectInfo();
		connInfo.setUsername(username);
		connInfo.setPassword(password);
		connInfo.setResource(resource);
		connectWriter.putInfo(connInfo);
		
		// 设置订阅信息
		String[] subscribeTopics = new String[] {
				resource, 
				String.format("%s/%s", resource, username)};
        for (String tp : subscribeTopics) {
        	Subscription newSubscription = new Subscription(username, tp, QOSType.LEAST_ONE, false);
        	newSubscription.setActive(false); // Http连接需要设置默认为离线状态
        	mqttServer.getMessaging().getSubscriptions().add(newSubscription);
        }
		
		response.getWriter().write("connect success");
		response.flushBuffer();
	}
}
