package cloudservices.client;

public class DefaultConnectionListener implements ConnectionListener {
	private ClientService client;
	
	public DefaultConnectionListener(ClientService client) {
		// TODO Auto-generated constructor stub
		this.client = client;
	}

	@Override
	public void connectionClosed() {
		// TODO Auto-generated method stub
		System.out.printf("-- 连接关闭\n");
	}

	@Override
	public void configSuccess(ClientConfiguration config) {
		// TODO Auto-generated method stub
		System.out.printf("-- 配置成功\n");
	}

	@Override
	public void startWriterAndReader() {
		// TODO Auto-generated method stub
		System.out.printf("-- 启动Packet读写线程\n");
	}

	@Override
	public void connectionSuccessful() {
		// TODO Auto-generated method stub
		System.out.printf("-- 连接成功  type: %s host: %s\n", client.getConnectType(), client.getConfiguration().getHost());
	}

	@Override
	public void reconnectStart() {
		// TODO Auto-generated method stub
		System.out.printf("-- 开始重连\n");
	}

}
