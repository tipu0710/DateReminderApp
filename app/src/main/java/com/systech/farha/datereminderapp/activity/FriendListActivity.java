package com.systech.farha.datereminderapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.adapter.PersonListAdapter;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.helper.SessionManager;
import com.systech.farha.datereminderapp.model.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    ListView listViewFriend;
    FloatingActionButton fabAddFriend;

    TextView alertTv;


    SessionManager session;
    DatabaseHelper databaseHelper;
    PersonListAdapter adapter;
    HashMap<String, String> user;
    Integer userId;
    List<Person> personList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeView();
    }

    private void initializeView() {

        session = new SessionManager(getApplicationContext());
        databaseHelper = new DatabaseHelper(getApplicationContext());

        listViewFriend = findViewById(R.id.lv_friend_list);
        fabAddFriend = findViewById(R.id.fab_add_riend);
        materialSearchBar = findViewById(R.id.friend_search);
        alertTv = findViewById(R.id.friend_alert_tv);

        user = session.getLoginDetails();
        userId = Integer.valueOf(user.get(SessionManager.KEY_ID));
        personList = databaseHelper.getFriendList(userId, "T");
        if (personList.size()==0){
            alertTv.setVisibility(View.VISIBLE);
        }else {
            alertTv.setVisibility(View.INVISIBLE);
        }
        adapter = new PersonListAdapter(this, personList);
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
                    personList = databaseHelper.getFriendList(userId,"T");
                    adapter = new PersonListAdapter(FriendListActivity.this, personList);
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
                    personList = databaseHelper.getFriendList(userId, "T");
                    if (personList.size()==0){
                        alertTv.setVisibility(View.VISIBLE);
                        alertTv.setText("Friend not available!");
                    }else {
                        alertTv.setVisibility(View.INVISIBLE);
                    }
                    adapter = new PersonListAdapter(FriendListActivity.this, personList);
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

        fabAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FriendListActivity.this, AddFriend.class));
            }
        });

    }

    private void startSearch(String text) {
        if (databaseHelper.getFriendByName(text).size()==0){
            alertTv.setVisibility(View.VISIBLE);
        }else {
            alertTv.setVisibility(View.INVISIBLE);
        }
        adapter = new PersonListAdapter(this, databaseHelper.getFriendByName(text));
        listViewFriend.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }
}
