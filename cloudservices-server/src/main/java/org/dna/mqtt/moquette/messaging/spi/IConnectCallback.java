package org.dna.mqtt.moquette.messaging.spi;

import org.dna.mqtt.moquette.proto.messages.ConnectMessage;

public interface IConnectCallback {
	/**
	 * 连接消息到达
	 * username 用户名
	 * willTopic 遗嘱主题
	 */
	void connectionArrive(String username, String willTopic);
	/**
	 * 账号密码验证成功
	 * msg 连接消息
	 */
	void authorizeSuccess(ConnectMessage msg);
	/**
	 * 初始订阅成功
	 */
	void subscribeSuccess();
}
