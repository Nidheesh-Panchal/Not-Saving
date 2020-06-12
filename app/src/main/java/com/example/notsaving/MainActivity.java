package com.example.notsaving;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

	private LinearLayout noti_list;

	BroadcastReceiver updateUIReciver;

	Intent mServiceIntent;
	private NotService mYourService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		verifyStoragePermissions(this);

		noti_list=findViewById(R.id.noti_list);

		String notificationListenerString = Settings.Secure.getString(this.getContentResolver(),"enabled_notification_listeners");
		if (notificationListenerString == null || !notificationListenerString.contains(getPackageName())) {
			Log.d("notsave", "Notification access: NO (Requesting permission)");
			requestPermission();
		}
		else {
			Log.d("notsave", "Notification access: YES");
		}

		Log.d("notsave","Starting service");
		mYourService = new NotService();
		Log.d("notsave","Starting intent");
		mServiceIntent = new Intent(this, mYourService.getClass());
		Log.d("notsave","Checking service status");
		if (!isMyServiceRunning(mYourService.getClass())) {
			startService(mServiceIntent);
		}

		/*Intent broadcastIntent = new Intent();
		broadcastIntent.setAction("startservice");
		broadcastIntent.setClass(this, NotService.class);
		this.sendBroadcast(broadcastIntent);*/

//		IntentFilter filter = new IntentFilter();
//		filter.addAction("com.example.notsaving");
		/*updateUIReciver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				*//*Log.d("notsave","receiving");
				String title=intent.getStringExtra("title");
//				Log.d("notsave","Title: "+title);
				String text=intent.getStringExtra("text");
//				Log.d("notsave","Text: "+text);
				TextView tv = new TextView(MainActivity.this);
				tv.setText("Title: "+title+"\nText: "+text+"\n");
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT
				);
				tv.setLayoutParams(params);
				noti_list.addView(tv);*//*
				*//*for(int i=0;i<5;i++)
				{

					TextView textView = new TextView(MainActivity.this);
					textView.setText("just added");

					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.MATCH_PARENT
					);
					textView.setLayoutParams(params);
					noti_list.addView(textView);
				}*//*
			}
		};
		registerReceiver(updateUIReciver,filter);*/
	}

	private boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				Log.d ("notsave", "Service running: YES");
				return true;
			}
		}
		Log.d ("notsave", "Service running: NO");
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		unregisterReceiver(updateUIReciver);
		/*Intent broadcastIntent = new Intent();
		broadcastIntent.setAction("restartservice");
		broadcastIntent.setClass(this, NotService.class);
		this.sendBroadcast(broadcastIntent);*/
		/*IntentFilter filter = new IntentFilter();
		filter.addAction("com.example.notsaving");*/
	}

	public void requestPermission() {
		Intent requestIntent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
		requestIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(requestIntent);
	}

	public static void verifyStoragePermissions(Activity activity) {
		int REQUEST_EXTERNAL_STORAGE = 1;
		String[] PERMISSIONS_STORAGE = {
				Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.WRITE_EXTERNAL_STORAGE
		};
		// Check if we have write permission
		int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

		if (permission != PackageManager.PERMISSION_GRANTED) {
			// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(
					activity,
					PERMISSIONS_STORAGE,
					REQUEST_EXTERNAL_STORAGE
			);
		}
	}
}