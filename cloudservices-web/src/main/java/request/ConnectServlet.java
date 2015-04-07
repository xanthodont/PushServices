package request;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import request.writer.ConnectInfo;
import request.writer.ConnectWriter;
import utils.StringUtil;

public class ConnectServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(ConnectServlet.class);
	private ConnectWriter connectWriter;
	
	@Override
	public void init(ServletConfig config) {
		/** 启动数据库异步写入类 */
		connectWriter = ConnectWriter.getInstance();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
	/**
	 * 注：多线程环境下运行
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		logger.info("Param - username:" + request.getParameter("username"));
		
		// 验证参数有效性
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String resource = request.getParameter("resource");
		if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password) || StringUtil.isEmpty(resource)) {
			response.getWriter().write("connect fail");
			response.flushBuffer();
			return;
		}
		
		// 将消息存储数据库写入器
		ConnectInfo connInfo = new ConnectInfo();
		connInfo.setUsername(username);
		connInfo.setPassword(password);
		connInfo.setResource(resource);
		connectWriter.putInfo(connInfo);
		response.getWriter().write("connect success");
		response.flushBuffer();
	}
}
