package com.systech.farha.datereminderapp.activity.Others;

import android.app.job.JobScheduler;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.adapter.EventBorrowerAdapter;
import com.systech.farha.datereminderapp.alarm.RingtonePlayingService;
import com.systech.farha.datereminderapp.adapter.EventBirthdayAdapter;
import com.systech.farha.datereminderapp.adapter.EventLoanerAdapter;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.helper.SessionManager;
import com.systech.farha.datereminderapp.model.Person;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class EventActivity extends AppCompatActivity {

    RecyclerView birthdayListView, loanListView, borrowListView;
    private RecyclerView.LayoutManager layoutManager,layoutManager1,layoutManager2;
    EventBirthdayAdapter eventBirthdayAdapter;
    EventLoanerAdapter eventLoanerAdapter;
    EventBorrowerAdapter eventBorrowerAdapter;

    SessionManager session;
    HashMap<String, String> user;
    Integer userId;
    List<Person> friendList = new ArrayList<>();
    List<Person> loanerList = new ArrayList<>();
    List<Person> borrowList = new ArrayList<>();
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            scheduler.cancel(123);
        }else {
            Intent stopIntent = new Intent(this, RingtonePlayingService.class);
            stopService(stopIntent);
        }

        session = new SessionManager(getApplicationContext());
        databaseHelper = new DatabaseHelper(this);
        user = session.getLoginDetails();
        userId = Integer.valueOf(user.get(SessionManager.KEY_ID));
        friendList = databaseHelper.getFriendList(userId, "T");
        loanerList = databaseHelper.getLoanerList(userId, "T");
        borrowList = databaseHelper.getBorrowList(userId,"T");

        birthdayListView = findViewById(R.id.event_birthday_list);
        loanListView = findViewById(R.id.event_loan_list);
        borrowListView = findViewById(R.id.event_borrow_list);

        birthdayListView.setHasFixedSize(true);
        loanListView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager1 = new LinearLayoutManager(this);
        layoutManager2 = new LinearLayoutManager(this);
        birthdayListView.setLayoutManager(layoutManager);
        loanListView.setLayoutManager(layoutManager1);
        borrowListView.setLayoutManager(layoutManager2);

        eventBirthdayAdapter = new EventBirthdayAdapter(getWeekDate(friendList, 0), this);
        birthdayListView.setAdapter(eventBirthdayAdapter);

        eventLoanerAdapter = new EventLoanerAdapter(getWeekDate(loanerList, 1), this);
        loanListView.setAdapter(eventLoanerAdapter);

        eventBorrowerAdapter = new EventBorrowerAdapter(getWeekDate(borrowList, 2), this);
        borrowListView.setAdapter(eventBorrowerAdapter);
    }

    private List<Person> getWeekDate(List<Person> friendList, int id) {
        Calendar calendar = Calendar.getInstance();
        int day = (calendar.get(Calendar.DAY_OF_MONTH));
        int month = (calendar.get(Calendar.MONTH))+1;
        int year = calendar.get(Calendar.YEAR);
        String dates = covertDate(day, month, year);

        long weekTime = getDateInMIli(dates);
        List<Person>personList = new ArrayList<>();
        for (int i=0; i<friendList.size(); i++){
            String[] splitDate = new String[0];
            switch (id){
                case 0:
                    splitDate = friendList.get(i).getFriendDate().split("/");
                    break;
                case 1:
                    splitDate = friendList.get(i).getLoanDate().split("/");
                    break;
                case 2:
                    splitDate = friendList.get(i).getBorrowDate().split("/");
                    break;
            }
            long time = getDateInMIli(splitDate[0]+"/"+splitDate[1]+"/"+year);

            if (time<=weekTime){
                long diff = (weekTime - time)/(24 * 60 * 60 * 1000);
                if (diff<=7){
                    personList.add(friendList.get(i));
                }
            }
        }

        return personList;
    }

    private String covertDate(int day, int month, int year){
        if (day+7>31){
            month=month+1;
            if (month>12){
                month=0;
                year=year+1;
            }
            day = day+7-31;
        }else {
            day = day+7;
        }

        return (day+"/"+month)+"/"+year;
    }

    private long getDateInMIli(String date){
        long time = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date mDate = null;
        try {
            mDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        time = mDate.getTime();
        return time;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
    }


}
