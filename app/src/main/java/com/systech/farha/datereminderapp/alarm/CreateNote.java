package com.systech.farha.datereminderapp.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.activity.MainActivity;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.helper.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CreateNote extends AppCompatActivity {
    SQLiteDatabase db;
    DatabaseHelper mDbHelper;
    EditText mTitleText;
    EditText mDescriptionText;
    DatePicker pickerDate;
    TimePicker pickerTime;
    TextView time;
    TextView date;
    int finalDay, finalMonth, mHour,mMinute;
    String des, type;

    SessionManager session;
    HashMap<String, String> user;
    Integer userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        getSupportActionBar().setTitle("Create Note"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mDbHelper = new DatabaseHelper(this);
        db = mDbHelper.getWritableDatabase();

        session = new SessionManager(getApplicationContext());
        user = session.getLoginDetails();
        userId = Integer.valueOf(user.get(SessionManager.KEY_ID));

        mTitleText = findViewById(R.id.txttitle);
        mDescriptionText = findViewById(R.id.description);
        pickerDate = findViewById(R.id.datePicker);
        pickerTime = findViewById(R.id.timePicker);
        time = findViewById(R.id.txtTime);
        date = findViewById(R.id.txtDate);

        finalDay = getIntent().getIntExtra("day",Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        finalMonth = getIntent().getIntExtra("month",Calendar.getInstance().get(Calendar.MONTH));
        des = getIntent().getStringExtra("des");
        type = getIntent().getStringExtra("type");
        mHour = getIntent().getIntExtra("hour",0);
        mMinute = getIntent().getIntExtra("minute", 0);

        if(type == null){
            type = "General";
        }

        if (des != null){
            mDescriptionText.setText(des);
        }
        pickerDate.init(Calendar.getInstance().get(Calendar.YEAR),finalMonth, finalDay,null);
        pickerTime.setCurrentHour(mHour);
        pickerTime.setCurrentMinute(mMinute);

    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, AlarmActivity.class);
        startActivity(setIntent);
    }

    void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.action_save:
                String title = mTitleText.getText().toString();
                String detail = mDescriptionText.getText().toString();
                ContentValues cv = new ContentValues();
                cv.put(DatabaseHelper.TITLE, title);
                cv.put(DatabaseHelper.DETAIL, detail);
                cv.put(DatabaseHelper.TYPE, type);
                cv.put(DatabaseHelper.TIME, "Not set");

                int year = pickerDate.getYear();
                int month = pickerDate.getMonth();
                int day = pickerDate.getDayOfMonth();
                Log.v("check", pickerDate.getYear()+"");
                Calendar calender = Calendar.getInstance();
                if (month<calender.get(Calendar.MONTH)){
                    calender.set(Calendar.YEAR, year+1);
                }else if (month==calender.get(Calendar.MONTH)){
                    if (day<calender.get(Calendar.DAY_OF_MONTH)){
                        calender.set(Calendar.YEAR, year+1);
                    }
                }else {
                    calender.set(Calendar.YEAR, year);
                }

                Log.v("check", calender.get(Calendar.YEAR)+"");
                calender.set(Calendar.MONTH, pickerDate.getMonth());
                calender.set(Calendar.DAY_OF_MONTH, pickerDate.getDayOfMonth());
                calender.set(Calendar.HOUR, pickerTime.getCurrentHour());
                calender.set(Calendar.MINUTE, pickerTime.getCurrentMinute());
                calender.set(Calendar.SECOND, 0);

                Toast.makeText(this, ""+pickerTime.getCurrentHour(), Toast.LENGTH_SHORT).show();

                SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
                String timeString = formatter.format(new Date(calender.getTimeInMillis()));
                SimpleDateFormat dateformatter = new SimpleDateFormat(getString(R.string.dateformate));
                String dateString = dateformatter.format(new Date(calender.getTimeInMillis()));

                AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, AlarmReceiver.class);

                String alertTitle = mTitleText.getText().toString();
                intent.putExtra("title", alertTitle);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

                alarmMgr.set(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pendingIntent);
                cv.put(DatabaseHelper.TIME, timeString);
                cv.put(DatabaseHelper.DATE, dateString);
                cv.put(DatabaseHelper.ALARM_USER_ID, userId);

                db.insert(DatabaseHelper.TABLE_ALARM, null, cv);

                Intent openMainScreen = new Intent(this, AlarmActivity.class);
                openMainScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(openMainScreen);
                return true;

            case R.id.action_back:
                Intent openAlarmMainActivity = new Intent(this, MainActivity.class);
                startActivity(openAlarmMainActivity);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
