package cloudservices.client;

public class ClientConfiguration {
	/*************************************
	 * 客户端服务相关配置
	 *************************************/
	/** 服务器地 */
	private String host;
	/** 服务器端 */
	private int port;
	/** 资源 */
	private String resourceName;
	/** http接口地址 */
	private String httpUrl;
	/** 心跳周期，单位：秒 */
	private int heartbeatCircle;
	/** 重连机制 */
	
	/** Http轮询周期 */
	private int httpCircle;
	
	/** 用户名 */
	private String username;
	/** 密码 */
	private String password;
	
	private String model;
	private String name;
	
	/** 调试模式
	 *  true 表示开启调试模式，程序输出日志信息
	 *  false 表示调试模式关闭状态。默认为false 
	 */
	private boolean debugMode;
	
	private int numberOfSchedulerThread = 1;
	
	public ClientConfiguration(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getHttpUrl() {
		return httpUrl;
	}
	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}
	public int getHeartbeatCircle() {
		return heartbeatCircle;
	}
	public void setHeartbeatCircle(int heartbeatCircle) {
		this.heartbeatCircle = heartbeatCircle;
	}
	public int getHttpCircle() {
		return httpCircle;
	}
	public void setHttpCircle(int httpCircle) {
		this.httpCircle = httpCircle;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	public int getNumberOfSchedulerThread() {
		return numberOfSchedulerThread;
	}


	public void setNumberOfSchedulerThread(int numberOfSchedulerThread) {
		this.numberOfSchedulerThread = numberOfSchedulerThread;
	}
	
	
}
