package com.example.hp.dsnotes;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import java.util.Set;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Setbroadcast extends BroadcastReceiver implements MediaPlayer.OnPreparedListener {

    private final String CHANNEL_ID = "Primary Notifications";
    private final int Notification_id = 001;
        MediaPlayer mediaPlayer;
    String msg=null;

    Context context;

    PowerManager.WakeLock wakelock;


    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        msg=intent.getStringExtra("notifi");
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakelock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getCanonicalName());
        wakelock.acquire();
        notifi(context);
        playMusic(0);
        wakelock.release();
    }

    private void notifi(Context context) {

            //CREATE INTENT
            Intent intent = new Intent(context.getApplicationContext(), Setbroadcast.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //END OF INTENT

            //CREATE A PENDING INTENT

            PendingIntent pendingIntent = PendingIntent.getActivities(context.getApplicationContext(), 0, new Intent[]{intent}, PendingIntent.FLAG_ONE_SHOT);
            //END OF PENDING INTENT
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID);
            builder.setSmallIcon(R.drawable.smallicon);
            String s = msg;

            builder.setContentTitle("Ds notes");
            builder.setContentText(s);
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

            builder.setAutoCancel(true);
            builder.setContentIntent(pendingIntent);


            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context.getApplicationContext());
            notificationManagerCompat.notify(Notification_id, builder.build());

    }

    private void notificationChannel(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            CharSequence name = "Ds notes";
            String s = msg;

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            // create notification channel object
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(s);

            //create notification manager object which hold notification channel

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel); // create notification channel through manager object
        }

    }

    private void playMusic(int i) {
        if (mediaPlayer != null) mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(context.getApplicationContext(), R.raw.notification);
            mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        });

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }
}
