package com.systech.farha.datereminderapp.alarm;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.systech.farha.datereminderapp.R;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class RingtonePlayingJobService extends JobService {
    private boolean jobCanceled = false;
    Ringtone ringtone;
    @Override
    public boolean onStartJob(JobParameters params) {
        playRingtone(params);
        return true;
    }

    private void playRingtone(final JobParameters prams) {
        NotificationHelper notificationHelper = new NotificationHelper(RingtonePlayingJobService.this);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i =0; i<6; i++){
                    if (jobCanceled){
                        return;
                    }
                    try {
                        Uri alarmUri = Uri.parse("android.resource://"+getPackageName()+"/"+ R.raw.tone);
                        ringtone = RingtoneManager.getRingtone(RingtonePlayingJobService.this, alarmUri);
                        ringtone.play();
                        Thread.sleep(10*1000);
                    }catch (Exception e){

                    }
                }

                jobFinished(prams, false);
                Log.v("service","finished");
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.v("service","canceled");
        jobCanceled = true;
        ringtone.stop();
        return false;
    }

}
