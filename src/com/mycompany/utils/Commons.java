package com.mycompany.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

public class Commons 
{
	public static final String TAG = "WebSourceDownloader";
	public static final String URL_KEY = "url";
	public static final String URL_PATTERN = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	public static final String EVENT_NAME = "DownloadResult";
	public static final String SD_DIR = "websource";

	public static boolean canAccess(Context context)
	{
		try
		{
			ConnectivityManager con=(ConnectivityManager)context.getSystemService(Activity.CONNECTIVITY_SERVICE);
			try
			{
				return con.getActiveNetworkInfo().isConnectedOrConnecting();
			}
			catch (Exception e)
			{
			    boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
			    boolean mobile=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
			    if (wifi || mobile)
				{
			    	return true;	        
			    }
				return false;
			}
		}
		catch (Exception e)
		{
			return false;
		}
	}
	public static String getStatusText(int status)
	{
		switch (status)
		{
			case 200:
				return "OK";
			case 301:
				return "Moved Permanently";
			case 400:
				return "Bad Request";
			case 401:
				return "Unauthorized";
			case 403:
				return "Forbidden";
			case 404:
				return "Not Found";
			case 407:
				return "Proxy Authentication Required";
			default:
				return "Other";
		}
	}
}
