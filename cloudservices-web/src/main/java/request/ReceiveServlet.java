package request;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReceiveServlet  extends HttpServlet {
	@Override
	public void init(ServletConfig config) {
		
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
	/**
	 * 注：多线程环境下运行
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		ServletOutputStream out = response.getOutputStream();
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putInt(1);
		buffer.put("test".getBytes());
		out.write(buffer.array());
		response.flushBuffer();
	}
}
