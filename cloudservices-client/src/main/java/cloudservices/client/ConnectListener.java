package cloudservices.client;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Listener;

public class ConnectListener implements Listener {

	@Override
	public void onConnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPublish(UTF8Buffer topic, Buffer body, Runnable ack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFailure(Throwable value) {
		// TODO Auto-generated method stub
		
	}

}
