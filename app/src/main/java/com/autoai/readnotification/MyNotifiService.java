package com.autoai.readnotification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.RemoteInput;
import android.util.Log;
import android.widget.Toast;

import com.autoai.readnotification.models.Action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@SuppressLint("OverrideAbstract")
public class MyNotifiService extends NotificationListenerService {
    private BufferedWriter bw;
    public static final String TAG = "salman";

    private SimpleDateFormat sdf;
    private MyHandler handler = new MyHandler();
    private String nMessage;
    private String data;
    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            String msgString = (String) msg.obj;
            Toast.makeText(getApplicationContext(), msgString, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("KEVIN", "Service is started" + "-----");
        data = intent.getStringExtra("data");
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Log.i(TAG, "onNotificationRemoved");

    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        //        super.onNotificationPosted(sbn);


        Log.i(TAG, "Here");

        MyNotifiService.this.cancelNotification(sbn.getKey());

        Action action = NotificationUtils.getQuickReplyAction(sbn.getNotification(), getPackageName());

        if (action != null) {
            Log.i(TAG, "success"+sbn.getPackageName());
            Log.i(TAG, "success"+sbn.getUser().toString());
            //
            Log.i("Salman--", "sender " + sbn.getNotification().extras.getString("android.title"));

            try {
                action.sendReply(getApplicationContext(), "Hello");
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }


            try {
                if (sbn.getPackageName().equalsIgnoreCase("com.whatsapp"))
                {
String num = null;

                    ContentResolver contentResolver = getContentResolver();
                    Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "=?", new String[]{sbn.getNotification().extras.getString("android.title")}, null);
                    if (cursor != null) {
                        if (cursor.getCount() > 0) {
                            if (cursor.moveToFirst()) {
                                do {

                                    num=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                } while (cursor.moveToNext());
                            }
                        }
                    }

                    if (num!=null && !num.isEmpty())
                    {
                        action.sendReply(getApplicationContext(), "Hello");

                    }


                }
            } catch (PendingIntent.CanceledException e) {
                Log.i(TAG, "CRAP " + e.toString());
            }
        } else {
            Log.i(TAG, "not success");
        }


//        Log.i("Salman", "package name " + sbn.getPackageName());
//        Log.i("Salman", "phone number " + sbn.getKey());  // extract from it
//        Log.i("Salman", "sender " + sbn.getNotification().extras.getString("android.title"));
//        Log.i("Salman", "text " + sbn.getNotification().extras.get("android.text"));


//        Log.i("Salman", "extras " + sbn.getNotification().extras.getString("android.title"));

//        Log.i("Salman", "Salman " + sbn.getNotification().tickerText);
//        Log.i("Salman", "Salman " + sbn.getNotification().tickerText);
//        Log.i("Salman", "Salman " + sbn.getNotification().tickerText);
//        Log.i("Salman", "Salman " + sbn.getNotification().tickerText);
//        Log.i("Salman", "Salman " + sbn.getNotification().tickerText);
//        Log.i("Salman", "Salman " + sbn.getNotification().tickerText);
//        Log.i("Salman", "Salman " + sbn.getNotification().tickerText);
//        Log.i("Salman", "Salman " + sbn.getNotification().tickerText);
//        Log.i("Salman", "Salman " + sbn.getNotification().tickerText);

//        try {
//            //
//            //Some notifications can't parse the TEXT content. Here is a message to judge.
//            if (sbn.getNotification().tickerText != null) {
//                SharedPreferences sp = getSharedPreferences("msg", MODE_PRIVATE);
//                nMessage = sbn.getNotification().tickerText.toString();
//                Log.e("KEVIN", "Get Message" + "-----" + nMessage);
//                sp.edit().putString("getMsg", nMessage).apply();
//                Message obtain = Message.obtain();
//                obtain.obj = nMessage;
//                mHandler.sendMessage(obtain);
//                init();
//                if (nMessage.contains(data)) {
//                    Message message = handler.obtainMessage();
//                    message.what = 1;
//                    handler.sendMessage(message);
//                    writeData(sdf.format(new Date(System.currentTimeMillis())) + ":" + nMessage);
//                }
//            }
//        } catch (Exception e) {
//            Toast.makeText(MyNotifiService.this, "Unresolvable notification", Toast.LENGTH_SHORT).show();
//        }

    }

    private void writeData(String str) {
        try {
//            bw.newLine();
//            bw.write("NOTE");
            bw.newLine();
            bw.write(str);
            bw.newLine();
//            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            FileOutputStream fos = new FileOutputStream(newFile(), true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);
        } catch (IOException e) {
            Log.d("KEVIN", "BufferedWriter Initialization error");
        }
        Log.d("KEVIN", "Initialization Successful");
    }

    private File newFile() {
        File fileDir = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "ANotification");
        fileDir.mkdir();
        String basePath = Environment.getExternalStorageDirectory() + File.separator + "ANotification" + File.separator + "record.txt";
        return new File(basePath);

    }


    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
//                    Toast.makeText(MyService.this,"Bingo",Toast.LENGTH_SHORT).show();




            }
        }
    }
}