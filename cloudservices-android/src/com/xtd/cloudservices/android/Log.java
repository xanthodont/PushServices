package com.xtd.cloudservices.android;


public class Log
{
	private static boolean logFlag = true;
	public static void d(String tag,String msg)
	{
		if(logFlag)
		{
			android.util.Log.d(tag, msg);
		}
	}
	public static void i(String tag,String msg)
	{
		if(logFlag)
		{
			android.util.Log.i(tag, msg);
		}
	}
	public static void e(String tag,String msg,Throwable e)
	{
		if(logFlag)
		{
			android.util.Log.e(tag,msg,e);
		}
	}
	public static void e(String tag,String msg)
	{
		if(logFlag)
		{
			android.util.Log.e(tag,msg);
		}
	}

}
