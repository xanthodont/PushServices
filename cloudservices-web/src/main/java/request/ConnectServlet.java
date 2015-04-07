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

public class ConnectServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(ConnectServlet.class);
	private ConnectWriter connectWriter;
	
	@Override
	public void init(ServletConfig config) {
		/** 启动数据库异步写入类 */
		connectWriter = new ConnectWriter();
		connectWriter.startup();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
	/**
	 * 注：多线程环境下运行
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		logger.info("Param - username:" + request.getParameter("username"));
		ConnectInfo connInfo = new ConnectInfo();
		connInfo.setUsername(request.getParameter("username"));
		connInfo.setPassword(request.getParameter("password"));
		connInfo.setResource(request.getParameter("resource"));
		connectWriter.putInfo(connInfo);
		response.getWriter().write("connect success");
		response.flushBuffer();
	}
}
