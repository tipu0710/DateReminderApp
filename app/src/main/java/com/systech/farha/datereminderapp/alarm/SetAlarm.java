package com.systech.farha.datereminderapp.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static com.systech.farha.datereminderapp.activity.MainActivity.PREFS_NAME;

public class SetAlarm {
    public static void SetAlarms(Context context, int month, int day, int hour, int minute) {
        Calendar calender = Calendar.getInstance();
        Calendar calender1 = Calendar.getInstance();
        calender.set(Calendar.MONTH, month);
        calender.set(Calendar.DAY_OF_MONTH, day);
        calender.set(Calendar.HOUR, hour);
        calender.set(Calendar.MINUTE, minute);
        calender.set(Calendar.SECOND, 0);

        if (month<calender1.get(Calendar.MONTH)){
            calender.set(Calendar.YEAR, (calender.get(Calendar.YEAR)+1));
        }else if (month==calender1.get(Calendar.MONTH)){
            if (day<calender1.get(Calendar.DAY_OF_MONTH)){
                calender.set(Calendar.YEAR, (calender.get(Calendar.YEAR)+1));
            }else {
                calender.set(Calendar.YEAR, (calender.get(Calendar.YEAR)));
            }
        }else {
            calender.set(Calendar.YEAR, (calender.get(Calendar.YEAR)));
        }
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int i =getPreference(context);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i, intent, 0);
        updatePreference(context,i);

        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pendingIntent);

        Log.v("times", calender.getTime().toString()+"  "+calender.getTimeInMillis());
    }

    private static int getPreference(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getInt("i", 0);
    }

    private static void updatePreference(Context context, int i) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor ed;
        ed = preferences.edit();
        ed.putInt("i",i);
        ed.apply();
    }

    public static int[] formatTime(String time){
        String[] s = time.split(":");
        int[] i= new int[3];
        i[0] = Integer.parseInt(s[0]);
        i[1] = Integer.parseInt(s[1]);

        if (i[0]>11){
            i[0] = i[0]-12;
            i[2]=0;
        }else {
            if (i[0]==0){
                i[0]=12;
            }
            i[2]=1;
        }

        return i;
    }

}
