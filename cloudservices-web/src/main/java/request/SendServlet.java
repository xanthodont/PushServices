package request;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dna.mqtt.moquette.messaging.spi.impl.events.PublishEvent;
import org.dna.mqtt.moquette.proto.messages.AbstractMessage.QOSType;
import org.dna.mqtt.moquette.server.Server;

import utils.ApplicationContextUtil;
import utils.StringUtil;

public class SendServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(SendServlet.class);
	private Server mqttServer;
	
	@Override
	public void init(ServletConfig config) {
		mqttServer = (Server) ApplicationContextUtil.getBeanByName("mqttServer");

	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
	/**
	 * 注：多线程环境下运行
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		logger.info("send Param - username:" + request.getParameter("username"));
		
		// 验证参数有效性
		String username = request.getParameter("username");
		String topic = request.getParameter("topic");
		String packet = request.getParameter("packet");
		int messageId = 0;
		if (StringUtil.isEmpty(username) || StringUtil.isEmpty(topic) || StringUtil.isEmpty(packet)) {
			response.getWriter().write("send fail");
			response.flushBuffer();
			return;
		}
		
		mqttServer.getMessaging().publish2Subscribers(topic, QOSType.LEAST_ONE, packet.getBytes(), false, 0);
		
		response.getWriter().write("sned success");
		response.flushBuffer();
	}
}
