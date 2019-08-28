package com.systech.farha.datereminderapp.alarm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.activity.MainActivity;

public class Alert extends AppCompatActivity {
    MediaPlayer mp;
    int reso=R.raw.tone;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        getWindow().setStatusBarColor(Color.parseColor("#000000"));
        ImageView im = findViewById(R.id.stop);
        mp= MediaPlayer.create(getApplicationContext(),reso);
        mp.setLooping(true);
        mp.start();

        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.release();
                startActivity(new Intent(Alert.this, MainActivity.class));
            }
        });

    }

}
