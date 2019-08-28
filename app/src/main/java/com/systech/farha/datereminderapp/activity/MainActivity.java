package com.systech.farha.datereminderapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.pavlospt.CircleView;
import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.alarm.AlarmActivity;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.helper.SessionManager;
import com.systech.farha.datereminderapp.model.Person;
import com.systech.farha.datereminderapp.model.User;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.StrictMath.round;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String REG_PREFS_NAME = "register_info";
    SessionManager session;
    HashMap<String, String> user;
    Integer userId;
    List<Person> personList = new ArrayList<>();
    DatabaseHelper databaseHelper;
    CircleView totalLoan, totalBorrow;
    public static String PREFS_NAME="unique";
    PieChart pieChart;
    private ImageView proPic;
    private TextView nameTv;
    private TextView emailTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor ed;
        if (!preferences.contains("i")){
            ed = preferences.edit();
            ed.putInt("i",111);
            ed.apply();
        }


        session = new SessionManager(getApplicationContext());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        View navView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        proPic = navView.findViewById(R.id.imageView);
        nameTv = navView.findViewById(R.id.user_name_);
        emailTv = navView.findViewById(R.id.email);

        databaseHelper = new DatabaseHelper(this);
        user = session.getLoginDetails();
        userId = Integer.valueOf(user.get(SessionManager.KEY_ID));
        personList = databaseHelper.getPersonList(userId);

        User user = databaseHelper.getUserById(userId);

        initializeView();

        nameTv.setText(user.getName());
        emailTv.setText(user.getEmail());
        Bitmap bitmap = BitmapFactory.decodeByteArray(user.getProfile(), 0, user.getProfile().length);
        proPic.setImageBitmap(bitmap);

    }

    private void initializeView() {
        session.checkLogin();
        totalLoan = findViewById(R.id.total_loan);
        totalBorrow = findViewById(R.id.total_borrow);
        pieChart = findViewById(R.id.pie_chart);

        double totalLoanD = databaseHelper.getTotalLoan(userId);
        double totalBorrowD = databaseHelper.getTotalBorrow(userId);
        double total = totalBorrowD+totalLoanD;
        double pOfLoan = (totalLoanD*100)/total;
        double pOfBorrow = (totalBorrowD*100)/total;
        DecimalFormat decimalFormat = new DecimalFormat("#.0");
        totalLoan.setTitleText(String.valueOf(totalLoanD));
        totalBorrow.setTitleText(String.valueOf(totalBorrowD));
        totalLoan.setSubtitleText(decimalFormat.format(pOfLoan)+"%");
        totalBorrow.setSubtitleText(decimalFormat.format(pOfBorrow)+"%");

        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry((float)pOfLoan,"Loan"));
        pieEntries.add(new PieEntry((float)pOfBorrow, "Borrow"));

        pieChart.animateXY(1000,1000);
        PieDataSet pieDataSet = new PieDataSet(pieEntries,"");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setValueTextSize(2);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.WHITE);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.friends) {
            Intent friendIntent = new Intent(getApplicationContext(), FriendListActivity.class);
            //startActivity(friendIntent);
            startActivityForResult(friendIntent, 0101);
        }else if (id == R.id.borrow) {
            Intent friendIntent = new Intent(getApplicationContext(), BorrowerListActivity.class);

            startActivity(friendIntent);
        }else if (id == R.id.loan) {
            Intent friendIntent = new Intent(getApplicationContext(), LoanerListActivity.class);

            startActivity(friendIntent);
        } else if (id == R.id.event) {
            startActivity(new Intent(MainActivity.this, EventActivity.class));
        } else if (id == R.id.nav_logout) {
            session.logoutUser();
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(MainActivity.this, ProfileEditActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
