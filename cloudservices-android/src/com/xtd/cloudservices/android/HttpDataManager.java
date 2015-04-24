package com.xtd.cloudservices.android;

import java.util.Properties;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class HttpDataManager {
	private static final String TAG = "DataManager";
	public static String SERVER_URL = "";
	public static final String SHARED_PREFERENCE_NAME = "cloudservices_preferences";
	/** 用户名 */
    public static final String USERNAME = "USERNAME";
    /** 密码 */
    public static final String PASSWORD = "PASSWORD";
    /** 轮询周期 */
    public static final String HTTP_CIRCLE = "HTTP_ALARM_CIRCLE";
    
	public static final String ACTION_NAME = "RRECIVER";
	public static final String ALARM_ACTION_NAME = "com.konka.push.HTTP_ALARM_SERVICE_RRECIVER";
	
	
	private SharedPreferences sharedPrefs;
	private Context context;
	private Properties props;
	
	private static Object flag = new Object();
	private static HttpDataManager instance;
	/** */
	public static boolean ALARM_STATUS = false;  
	private HttpDataManager() {
	}
	public static HttpDataManager singleInstance() {
		if (instance == null) {
			synchronized (flag) {
				if (instance == null) {
					instance = new HttpDataManager();
				}
			}
		}
		return instance;
	}
	
	public void setContext(Context context) {
		this.context = context;
		sharedPrefs = this.context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		props = loadProperties();
		SERVER_URL = props.getProperty("ServerUrl");
	}
	
	private SharedPreferences getSharedPreferences() {
		if (sharedPrefs == null) {
			this.context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		}
		return sharedPrefs;
	}
	
	public void setDataMessage(String userName,String password,String systemVersion,String otherParam,String model) {
		Editor editor = getSharedPreferences().edit();
        editor.commit();
	}
	
	
	/**
	 * 
	 * @return
	 */
	private Properties loadProperties() {
        Properties props = new Properties();
        try {
            int id = context.getResources().getIdentifier("cloudservices", "raw", context.getPackageName());
            props.load(context.getResources().openRawResource(id));
        } 
        catch (Exception e) {
            //Log.e(TAG, "", e);
        }
        return props;
    }

	
}
