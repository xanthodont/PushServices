package com.xtd.cloudservices.android.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cloudservices.client.ClientConfiguration;
import cloudservices.client.ClientService;
import cloudservices.client.ConfigException;
import cloudservices.client.ConnectException;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class CloudService extends Service {
	// Identifier for Intents, log messages, etc..
	static final String TAG = "CloudService";

	// callback id for making trace callbacks to the Activity
	// needs to be set by the activity as appropriate
	//private String traceCallbackId;
	// state of tracing
	//private boolean traceEnabled = false;

	// somewhere to persist received messages until we're sure
	// that they've reached the application
	/** 消息持久化存储 */
	//MessageStore messageStore;

	// An intent receiver to deal with changes in network connectivity
	private NetworkConnectionIntentReceiver networkConnectionMonitor;

	// a way to pass ourself back to the activity
	private CloudServiceBinder mqttServiceBinder;

	// mapping from client handle strings to actual client connections.
	// private Map<String/* clientHandle */, MqttConnection/* client */>
	// connections = new ConcurrentHashMap<String, MqttConnection>();
	DataManager dataManager;
	ClientConfiguration config;
	ClientService client;

	public CloudService() {
		super();
	}
	
	/**
	   * @see android.app.Service#onCreate()
	   */
	@Override
	public void onCreate() {
		super.onCreate();
		
		// 载入配置信息
		dataManager = DataManager.getInstance();
		dataManager.config(this);
		// 配置clientService 
		
		config = new ClientConfiguration();
		config.setConnectType(dataManager.getConnectType());
		config.setUsername(dataManager.getUsername());
		config.setPassword(dataManager.getPassword());
		config.setHost(dataManager.getHost());
		config.setPort(dataManager.getPort());
		config.setTopic(dataManager.getBaseTopic());
		config.setReceiveUrl(dataManager.getReceiveUrl());
		config.setSendUrl(dataManager.getSendUrl());
		config.setConnectUrl(dataManager.getConnectUrl());
		config.setBufferSize(dataManager.getBufferSize());
		
		client = ClientService.getInstance();
		try {
			client.config(config);
		} catch (ConfigException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.startup();
		// create a binder that will let the Activity UI send
		// commands to the Service
		// mqttServiceBinder = new MqttServiceBinder(this);

		// create somewhere to buffer received messages until
		// we know that they have been passed to the application
		// messageStore = new DatabaseMessageStore(this, this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// What we pass back to the Activity on binding -
	    // a reference to ourself, and the activityToken
	    // we were given when started
	    String activityToken = null;//intent.getStringExtra(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN);
	    mqttServiceBinder.setActivityToken(activityToken);
	    return mqttServiceBinder;
	}
	
	/**
	 * @see android.app.Service#onStartCommand(Intent,int,int)
	 */
	@Override
	public int onStartCommand(final Intent intent, int flags, final int startId) {
		// 开启连接
		try {
			client.connect();
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// run till explicitly stopped, restart when
		// process restarted
		registerBroadcastReceivers();

		return START_STICKY;
	}
	
	/**
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// disconnect immediately
		client.shutdown();
		
		// clear down
		if (mqttServiceBinder != null) {
			mqttServiceBinder = null;
		}

		unregisterBroadcastReceivers();

		//if (this.messageStore != null)
			//this.messageStore.close();

		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	private void registerBroadcastReceivers() {
		if (networkConnectionMonitor == null) {
			networkConnectionMonitor = new NetworkConnectionIntentReceiver();
			registerReceiver(networkConnectionMonitor, new IntentFilter(
					ConnectivityManager.CONNECTIVITY_ACTION));
		}
	}

	private void unregisterBroadcastReceivers() {
		if (networkConnectionMonitor != null) {
			unregisterReceiver(networkConnectionMonitor);
			networkConnectionMonitor = null;
		}
	}

	/*
	 * Called in response to a change in network connection - after losing a
	 * connection to the server, this allows us to wait until we have a usable
	 * data connection again
	 */
	private class NetworkConnectionIntentReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// traceDebug(TAG, "Internal network status receive.");
			// we protect against the phone switching off
			// by requesting a wake lock - we request the minimum possible wake
			// lock - just enough to keep the CPU running until we've finished
			PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
			WakeLock wl = pm
					.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MQTT");
			wl.acquire();
			// traceDebug(TAG,"Reconnect for Network recovery.");
			if (false/* isOnline() */) {
				// traceDebug(TAG,"Online,reconnect.");
				// we have an internet connection - have another try at
				// connecting
				// reconnect();
			} else {
				// notifyClientsOffline();
			}

			wl.release();
		}
	}
}
