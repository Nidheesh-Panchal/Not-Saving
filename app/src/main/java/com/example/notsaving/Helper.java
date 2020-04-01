package com.example.notsaving;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.TimerTask;

public class Helper extends TimerTask
{
	public static int counter = 0;
	public void run()
	{
		Log.d("notsave", "=========  "+ (counter++));
		/*Intent broadcastIntent = new Intent();
		broadcastIntent.setAction("restartservice");
		broadcastIntent.setClass(this, NotService.class);
		this.sendBroadcast(broadcastIntent);*/
	}
}