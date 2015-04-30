package org.dna.mqtt.moquette.messaging.spi;

import org.dna.mqtt.moquette.messaging.spi.impl.events.PublishEvent;
import org.dna.mqtt.moquette.proto.messages.ConnectMessage;
import org.dna.mqtt.moquette.proto.messages.PublishMessage;

public interface IConnectCallback {
	/**
	 * 连接消息到达
	 * username 用户名
	 * willTopic 遗嘱主题
	 */
	void onConnectionArrive(String username, String willTopic);
	/**
	 * 账号密码验证成功
	 * msg 连接消息
	 */
	void onAuthorizeSuccess(ConnectMessage msg);
	/**
	 * 初始订阅成功
	 */
	void onSubscribeSuccess();
	/**
	 * 消息发送成功
	 * @param pubMessage
	 */
	void onSendMessageSuccess(PublishMessage pubMessage);
	/**
	 * 离线消息存储-内存
	 */
	void onStoreOfflineMessage(PublishEvent newPublishEvt);
	/**
	 * 离线消息超时
	 */
	void onSendMessageTimeout();
	/**
	 * 用户断开连接
	 * @param username 
	 */
	void disconnect(String username);
	/**
	 * 服务启动成功
	 */
	void onServerStarted();
}
