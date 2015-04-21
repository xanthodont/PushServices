package com.xtd.cloudservices.android;

import cloudservices.client.ClientConfiguration;
import cloudservices.client.ClientService;
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
	private Button btnSubscribe;
	private Button btnUnSubscribe;
	private EditText etPushTopic;
	private EditText etPushMessage;
	private EditText etPubTopic;
	private EditText etPubMessage;
	private Button btnPublish;
	private ClientService client;

	private TextView tvReceiveMsg;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        etHost = (EditText) findViewById(R.id.etHost);
		etPort = (EditText) findViewById(R.id.etPort);
		btnConnect = (Button) findViewById(R.id.btnConnect);
		btnDisconnect = (Button) findViewById(R.id.btnDisconnect);
		etTopic = (EditText) findViewById(R.id.etTopic);
		btnSubscribe = (Button) findViewById(R.id.btnSubscribe);
		btnUnSubscribe = (Button) findViewById(R.id.btnUnSubscribe);
		etPubTopic = (EditText) findViewById(R.id.etPushTopic);
		etPubMessage = (EditText) findViewById(R.id.etPushMessage); 
		btnPublish = (Button) findViewById(R.id.btnPublish);
		tvReceiveMsg = (TextView) findViewById(R.id.tvReceiveMessage);
		
		String host = etHost.getText().toString();
		int port = Integer.parseInt(etPort.getText().toString());
		Toast.makeText(MainActivity.this, String.format("Host:%s Port:%d", host, port), Toast.LENGTH_LONG).show();
		ClientConfiguration config = new ClientConfiguration(host, port);
		config.setResourceName("android");
		config.setUsername("12345678901234");
		
		client = ClientService.getInstance();
				
		
		btnConnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
					
				
			}
		});
		btnDisconnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		btnSubscribe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		
    }

	

}
