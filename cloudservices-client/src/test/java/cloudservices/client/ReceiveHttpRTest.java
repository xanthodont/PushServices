package cloudservices.client;

public class ReceiveHttpRTest extends TestBase{

	public static void main(String[] args) throws ConfigException, ConnectException, InterruptedException {
		// TODO Auto-generated method stub
		ClientConfiguration config = getInitConfig();
		config.setUsername("HR");
		config.setConnectType(ClientConfiguration.SHORT_HTTP);
		config.setHttpCircle(2);
		config.setBufferSize(2000); // 测试
		
		ClientService client = ClientService.getInstance();
		client.config(config);
		//client.startup();
		client.connect();

		
		//client.shutdown();
		
		while (true) {
			Thread.sleep(20000);
		}
	}

}
