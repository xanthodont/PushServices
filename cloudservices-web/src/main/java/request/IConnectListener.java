package request;

public interface IConnectListener {
	/**
	 * 连接消息到达
	 */
	void connectionArrive();
	/**
	 * 账号密码验证成功
	 */
	void authorizeSuccess();
	/**
	 * 初始订阅成功
	 */
	void subscribeSuccess();
}
