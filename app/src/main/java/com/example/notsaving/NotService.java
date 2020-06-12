package com.example.notsaving;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class NotService extends NotificationListenerService {

	int counter=0;
	String filePath;
	String fileName;
	File f;
//	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/NotSaving";
	String baseDir;

	@Override
	public void onCreate()
	{
		super.onCreate();
		baseDir = getExternalFilesDir(null)+File.separator+"NotSaving";
		startTimer();
		Log.d("notsave","Timer started onCreate");
		Log.d("notsave","created service");
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
			startMyOwnForeground();
//		else
//			startForeground(1, new Notification());
	}

	@RequiresApi(Build.VERSION_CODES.O)
	private void startMyOwnForeground()
	{
		String NOTIFICATION_CHANNEL_ID = "example.notsaving";
		String channelName = "Background Service";
		NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
		chan.setLightColor(Color.BLUE);
		chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		assert manager != null;
		manager.createNotificationChannel(chan);

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
		Notification notification = notificationBuilder.setOngoing(true)
//				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle("App is running in background")
				.setPriority(NotificationManager.IMPORTANCE_MAX)
				.setCategory(Notification.CATEGORY_SERVICE)
				.build();
		startForeground(2, notification);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		startTimer();
		Log.d("notsave","Timer started onStartCommand");
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
			startMyOwnForeground();
//		else
//			startForeground(1, new Notification());
		return START_STICKY;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Log.d("notsave","onDestroy");
		stoptimertask();
		Log.d("notsave","stopped timer");
		restartservice();
	}

	private Timer timer;
	private TimerTask timerTask;
	public void startTimer() {
		timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				Log.d("notsave","Scheduled");
				restartservice();
			}
		};
		/*{
			public void run() {
//				Log.d("notsave", "=========  "+ (counter++));
				Intent broadcastIntent = new Intent();
				broadcastIntent.setAction("restartservice");
				broadcastIntent.setClass(mcontext, NotService.class);
				this.sendBroadcast(broadcastIntent);
			}

		};*/
		timer.schedule(timerTask, 300000, 300000); //
	}

	public void restartservice()
	{
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction("restartservice");
		broadcastIntent.setClass(this, Restarter.class);
		this.sendBroadcast(broadcastIntent);
	}

	public void stoptimertask() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	@Override
	public void onNotificationPosted(StatusBarNotification sbn)
	{
		Log.d("notsave","got notification");
		String pakagename=sbn.getPackageName();
		Log.d("notsave",sbn.getPackageName());

		if(pakagename.equals("com.whatsapp"))
		{
			filePath = baseDir + File.separator + "WhatsApp";
			File createFolder=new File(filePath);
			createFolder.mkdirs();
			Log.d("notsave","creating folders");
			String temp= DateFormat.getDateTimeInstance().format(new Date()).substring(0,11);
			fileName = temp+".txt";
			filePath += File.separator;
			f = new File(filePath,fileName);
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				Log.println(Log.INFO,"notsave","unable to create file");
			}

			Bundle extras=sbn.getNotification().extras;
			//ArrayList<String> noti=new ArrayList<>();

			String title=extras.getCharSequence("android.title").toString();
			String text=extras.getCharSequence("android.text").toString();
			if(title.substring(0,8).equals("WhatsApp"))
			{
				return;
			}

			if(text.matches(".* messages from .* chats"))
			{
				return;
			}

			long millis = sbn.getNotification().when;

			if(System.currentTimeMillis() - millis > 3000 ){
				return;
			}

			Date date=new Date(millis);

			SimpleDateFormat format=new SimpleDateFormat("hh:mm a");
//			Date today= Calendar.getInstance().getTime();
			String time = format.format(date);

			Log.d("notsave","Time : " + time);

			/*noti.add(title);
			noti.add(text);*/

			//Broadcast
			/*Intent local = new Intent();
			local.setAction("com.example.notsaving");
			local.putExtra("title",title);
			local.putExtra("text",text);
			this.sendBroadcast(local);*/

			f=new File(filePath,fileName);
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(f,true)));
				pw.println("");
				pw.println("Title : " + title);
				pw.println("Text : " + text);
				pw.println("Time : " + time);
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.d("notsave","saving into file");
		}

		else if(pakagename.equals("com.instagram.android"))
		{
			filePath = baseDir + File.separator + "Instagram";
			File createFolder=new File(filePath);
			createFolder.mkdirs();
			Log.d("notsave","creating folders");
			String temp= DateFormat.getDateTimeInstance().format(new Date()).substring(0,11);
			fileName = temp+".txt";
			filePath += File.separator;
			f = new File(filePath,fileName);
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				Log.println(Log.INFO,"notsave","unable to create file");
			}

			Bundle extras=sbn.getNotification().extras;
			//ArrayList<String> noti=new ArrayList<>();
			String title=extras.getCharSequence("android.title").toString();
			String text=extras.getCharSequence("android.text").toString();

			if(text.endsWith("liked your post."))
			{
				return;
			}

			long millis = sbn.getNotification().when;
			Date date=new Date(millis);

			SimpleDateFormat format=new SimpleDateFormat("hh:mm a");
//			Date today= Calendar.getInstance().getTime();
			String time = format.format(date);

			Log.d("notsave","Time : " + time);
			/*noti.add(title);
			noti.add(text);*/

			//Broadcast
			/*Intent local = new Intent();
			local.setAction("com.example.notsaving");
			local.putExtra("title",title);
			local.putExtra("text",text);
			this.sendBroadcast(local);*/

			f=new File(filePath,fileName);
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(f,true)));
				pw.println("");
				pw.println("Title : " + title);
				pw.println("Text : " + text);
				pw.println("Time : " + time);
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.d("notsave","saving into file");
		}
		/*Set<String> keys = extras.keySet();
		Iterator<String> it = keys.iterator();
		Log.d("notsave","Dumping Intent start");
		while (it.hasNext()) {
			String key = it.next();
			Log.d("notsave","[" + key + "=" + extras.get(key)+"]");
		}*/
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn)
	{
		Log.d("notsave","notification removed");
	}
}
