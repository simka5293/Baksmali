package com.mycompany.utils;

import android.content.Context;
import android.widget.Toast;

public class QuickToast 
{
	Toast quick;
	int duration;
	public QuickToast(Context context, CharSequence message) 
	{
		// TODO Auto-generated constructor stub

		duration = Toast.LENGTH_SHORT;

		quick = Toast.makeText(context, message, duration);
		quick.show();
	}
	public QuickToast(Context context, CharSequence message, String how) 
	{
		// TODO Auto-generated constructor stub
		if (how.equals("long"))
			duration = Toast.LENGTH_LONG;
		else 
			duration = Toast.LENGTH_SHORT;

		quick = Toast.makeText(context, message, duration);
		quick.show();
	}
}
