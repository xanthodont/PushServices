package cloudservices.client.http;

import cloudservices.client.ClientConfiguration;
import cloudservices.client.ClientService;
import cloudservices.client.ConfigException;
import cloudservices.client.ConnectException;
import cloudservices.client.ISender;
import cloudservices.client.packets.Packet;
import cloudservices.utils.StringUtil;

public class HTTPClientService implements ISender {
	private ClientService clientService;
	//private AsyncHttpConnection http;
	private String sendUrl = "";
	private String receiveUrl = "";

	public HTTPClientService(ClientService clientService) {
		// TODO Auto-generated constructor stub
		this.clientService = clientService;
	}

	public void config(ClientConfiguration config) throws ConfigException {
		// TODO Auto-generated method stub
		if (StringUtil.isEmpty(config.getSendUrl())) throw new ConfigException("未设置Http发送消息的接口地址");
		sendUrl = config.getSendUrl();
		if (StringUtil.isEmpty(config.getReceiveUrl())) throw new ConfigException("未设置Http接收消息的接口地址");
		receiveUrl = config.getReceiveUrl();
	}

	@Override
	public void send(Packet packet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connect() throws ConnectException {
		// TODO Auto-generated method stub
		
	}

}
