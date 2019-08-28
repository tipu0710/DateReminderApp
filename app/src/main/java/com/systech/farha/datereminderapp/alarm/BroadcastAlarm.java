package com.systech.farha.datereminderapp.alarm;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.systech.farha.datereminderapp.R;

public class BroadcastAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        /*if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            ComponentName componentName = new ComponentName(context, RingtonePlayingJobService.class);
            JobInfo jobInfo = new JobInfo.Builder(123, componentName)
                    .setPersisted(true)
                    .setPeriodic(15*60*1000)
                    .build();
            JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            scheduler.schedule(jobInfo);
        }else {
            Uri alarmUri = Uri.parse("android.resource://"+context.getPackageName()+"/"+ R.raw.tone);
            Intent startIntent = new Intent(context, RingtonePlayingService.class);
            startIntent.putExtra("ringtone-uri", alarmUri);
            context.startService(startIntent);
        }*/

        Log.v("alarm", "start");

    }


}
