package cloudservices.client;

import cloudservices.client.packets.FilePacket;

public class ClientConfiguration {
	/** 基于HTTP协议的短连接轮询方式 */
	public static final int SHORT_HTTP = 1;
	/** 基于MQTT协议的长连接方式 */
	public static final int LONG_MQTT = 2;
	/** 长短连接共存方式 */
	public static final int BOTH =3;
	/** 连接方式
	 * 1  --  短连接，采用Http轮询方式
	 * 2  --  长连接，采用Mqtt连接方式
	 * 3  --  默认方式，长短连接相结合
	 */
	private int connectType = BOTH;
	
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
	/** 消息的最大缓冲大小，如果一条消息大于这个缓冲大小，那么这条消息将会被划分
	 *  默认值 0.5M 
	 *  注：因为系统有长短连接之分，短连接依赖于服务器Tomcat，Tomcat对消息大小有限制，所以这个缓冲大小应小于服务器设定的限定值
	 */
	private int bufferSize = 1*1024*1024/2;//发送最大缓冲为0.5M
	/** 资源 */
	private String resourceName;
	/** 主题 */
	private String topic;
	/** http接口地址 */
	private String sendUrl;
	private String receiveUrl;
	private String connectUrl;
	/** 重连机制 */
	
	/** Http轮询周期 */
	private int httpCircle = 30;
	
	/** 用户名 */
	private String username;
	/** 密码 */
	private String password;
	
	private String model;
	private String name;
	
	/** 连接超时时间 */
	private int connectTimeout = 20;
	
	/** 调试模式
	 *  true 表示开启调试模式，程序输出日志信息
	 *  false 表示调试模式关闭状态。默认为false 
	 */
	private boolean debugMode;
	
	private int numberOfSchedulerThread = 1;
	/** 文件接收保存文件夹 */
	private String filePath = "\\";
	
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
		FilePacket.setFilePath(filePath);
	}
	public ClientConfiguration() {}
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


	public int getConnectTimeout() {
		return connectTimeout;
	}


	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}


	public String getConnectUrl() {
		return connectUrl;
	}


	public void setConnectUrl(String connectUrl) {
		this.connectUrl = connectUrl;
	}
	
	
}
