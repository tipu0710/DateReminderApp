package com.systech.farha.datereminderapp.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.alarm.CreateNote;
import com.systech.farha.datereminderapp.alarm.SetAlarm;
import com.systech.farha.datereminderapp.adapter.LoanerAdapter;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.helper.SessionManager;
import com.systech.farha.datereminderapp.model.Person;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LoanerListActivity extends AppCompatActivity {
    private Person loaner;
    ListView listViewFriend;

    TextView txtLabel,txtDate, alertTv,txtTime;
    EditText txtName, txtPhoneNo, txtAmount;
    FloatingActionButton fabAddPerson;

    String name, phoneNo, date;
    Double amount;
    Boolean hasPaid = false;
    final Calendar calendar = Calendar.getInstance();

    SessionManager session;
    DatabaseHelper databaseHelper;
    AlertDialog dialog;
    LoanerAdapter adapter;
    HashMap<String, String> user;
    Integer userId;
    List<Person> personList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;
    int finalHour, finalMinute, day, month = 0;

    public static final String LOAN = "loan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaner);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeView();
    }

    private void initializeView() {

        session = new SessionManager(getApplicationContext());
        databaseHelper = new DatabaseHelper(getApplicationContext());
        listViewFriend = findViewById(R.id.loan_friend_lists);
        materialSearchBar = findViewById(R.id.loaner_search);
        alertTv = findViewById(R.id.loaner_alert_tv);


        FloatingActionButton fab = findViewById(R.id.loan_fab);

        user = session.getLoginDetails();
        userId = Integer.valueOf(user.get(SessionManager.KEY_ID));
        personList = databaseHelper.getLoanerList(userId, "T");
        if (personList.size()==0){
            alertTv.setVisibility(View.VISIBLE);
        }else {
            alertTv.setVisibility(View.INVISIBLE);
        }
        adapter = new LoanerAdapter(this, personList,false);
        listViewFriend.setAdapter(adapter);

        materialSearchBar.setHint("Search");
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().isEmpty()){
                    personList = databaseHelper.getLoanerList(userId,"T");
                    adapter = new LoanerAdapter(LoanerListActivity.this, personList,true);
                    listViewFriend.setAdapter(adapter);
                }else {
                    startSearch(s.toString());
                    if (databaseHelper.getPersonByName(s.toString()).size()==0){
                        alertTv.setVisibility(View.VISIBLE);
                        alertTv.setText("Result not found");
                    }else {
                        alertTv.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                personList = databaseHelper.getLoanerList(userId, "T");
                if (!enabled){
                    if (personList.size()==0){
                        alertTv.setVisibility(View.VISIBLE);
                        alertTv.setText("Loner not available!");
                    }else {
                        alertTv.setVisibility(View.INVISIBLE);
                    }
                    adapter = new LoanerAdapter(LoanerListActivity.this, personList,false);
                    listViewFriend.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        listViewFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: Add Required Code
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoanerListActivity.this, AddClient.class);
                intent.putExtra("type", LOAN);
                startActivity(intent);
            }
        });

    }

    private void startSearch(String text) {
        if (databaseHelper.getPersonByName(text).size()==0){
            alertTv.setVisibility(View.VISIBLE);
        }else {
            alertTv.setVisibility(View.INVISIBLE);
        }
        adapter = new LoanerAdapter(this, databaseHelper.getPersonByName(text),true);
        listViewFriend.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }
}
