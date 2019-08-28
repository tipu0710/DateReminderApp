package com.systech.farha.datereminderapp.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.helper.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Edit_Note extends AppCompatActivity {

    SQLiteDatabase db;
    DatabaseHelper mDbHelper;
    EditText mTitleText;
    EditText mDescriptionText;
    Spinner mSpinner;
    DatePicker pickerDate;
    TimePicker pickerTime;
    TextView time;
    TextView date;
    String type;

    int day, month, year, hour, minute;
    SessionManager session;
    HashMap<String, String> user;
    Integer userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__note);
        getSupportActionBar().setTitle("Edit Note"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        session = new SessionManager(getApplicationContext());
        user = session.getLoginDetails();
        userId = Integer.valueOf(user.get(SessionManager.KEY_ID));

        mDbHelper = new DatabaseHelper(this);
        db = mDbHelper.getWritableDatabase();

        mTitleText = findViewById(R.id.txttitle);
        mDescriptionText = findViewById(R.id.description);
        mSpinner = findViewById(R.id.spinnerNoteType);
        pickerDate = findViewById(R.id.datePicker2);
        pickerTime = findViewById(R.id.timePicker2);
        time = findViewById(R.id.txt_selecttime);
        date = findViewById(R.id.txt_selectdate);

        final long id = getIntent().getExtras().getLong(getString(R.string.row_id_log));

        Cursor cursor = db.rawQuery("select * from " + mDbHelper.TABLE_ALARM + " where " + mDbHelper.C_ID + "=" + id+" and "+mDbHelper.ALARM_USER_ID+"="+userId, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                mTitleText.setText(cursor.getString(cursor.getColumnIndex(mDbHelper.TITLE)));
                mDescriptionText.setText(cursor.getString(cursor.getColumnIndex(mDbHelper.DETAIL)));
                type = cursor.getString(cursor.getColumnIndex(mDbHelper.TYPE));
                String s = cursor.getString(cursor.getColumnIndex(mDbHelper.DATE));
                String t = cursor.getString(cursor.getColumnIndex(mDbHelper.TIME));
                String[] time = t.split(":");
                String[] date = s.split("/");
                hour = Integer.parseInt(time[0]);
                minute = Integer.parseInt(time[1]);
                day = Integer.parseInt(date[0]);
                month = Integer.parseInt(date[1])-1;
                year = Integer.parseInt(date[2]);

                pickerDate.init(year, month, day, null);
                pickerTime.setCurrentHour(hour);
                pickerTime.setCurrentMinute(minute);
            }
        }
        cursor.close();
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
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_back:
                Intent openAlarmMainActivity = new Intent(this, AlarmActivity.class);
                startActivity(openAlarmMainActivity);
                return true;
            case R.id.action_save:
                final long id = getIntent().getExtras().getLong(getString(R.string.row_id_long));
                String title = mTitleText.getText().toString();
                String detail = mDescriptionText.getText().toString();
                ContentValues cv = new ContentValues();
                cv.put(mDbHelper.TITLE, title);
                cv.put(mDbHelper.DETAIL, detail);
                cv.put(mDbHelper.TYPE, type);
                cv.put(mDbHelper.TIME, getString(R.string.Not_Set));
                cv.putNull(mDbHelper.DATE);

                Calendar calender = Calendar.getInstance();
                calender.clear();
                calender.set(Calendar.MONTH, pickerDate.getMonth());
                calender.set(Calendar.DAY_OF_MONTH, pickerDate.getDayOfMonth());
                calender.set(Calendar.YEAR, pickerDate.getYear());
                calender.set(Calendar.HOUR, pickerTime.getCurrentHour());
                calender.set(Calendar.MINUTE, pickerTime.getCurrentMinute());
                calender.set(Calendar.SECOND, 00);

                SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
                String timeString = formatter.format(new Date(calender.getTimeInMillis()));
                SimpleDateFormat dateformatter = new SimpleDateFormat(getString(R.string.dateformate));
                String dateString = dateformatter.format(new Date(calender.getTimeInMillis()));


                AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, AlarmReceiver.class);

                String alertTitle = mTitleText.getText().toString();
                intent.putExtra(getString(R.string.alert_title), alertTitle);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

                alarmMgr.set(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pendingIntent);
                cv.put(mDbHelper.TIME, timeString);
                cv.put(mDbHelper.DATE, dateString);


                db.update(mDbHelper.TABLE_ALARM, cv, mDbHelper.C_ID + "=" + id+" and "+mDbHelper.ALARM_USER_ID+"="+userId, null);

                Intent openMainScreen = new Intent(Edit_Note.this, AlarmActivity.class);
                startActivity(openMainScreen);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
