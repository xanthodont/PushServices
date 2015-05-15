package cloudservices.client;

public class ReceiveMqttRTest extends TestBase{

	public static void main(String[] args) throws ConfigException, ConnectException, InterruptedException {
		// TODO Auto-generated method stub
		ClientConfiguration config = getInitConfig();
		config.setUsername("M");
		config.setConnectType(ClientConfiguration.LONG_MQTT);
		//sendConfig.setBufferSize(1000); // 测试
		
		ClientService client = ClientService.getInstance();
		client.config(config);
		//client.startup();
		client.connect();

		while (true) {
			Thread.sleep(130000);
			//break;
		}
		//client.shutdown();
	}

}
