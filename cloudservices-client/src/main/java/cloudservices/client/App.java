package cloudservices.client;

//import org.apache.log4j.Logger;

import java.io.IOException;

import cloudservices.client.packets.TextPacket;

public class App {
	//private static Logger logger = Logger.getLogger(App.class);
	protected static final String SERVER_IP = "172.21.4.64";
	protected static final int MQTT_PORT = 1883;
	protected static final String SERVER_URL = "http://"+SERVER_IP+":8080/cloudservices-web";
	protected static final String RECEIVE_URL = SERVER_URL + "/api/receive";
	protected static final String SEND_URL = SERVER_URL + "/api/send";
	protected static final String CONNECT_URL = SERVER_URL + "/api/connect";
	
	protected static final String TOPIC = "beidou";
	protected static final String DEFAULT_PASSWORD = "kk-xtd-push";
	
	public static String getReceiveUrl(String ip, int port) {
		return String.format("http://%s:%d/cloudservices-web/api/receive", ip, port);
	}
	public static String getSendUrl(String ip, int port) {
		return String.format("http://%s:%d/cloudservices-web/api/send", ip, port);
	}
	public static String getConnectUrl(String ip, int port) {
		return String.format("http://%s:%d/cloudservices-web/api/connect", ip, port);
	}
	
	/**
	 * 
	 * @param args  <br/>
	 * 	0 -- http(1) or mqtt(2)<br/>
	 * 	1 -- username 用户名<br/>
	 * 	2 -- sendto 发送到的用户<br/>
	 *  3 -- period 时间间隔,毫秒<br/>
	 *  4 -- SERVER IP 服务器IP 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		if (args.length < 4) {
			return;
		}
		//System.in.read();System.in.read();
		String ip = "";
		if (args.length == 5)
			ip = args[4];
		else 
			ip = SERVER_IP;
		int circle = Integer.parseInt(args[3]);
		ClientConfiguration config = new ClientConfiguration(ip, MQTT_PORT);
		config.setUsername(args[1]);
		config.setPassword(DEFAULT_PASSWORD);
		config.setTopic(TOPIC);
		config.setSendUrl(getSendUrl(ip, 8080));
		config.setReceiveUrl(getReceiveUrl(ip, 8080));
		config.setConnectUrl(getConnectUrl(ip, 8080));
		config.setConnectType(Integer.parseInt(args[0]));
		config.setHttpCircle(circle);
		
		//System.in.read();System.in.read();
		ClientService client = ClientService.getInstance();
		try {
			client.config(config);
			//client.startup();
			client.connect();
			System.out.println("connect success");
		} catch (ConfigException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
			System.out.println(e1.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		int i = 0;
		String[] tos = {};
		if (!args[2].equals("null"))
			tos = args[2].split(",");
		//System.in.read();System.in.read();
		while (true) {
			// client.sendPacket(new Packet());
			try {
				i++;
				Thread.sleep(circle*1000);
				for (int j = 0; j < tos.length; j++) {
					TextPacket t = new TextPacket();
					t.setText(String.format("--%d--", i));
					client.sendPacket(t, "beidou/"+tos[j]);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//logger.error("发送消息异常", e);
				break;
			}
		}
		//System.in.read();System.in.read();
	}

}
