package com.example.notsaving;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class Restarter extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("notsave", "Broadcast received");
//		Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();
		context.stopService(intent);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			context.startForegroundService(new Intent(context, NotService.class));
		} else {
			context.startService(new Intent(context, NotService.class));
		}
	}
}
