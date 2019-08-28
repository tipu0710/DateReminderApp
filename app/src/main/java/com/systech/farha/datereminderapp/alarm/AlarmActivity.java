package com.systech.farha.datereminderapp.alarm;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.activity.MainActivity;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.helper.SessionManager;

import java.util.HashMap;

public class AlarmActivity extends AppCompatActivity {

    SQLiteDatabase db;
    DatabaseHelper mDbHelper;
    ListView list;
    FloatingActionButton floatingActionButton;
    SessionManager session;
    HashMap<String, String> user;
    Integer userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        getSupportActionBar().setTitle("Alarm List"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        session = new SessionManager(getApplicationContext());
        user = session.getLoginDetails();
        userId = Integer.valueOf(user.get(SessionManager.KEY_ID));

        list = findViewById(R.id.commentlist);
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openCreateNote = new Intent(AlarmActivity.this, CreateNote.class);
                startActivity(openCreateNote);
            }
        });
        mDbHelper = new DatabaseHelper(this);
        db= mDbHelper.getWritableDatabase();
        final ImageView alarmImage = findViewById(R.id.alarmImage);

        String[] from = {mDbHelper.TITLE, mDbHelper.DETAIL, mDbHelper.TYPE, mDbHelper.TIME, mDbHelper.DATE};
        final String[] column = {mDbHelper.C_ID, mDbHelper.TITLE, mDbHelper.DETAIL, mDbHelper.TYPE, mDbHelper.TIME, mDbHelper.DATE};
        int[] to = {R.id.title, R.id.Detail, R.id.type, R.id.time, R.id.date};
        String selection = mDbHelper.ALARM_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        final Cursor cursor = db.query(mDbHelper.TABLE_ALARM, column, selection, selectionArgs ,null, null, null);
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_entry_alarm, cursor, from, to, 0);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> listView, View view, int position,
                                    long id){
                Intent intent = new Intent(AlarmActivity.this, View_Note.class);
                intent.putExtra(getString(R.string.rodId), id);
                startActivity(intent);
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AlarmActivity.this, MainActivity.class));
    }
}
