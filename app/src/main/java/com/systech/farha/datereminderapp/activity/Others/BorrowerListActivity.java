package com.systech.farha.datereminderapp.activity.Others;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.adapter.BorrowerAdapter;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.helper.SessionManager;
import com.systech.farha.datereminderapp.model.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BorrowerListActivity extends AppCompatActivity {
    public static final String BORROW = "borrow";
    ListView listViewFriend;

    TextView alertTv;

    SessionManager session;
    DatabaseHelper databaseHelper;
    BorrowerAdapter adapter;
    HashMap<String, String> user;
    Integer userId;
    List<Person> personList = new ArrayList<>();

    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrower);
        getSupportActionBar().setTitle("Borrower List"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        initializeView();
    }

    private void initializeView() {

        session = new SessionManager(getApplicationContext());
        databaseHelper = new DatabaseHelper(getApplicationContext());
        listViewFriend = findViewById(R.id.borrower_friend_list);
        FloatingActionButton borrowerFab = findViewById(R.id.borrower_fab);
        materialSearchBar = findViewById(R.id.borrow_search);
        alertTv = findViewById(R.id.borrower_alert_tv);

        user = session.getLoginDetails();
        userId = Integer.valueOf(user.get(SessionManager.KEY_ID));
        personList = databaseHelper.getBorrowList(userId, "T");
        if (personList.size()==0){
            alertTv.setVisibility(View.VISIBLE);
        }else {
            alertTv.setVisibility(View.INVISIBLE);
        }
        adapter = new BorrowerAdapter(this, personList, false);
        listViewFriend.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        materialSearchBar.setHint("Search");

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()){
                    personList = databaseHelper.getBorrowList(userId,"T");
                    adapter = new BorrowerAdapter(BorrowerListActivity.this, personList, true);
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
                if (!enabled){
                    personList = databaseHelper.getBorrowList(userId, "T");
                    if (personList.size()==0){
                        alertTv.setVisibility(View.VISIBLE);
                        alertTv.setText("Borrower not available!");
                    }else {
                        alertTv.setVisibility(View.INVISIBLE);
                    }
                    adapter = new BorrowerAdapter(BorrowerListActivity.this, personList, false);
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

        borrowerFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BorrowerListActivity.this, AddClient.class);
                intent.putExtra("type", BORROW);
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
        adapter = new BorrowerAdapter(this, databaseHelper.getPersonByName(text), true);
        listViewFriend.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

}
