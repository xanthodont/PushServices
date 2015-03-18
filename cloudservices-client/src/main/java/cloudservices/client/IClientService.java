package cloudservices.client;

public interface IClientService {
	void config();
	
	void startService();
	
	void shutdownService();
	
	void subscribe();
	
	void publish();
}
