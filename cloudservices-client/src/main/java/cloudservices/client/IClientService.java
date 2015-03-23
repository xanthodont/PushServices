package cloudservices.client;

public interface IClientService {
	void config();
	
	void startService();
	
	void shutdown();
	
	void sendMessage();
	
	void receive();
}
