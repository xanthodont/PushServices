package cloudservices.client;

public class ClientConfiguration {
	/** 连接方式
	 * 1  --  短连接，采用Http轮询方式
	 * 2  --  长连接，采用Mqtt连接方式
	 * 3  --  默认方式，长短连接相结合
	 */
	private int connectType = 3;
	
	/*************************************
	 * 客户端服务相关配置
	 *************************************/
	/** 服务器地 */
	private String host;
	/** 服务器端口 */
	private int port;
	/** 心跳周期，单位：秒
	 *  默认值 30 
	 */
	private short keepAlive = 30;
	/** 重连的次数
	 *  默认值 6  
	 */
	private int reconnectAttemptsMax = 6;
	/** 两次重连间的时间间隔，单位：秒
	 *  默认值 3 
	 */
	private long reconnectDelay = 3;
	/** 两次重连间的时间间隔最大值，单位：秒
	 *  默认值 300 
	 */
	private long reconnectDelayMax = 300;
	/** 重连回归指数 
	 *  默认为1，表示不使用回归指数
	 */
	private double reconnectBackOffMultiplier = 1;
	/** 消息的最大缓冲大小
	 *  默认值 2M 
	 */
	private int bufferSize = 2*1024*1024;//发送最大缓冲为2M
	/** 资源 */
	private String resourceName;
	/** 主题 */
	private String topic;
	/** http接口地址 */
	private String sendUrl;
	private String receiveUrl;
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
	
	
	public String getSendUrl() {
		return sendUrl;
	}


	public void setSendUrl(String sendUrl) {
		this.sendUrl = sendUrl;
	}


	public String getReceiveUrl() {
		return receiveUrl;
	}


	public void setReceiveUrl(String receiveUrl) {
		this.receiveUrl = receiveUrl;
	}


	public short getKeepAlive() {
		return keepAlive;
	}


	public void setKeepAlive(short keepAlive) {
		this.keepAlive = keepAlive;
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
	
	public String getMqttHost() {
		return String.format("tcp://%s:%d", host, port);
	}


	public String getTopic() {
		return topic;
	}


	public void setTopic(String topic) {
		this.topic = topic;
	}


	public boolean isDebugMode() {
		return debugMode;
	}


	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}


	public int getConnectType() {
		return connectType;
	}


	public void setConnectType(int connectType) {
		this.connectType = connectType;
	}


	public int getReconnectAttemptsMax() {
		return reconnectAttemptsMax;
	}


	public void setReconnectAttemptsMax(int reconnectAttemptsMax) {
		this.reconnectAttemptsMax = reconnectAttemptsMax;
	}


	public long getReconnectDelay() {
		return reconnectDelay;
	}


	public void setReconnectDelay(long reconnectDelay) {
		this.reconnectDelay = reconnectDelay;
	}


	public int getBufferSize() {
		return bufferSize;
	}


	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}


	public long getReconnectDelayMax() {
		return reconnectDelayMax;
	}


	public void setReconnectDelayMax(long reconnectDelayMax) {
		this.reconnectDelayMax = reconnectDelayMax;
	}


	public double getReconnectBackOffMultiplier() {
		return reconnectBackOffMultiplier;
	}


	public void setReconnectBackOffMultiplier(double reconnectBackOffMultiplier) {
		this.reconnectBackOffMultiplier = reconnectBackOffMultiplier;
	}
	
	
}
