package com.example.notsaving;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

	private LinearLayout noti_list;

	BroadcastReceiver updateUIReciver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		verifyStoragePermissions(this);

		noti_list=findViewById(R.id.noti_list);

		String notificationListenerString = Settings.Secure.getString(this.getContentResolver(),"enabled_notification_listeners");
		if (notificationListenerString == null || !notificationListenerString.contains(getPackageName())) {
			Log.d("notsave", "no access");
			requestPermission();
		}
		else {
			Log.d("notsave", "has access");
		}

		IntentFilter filter = new IntentFilter();
		filter.addAction("com.example.notsaving");
		updateUIReciver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Log.d("notsave","receiving");
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
				noti_list.addView(tv);
				/*for(int i=0;i<5;i++)
				{

					TextView textView = new TextView(MainActivity.this);
					textView.setText("just added");

					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.MATCH_PARENT
					);
					textView.setLayoutParams(params);
					noti_list.addView(textView);
				}*/
			}
		};
		registerReceiver(updateUIReciver,filter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(updateUIReciver);
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