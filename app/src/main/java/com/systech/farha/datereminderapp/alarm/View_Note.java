package com.systech.farha.datereminderapp.alarm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.helper.SessionManager;

import java.util.HashMap;

public class View_Note extends AppCompatActivity {
    SQLiteDatabase db;
    DatabaseHelper dbHelper;
    SessionManager session;
    HashMap<String, String> user;
    Integer userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__note);
        getSupportActionBar().setTitle("View Note"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        final long id = getIntent().getExtras().getLong(getString(R.string.row_id));

        session = new SessionManager(getApplicationContext());
        user = session.getLoginDetails();
        userId = Integer.valueOf(user.get(SessionManager.KEY_ID));

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + dbHelper.TABLE_ALARM + " where " + dbHelper.C_ID + "=" + id+" and "+dbHelper.ALARM_USER_ID+"="+userId, null);
        TextView title = findViewById(R.id.title);
        TextView detail = findViewById(R.id.detail);
        TextView notetype = findViewById(R.id.note_type_ans);
        TextView time = findViewById(R.id.alertvalue);
        TextView date = findViewById(R.id.datevalue);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                title.setText(cursor.getString(cursor.getColumnIndex(dbHelper.TITLE)));
                detail.setText(cursor.getString(cursor.getColumnIndex(dbHelper.DETAIL)));
                notetype.setText(cursor.getString(cursor.getColumnIndex(dbHelper.TYPE)));
                time.setText(cursor.getString(cursor.getColumnIndex(dbHelper.TIME)));
                date.setText(cursor.getString(cursor.getColumnIndex(dbHelper.DATE)));

            }
            cursor.close();
        }
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, AlarmActivity.class);
        startActivity(setIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final long id = getIntent().getExtras().getLong("rowID");

        switch (item.getItemId()) {
            case R.id.action_back:
                Intent openMainActivity = new Intent(this, AlarmActivity.class);
                startActivity(openMainActivity);
                return true;
            case R.id.action_edit:

                Intent openEditNote = new Intent(View_Note.this, Edit_Note.class);
                openEditNote.putExtra(getString(R.string.intent_row_id), id);
                startActivity(openEditNote);
                return true;

            case R.id.action_discard:
                AlertDialog.Builder builder = new AlertDialog.Builder(View_Note.this);
                builder
                        .setTitle(getString(R.string.delete_title))
                        .setMessage(getString(R.string.delete_message))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Long id = getIntent().getExtras().getLong(getString(R.string.rodID));
                                db.delete(DatabaseHelper.TABLE_ALARM, DatabaseHelper.C_ID + "=" + id+" and "+dbHelper.ALARM_USER_ID+"="+userId, null);
                                db.close();
                                Intent openAlarmMainActivity = new Intent(View_Note.this, AlarmActivity.class);
                                startActivity(openAlarmMainActivity);

                            }
                        })
                        .setNegativeButton(getString(R.string.no), null)                        //Do nothing on no
                        .show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
