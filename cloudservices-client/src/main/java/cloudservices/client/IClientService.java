package cloudservices.client;

import cloudservices.client.packets.Packet;

public interface IClientService {
	void config(ClientConfiguration config);
	
	void startup();
	
	void shutdown();
	
	void sendPacket(Packet packet);
	
	void receive();
}
