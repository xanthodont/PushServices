package cloudservices.client;

public class ReconnectTest extends TestBase {

	public static void main(String[] args) throws ConfigException, ConnectException, InterruptedException {
		// TODO Auto-generated method stub
		ClientConfiguration config = getInitConfig();
		config.setHost("112.74.85.143"); // 阿里云
		//config.setHost("172.21.4.64"); //
		config.setUsername("Reconnection");
		config.setConnectType(ClientConfiguration.SHORT_HTTP);
		//sendConfig.setBufferSize(1000); // 测试
		
		ClientService client = ClientService.getInstance();
		client.config(config);
		//client.startup();
		client.connect();

		Thread.sleep(10000);
		
		client.shutdown();
		
		Thread.sleep(10000);
		// 换短连接
		//client.getConfiguration().setConnectType(ClientConfiguration.SHORT_HTTP);
		client.reconnect();
		
		
		Thread.sleep(10000);
		//client.shutdown();
	}

}
