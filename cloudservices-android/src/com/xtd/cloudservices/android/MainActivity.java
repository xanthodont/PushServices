package com.xtd.cloudservices.android;

import cloudservices.client.ClientConfiguration;
import cloudservices.client.ClientService;
import cloudservices.client.ConfigException;
import cloudservices.client.ConnectException;
import cloudservices.client.mqtt.MQTTClientService;

import com.xtd.cloudservices.android.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;


public class MainActivity extends Activity {

private static final int MQTT_PORT = 1883;
    
	private EditText etHost;
	private EditText etPort;
	private Button btnConnect;
	private Button btnDisconnect;
	private EditText etTopic;
	private EditText etPushTopic;
	private EditText etPushMessage;
	private EditText etPubTopic;
	private EditText etPubMessage;
	private EditText etConnectType;
	private Button btnPublish;
	private ClientService client;
	private ClientConfiguration config;

	private TextView tvReceiveMsg;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        etConnectType = (EditText) findViewById(R.id.etConnectType);
        etHost = (EditText) findViewById(R.id.etHost);
		etPort = (EditText) findViewById(R.id.etPort);
		btnConnect = (Button) findViewById(R.id.btnConnect);
		btnDisconnect = (Button) findViewById(R.id.btnDisconnect);
		etPubTopic = (EditText) findViewById(R.id.etPushTopic);
		etPubMessage = (EditText) findViewById(R.id.etPushMessage); 
		btnPublish = (Button) findViewById(R.id.btnPublish);
		tvReceiveMsg = (TextView) findViewById(R.id.tvReceiveMessage);
		
		String host = etHost.getText().toString();
		int port = Integer.parseInt(etPort.getText().toString());
		Toast.makeText(MainActivity.this, String.format("Host:%s Port:%d", host, port), Toast.LENGTH_LONG).show();
		config = new ClientConfiguration();
		
		
		client = ClientService.getInstance();
		
		btnConnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String host = etHost.getText().toString();
				int port = Integer.parseInt(etPort.getText().toString());
				Toast.makeText(MainActivity.this, String.format("Host:%s Port:%d", host, port), Toast.LENGTH_LONG).show();
				config.setHost(host);
				config.setPort(port);
				//config.setResourceName("android");
				config.setUsername("android");
				config.setPassword("kk-xtd-push");
				config.setTopic("beidou");
				config.setConnectType(Integer.parseInt(etConnectType.getText().toString()));
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							client.config(config);
							client.startup();
							client.connect();
						} catch (ConfigException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							client.shutdown();
						} catch (ConnectException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							client.shutdown();
						}		
						
					}
				}).start();
			}
		});
		btnDisconnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//client.shutdown();
			}
		});
    }

	

}
