package com.systech.farha.datereminderapp.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.alarm.SetAlarm;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.helper.SessionManager;
import com.systech.farha.datereminderapp.model.Person;
import com.systech.farha.datereminderapp.model.User;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static com.systech.farha.datereminderapp.activity.MainActivity.REG_PREFS_NAME;
import static com.systech.farha.datereminderapp.activity.ProfileActivity.getBitmapAsByteArray;

public class AddFriend extends AppCompatActivity {
    private Person friend;

    TextView txtLabel, txtDate, txtTime;
    EditText txtName, txtPhoneNo;
    FloatingActionButton fabAddPerson;
    ImageView proPic;
    ImageButton galleryImgBtn, cameraBtn;
    byte[]imageByte;

    String name, phoneNo, date;

    int finalHour, finalMinute, day, month = 0, years;
    SessionManager session;
    boolean isEdit = false;
    private ProgressBar progressBar;
    private Sprite doubleBounce;

    DatabaseHelper databaseHelper;
    HashMap<String, String> user;
    Integer userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        txtLabel = findViewById(R.id.txt_label_f);
        txtName = findViewById(R.id.txt_person_name_f);
        txtPhoneNo = findViewById(R.id.txt_person_phone_no_f);
        txtDate = findViewById(R.id.txt_person_date_f);
        txtTime = findViewById(R.id.txt_person_time_f);
        fabAddPerson = findViewById(R.id.fab_add_person_f);
        proPic = findViewById(R.id.dialog_profile_f);
        cameraBtn = findViewById(R.id.camera_dialog_f);
        galleryImgBtn = findViewById(R.id.gallery_dialog_f);
        progressBar = findViewById(R.id.bar_friend);
        doubleBounce = new Wave();

        friend = new Person();

        session = new SessionManager(getApplicationContext());
        user = session.getLoginDetails();
        userId = Integer.valueOf(user.get(SessionManager.KEY_ID));
        databaseHelper = new DatabaseHelper(this);

        isEdit = getIntent().getBooleanExtra("edit",false);
        if (isEdit){
            int id = getIntent().getIntExtra("id",-1);
            friend = databaseHelper.getPersonById(id);
            txtLabel.setText("Edit Friend");
            txtName.setText(friend.getName());
            txtDate.setHint(friend.getFriendDate());
            txtTime.setText(friend.getTimeFriend());
            txtPhoneNo.setText(friend.getPhoneNo());
            Bitmap bitmap = BitmapFactory.decodeByteArray(friend.getProfile(), 0, friend.getProfile().length);
            proPic.setImageBitmap(bitmap);
        }


        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        galleryImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddFriend.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        finalMinute = selectedMinute;
                        finalHour = selectedHour;
                        txtTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog dpd = new DatePickerDialog(AddFriend.this,
                        dateListener,
                        2000,month,day);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
                dpd.getDatePicker().setSpinnersShown(true);
                dpd.getDatePicker().setCalendarViewShown(false);
                dpd.show();
            }

            private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int myear, int mmonth, int dayOfMonth) {
                    view.setMaxDate(System.currentTimeMillis());
                    String showDate = dayOfMonth + "/"+ (mmonth+1) + "/"+myear;
                    day = dayOfMonth;
                    month = mmonth;
                    years = myear;
                    txtDate.setText(showDate);
                }
            };
        });

        fabAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = txtName.getText().toString();
                phoneNo = txtPhoneNo.getText().toString();
                date = txtDate.getText().toString();

                if (name.isEmpty()){
                    txtName.setError("Enter Name!");
                }else if (date.isEmpty()){
                    txtDate.setError("Enter date!");
                }else if (phoneNo.isEmpty()){
                    txtPhoneNo.setError("Enter phone number!");
                }else {
                    new LoadData().execute();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case 0:
                    assert data != null;
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    proPic.setImageBitmap(bitmap);
                    break;
                case 1:
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        proPic.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(AddFriend.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    Toast.makeText(AddFriend.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddFriend.this, FriendListActivity.class));
    }

    private class LoadData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Bitmap bitmap = ((BitmapDrawable)proPic.getDrawable()).getBitmap();
            imageByte = getBitmapAsByteArray(bitmap);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminateDrawable(doubleBounce);
            fabAddPerson.setClickable(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            friend.setName(name);
            friend.setPhoneNo(phoneNo);
            friend.setFriendDate(date);
            friend.setTimeFriend(finalHour+":"+finalMinute);
            friend.setUserId(userId);
            friend.setProfile(imageByte);

            if (isEdit){
                boolean b = databaseHelper.updatePerson(friend);
                if (b){
                    Toast.makeText(AddFriend.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddFriend.this, FriendListActivity.class));
                    finish();
                }
            }else {
                friend.setAmountBorrow(null);
                friend.setAmountLoan(null);
                friend.setFriend("T");
                friend.setLoan("F");
                friend.setBorrow("F");
                friend.setLoanHasPaid(false);
                friend.setBorrowHasPaid(false);
                databaseHelper.addPerson(friend);

                if(databaseHelper.checkPerson(name, phoneNo)){
                    SetAlarm.SetAlarms(AddFriend.this, month, day, finalHour, finalMinute);
                    Log.v("times", finalHour+":"+finalMinute+"  "+day);
                    startActivity(new Intent(AddFriend.this,FriendListActivity.class));
                    finish();
                }
            }

        }
    }
}
