package com.example.notsaving;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class NotService extends NotificationListenerService {

	String filePath;
	String fileName;
	File f;
	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/NotSaving";

	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.d("notsave","created service");
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public void onNotificationPosted(StatusBarNotification sbn)
	{
		Log.d("notsave","got notification");
		String pakagename=sbn.getPackageName();
//		Log.d("notsave",sbn.getPackageName());

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
			/*noti.add(title);
			noti.add(text);*/

			//Broadcast
			Intent local = new Intent();
			local.setAction("com.example.notsaving");
			local.putExtra("title",title);
			local.putExtra("text",text);
			this.sendBroadcast(local);

			f=new File(filePath,fileName);
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(f,true)));
				pw.println("");
				pw.println("Title : "+title);
				pw.println("Text : "+text);
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
			/*noti.add(title);
			noti.add(text);*/

			//Broadcast
			Intent local = new Intent();
			local.setAction("com.example.notsaving");
			local.putExtra("title",title);
			local.putExtra("text",text);
			this.sendBroadcast(local);

			f=new File(filePath,fileName);
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(f,true)));
				pw.println("");
				pw.println("Title : "+title);
				pw.println("Text : "+text);
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
