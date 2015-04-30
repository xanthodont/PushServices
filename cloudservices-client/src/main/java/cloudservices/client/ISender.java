package cloudservices.client;

import cloudservices.client.packets.Packet;

public interface ISender {
	public void send(Packet packet);

	public void connect() throws ConnectException;

	public void disconnect();
}
