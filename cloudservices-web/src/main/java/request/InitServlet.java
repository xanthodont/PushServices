package request;

import java.util.ResourceBundle;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import request.writer.ConnectWriter;
import request.writer.InitConfiguration;


public class InitServlet  extends HttpServlet {
	private static Logger logger = Logger.getLogger(Logger.class);
	
	@Override
	public void init(ServletConfig config) {
		InitConfiguration initConfig = InitConfiguration.getInstance();
		//读取配置文件信息
		ResourceBundle bundle = ResourceBundle.getBundle("config");
		initConfig.setCacheQueueSize(Integer.parseInt(bundle.getString("cacheQueueSize")));
		initConfig.setCacheQueueThreadSize(Integer.parseInt(bundle.getString("cacheQueueThreadSize")));
		logger.debug("InitConfig: queueSize:" + initConfig.getCacheQueueSize() + " threadSize:" + initConfig.getCacheQueueThreadSize());
		
		// 启动数据库读写类
		ConnectWriter.getInstance().startup();
	}
}
