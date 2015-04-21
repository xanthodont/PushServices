package request;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
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
	private static final String ENCODING = "utf-8";
	
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
		logger.info("send Param - length:" + request.getContentLength());
		request.setCharacterEncoding("utf-8");
		
		ServletInputStream in = request.getInputStream();
		
		int length = request.getContentLength();
		byte[] data = new byte[length];
		in.read(data);
		ByteBuffer buffer = ByteBuffer.wrap(data);
		String username = getString(buffer);
		String topic = getString(buffer);
		byte[] packet = getBytes(buffer);
		
		// 验证参数有效性
		if (StringUtil.isEmpty(username) || StringUtil.isEmpty(topic) || packet == null) {
			response.getWriter().write("send fail, param error");
			response.flushBuffer();
			return;
		}
		// 接口测试
		if ("test".equals(topic)) {
			response.getWriter().write("send inteface test success");
			response.flushBuffer();
			return;
		}
		logger.info(String.format("send Param - username: %s, topic: %s, packet.len: %d", username, topic, packet.length));
		
		mqttServer.getMessaging().publish2Subscribers(topic, QOSType.LEAST_ONE, packet, false, 0);
		
		response.getWriter().write("sned success");
		response.flushBuffer();
	}

	private byte[] getBytes(ByteBuffer buffer) {
		// TODO Auto-generated method stub
		int len = buffer.getInt();
		byte[] data = new byte[len];
		buffer.get(data);
		
		return data;
	}

	private String getString(ByteBuffer buffer) {
		// TODO Auto-generated method stub
		return new String(getBytes(buffer));
	}

}
