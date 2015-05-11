package org.dna.mqtt.moquette.messaging.spi.impl.subscriptions;

public class SubConstants {
	public static final String SUB_PREFIX = "sub-";
	
	public static String getSubKey(String username) {
		return SUB_PREFIX + username;
	}
	public static byte[] getSubPrefixParttenBytes() {
		return (SUB_PREFIX + "*").getBytes();
	}
	public static byte[] getSubKeyBytes(String username) {
		return getSubKey(username).getBytes();
	}
}
