package request;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dna.mqtt.moquette.messaging.spi.impl.events.PublishEvent;
import org.dna.mqtt.moquette.server.Server;

import utils.ApplicationContextUtil;
import utils.StringUtil;

public class ReceiveServlet  extends HttpServlet {
	private static Logger logger = Logger.getLogger(ReceiveServlet.class);
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
		logger.info("receive Param - username:" + request.getParameter("username"));
		
		// 验证参数有效性
		String username = request.getParameter("username");
		if (StringUtil.isEmpty(username)) {
			response.getWriter().write("receive fail");
			response.flushBuffer();
			return;
		}
		String rs = "receive msg:";
		List<PublishEvent> publishedEvents = mqttServer.getStorageService().retrivePersistedPublishes(username);
		ByteBuffer resultBuffer = ByteBuffer.allocate(4);
		resultBuffer.putInt(publishedEvents.size());
        if (publishedEvents == null) {
        	
        } else {
        	for (PublishEvent pubEvt : publishedEvents) {
        		ByteBuffer oldBuffer = resultBuffer;
        		resultBuffer = ByteBuffer.allocate(oldBuffer.capacity() + 4 + pubEvt.getMessage().length);
        		resultBuffer.put(oldBuffer.array());
        		resultBuffer.putInt(pubEvt.getMessage().length);
        		resultBuffer.put(pubEvt.getMessage());
        	}
        }
		
		ServletOutputStream out = response.getOutputStream();
		out.write(resultBuffer.array());
		response.flushBuffer();
	}
}
