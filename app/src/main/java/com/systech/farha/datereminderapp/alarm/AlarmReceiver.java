package com.systech.farha.datereminderapp.alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.activity.Others.EventActivity;
import com.systech.farha.datereminderapp.activity.Others.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {

    private String CHANNEL_ID;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotification("Check out your new event!");
        }else {
            Intent x = new Intent(context, Alert.class);
            x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.v("alarm", "start");
            context.startActivity(x);
        }

    }



    private void createNotificationChannel() {
        CharSequence channelName = CHANNEL_ID;
        String channelDesc = "channelDesc";
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDesc);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            assert notificationManager != null;
            NotificationChannel currChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (currChannel == null)
                notificationManager.createNotificationChannel(channel);
        }
    }


    public void createNotification(String message) {

        CHANNEL_ID = context.getString(R.string.app_name);
        if (message != null ) {
            createNotificationChannel();

            Intent intentGo = new Intent(context, EventActivity.class);
            intentGo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            PendingIntent pendingIntentGo = PendingIntent.getActivity(context, 556699, intentGo, 0);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("New Event!")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntentGo)
                    .setAutoCancel(true);
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            mBuilder.setSound(uri);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            int notificationId = 6654;

            notificationManager.notify(notificationId, mBuilder.build());
        }
    }
}
