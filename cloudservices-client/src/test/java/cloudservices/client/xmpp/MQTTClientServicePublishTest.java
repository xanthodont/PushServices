package cloudservices.client.xmpp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.dna.mqtt.moquette.PublishException;

import cloudservices.client.ClientConfiguration;
import cloudservices.client.mqtt.IPublishCallback;
import cloudservices.client.mqtt.MQTTClientService;

public class MQTTClientServicePublishTest {
	public final static int MQTT_PORT = 1883; 

	public static void main(String[] args) throws PublishException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		ClientConfiguration config = new ClientConfiguration("172.21.4.64", MQTT_PORT);
		config.setResourceName("android");
		config.setUsername("12345678901235");
		
		final MQTTClientService client = new MQTTClientService(config);
		client.connect();
		
		final String topic = "common";
		new Thread(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				int i = 0;
				while (i < 1) {
					try {/*
						File file = new File("D://t.txt");
						FileInputStream input = new FileInputStream(file);  
				        BufferedInputStream inBuff=new BufferedInputStream(input);  
				        
				        byte[] b = new byte[1024 * 1];  
				        int len;  
				        while ((len =inBuff.read(b)) != -1) {  
				        	client.publish(topic, b);
				            //outBuff.write(b, 0, len);  
				        }
				        */  
				        byte[] b = "abcdefg".getBytes("UTF-8");
						client.publish(topic, b);
						Thread.sleep(2000);
					} catch (PublishException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						i++;
					}
				}
				client.close();

			}
		}).start();
	}
	
}
