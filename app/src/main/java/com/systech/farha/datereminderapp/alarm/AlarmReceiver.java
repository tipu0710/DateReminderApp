package com.systech.farha.datereminderapp.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.systech.farha.datereminderapp.R;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //String Title = intent.getStringExtra("title");
        Intent x = new Intent(context, Alert.class);
        //x.putExtra("title", Title);
        x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.v("alarm", "start");
        context.startActivity(x);
    }
}
