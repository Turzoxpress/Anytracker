package com.lonetiger.onetracker.Other.Services;

/**
 * Created by Turzo on 22-Jun-18.
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.lonetiger.onetracker.Other.Constants;
import com.lonetiger.onetracker.R;
import com.lonetiger.onetracker.View.MainActivity;


public class ForegroundService extends Service {
  private static final String LOG_TAG = "ForegroundService";

  PendingIntent pendingIntent;

  //Timer timer = new Timer();
  //TimerTask updateProfile = new CustomTimerTask(ForegroundService.this);


  @Override
  public void onCreate() {
    super.onCreate();


  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
      Log.i(LOG_TAG, "Received Start Foreground Intent ");

      Intent notificationIntent = new Intent(this, MainActivity.class);
      notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
      notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
              | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      pendingIntent = PendingIntent.getActivity(this, 0,
              notificationIntent, 0);

      Intent previousIntent = new Intent(this, ForegroundService.class);
      previousIntent.setAction(Constants.ACTION.PREV_ACTION);
      PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
              previousIntent, 0);

      Intent playIntent = new Intent(this, ForegroundService.class);
      playIntent.setAction(Constants.ACTION.PLAY_ACTION);
      PendingIntent pplayIntent = PendingIntent.getService(this, 0,
              playIntent, 0);

      Intent nextIntent = new Intent(this, ForegroundService.class);
      nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
      PendingIntent pnextIntent = PendingIntent.getService(this, 0,
              nextIntent, 0);

      Bitmap icon = BitmapFactory.decodeResource(getResources(),
              R.drawable.ic_launcher_background);






      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        startMyOwnForeground();
      } else{

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Your position is updating")
                .setTicker("Your position is updating")
                .setContentText("Interval : 1 minute")
                .setSmallIcon(R.drawable.ic_location)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                //.addAction(android.R.drawable.ic_media_previous,
                //   "Previous", ppreviousIntent)
                //.addAction(android.R.drawable.ic_media_play, "Play",
                //   pplayIntent)
                // .addAction(R.drawable.log_out, "Log out",pnextIntent)
                .build();
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                notification);
      }


      //-------

      // timer.scheduleAtFixedRate(updateProfile, 0, 10000);

      startService(new Intent(getApplicationContext(), LocationService.class));
      //------------
    } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
      Log.i(LOG_TAG, "Stopped the service from notification tray...");

      //timer.cancel();
      stopService(new Intent(getApplicationContext(), LocationService.class));

      stopSelf();


    } else if (intent.getAction().equals(
            Constants.ACTION.STOPFOREGROUND_ACTION)) {
      Log.i(LOG_TAG, "Received Stop Foreground Intent");

      stopService(new Intent(getApplicationContext(), LocationService.class));

      stopForeground(true);


      //timer.cancel();
      stopSelf();
    }
    return START_STICKY;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.i(LOG_TAG, "In onDestroy");
  }

  @Override
  public IBinder onBind(Intent intent) {
    // Used only in case of bound services.
    return null;
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  private void startMyOwnForeground(){
    String NOTIFICATION_CHANNEL_ID = "com.lonetiger.onetracker";
    String channelName = "Your position is updating";
    NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
    chan.setLightColor(Color.BLUE);
    chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    assert manager != null;
    manager.createNotificationChannel(chan);

    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);



    Notification notification =notificationBuilder.setOngoing(true)
            .setContentTitle("Your position is updating")
            .setTicker("Your position is updating")
            .setContentText("Interval : 1 minute")
            .setSmallIcon(R.drawable.ic_location)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            //.addAction(android.R.drawable.ic_media_previous,
            //   "Previous", ppreviousIntent)
            //.addAction(android.R.drawable.ic_media_play, "Play",
            //   pplayIntent)
            // .addAction(R.drawable.log_out, "Log out",pnextIntent)
            .build();
    startForeground(2, notification);
  }


}
