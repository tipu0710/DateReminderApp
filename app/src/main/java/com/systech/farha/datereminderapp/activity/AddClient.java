package com.systech.farha.datereminderapp.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.alarm.SetAlarm;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.helper.SessionManager;
import com.systech.farha.datereminderapp.model.Person;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.systech.farha.datereminderapp.activity.BorrowerListActivity.BORROW;
import static com.systech.farha.datereminderapp.activity.LoanerListActivity.LOAN;
import static com.systech.farha.datereminderapp.activity.ProfileActivity.getBitmapAsByteArray;
import static com.systech.farha.datereminderapp.adapter.BorrowerAdapter.BORROW_ADD;
import static com.systech.farha.datereminderapp.adapter.BorrowerAdapter.BORROW_EDIT;
import static com.systech.farha.datereminderapp.adapter.LoanerAdapter.LOAN_ADD;
import static com.systech.farha.datereminderapp.adapter.LoanerAdapter.LOAN_EDIT;

public class AddClient extends AppCompatActivity {
    private Person person;

    TextView txtLabel, txtDate, txtTime;
    EditText txtName, txtPhoneNo,txtAmount;
    CircleImageView proPic;
    ImageButton cameraBtn, galleryBtn;

    FloatingActionButton fabAddPerson;

    String name, phoneNo, date;
    Double amount;
    Boolean hasPaid =false;
    private String type;

    SessionManager session;
    DatabaseHelper databaseHelper;
    HashMap<String, String> user;
    Integer userId;
    private String amountS;
    private int id;
    int finalHour, finalMinute, day, month, year = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        type = getIntent().getStringExtra("type");
        person = new Person();

        txtLabel = findViewById(R.id.txt_label);
        txtName = findViewById(R.id.txt_person_name);
        txtPhoneNo = findViewById(R.id.txt_person_phone_no);
        txtDate = findViewById(R.id.txt_person_date);
        txtTime = findViewById(R.id.txt_person_time);
        txtAmount = findViewById(R.id.txt_person_amount);
        fabAddPerson = findViewById(R.id.fab_add_person);
        proPic = findViewById(R.id.dialog_profile);
        cameraBtn = findViewById(R.id.camera_dialog);
        galleryBtn = findViewById(R.id.gallery_dialog);

        txtName.setHint("Enter Borrower Name");
        txtDate.setHint("Enter Date");

        session = new SessionManager(getApplicationContext());
        user = session.getLoginDetails();
        userId = Integer.valueOf(user.get(SessionManager.KEY_ID));
        databaseHelper = new DatabaseHelper(this);

        switch (type){
            case BORROW:
                txtLabel.setText("Add Borrower");
                break;
            case BORROW_EDIT:
                txtLabel.setText("Edit Borrower");
                id = getIntent().getIntExtra("id", -1);
                person = databaseHelper.getPersonById(id);
                txtName.setText(person.getName());
                txtPhoneNo.setText(person.getPhoneNo());
                txtDate.setText(person.getBorrowDate());
                txtTime.setText(person.getTimeBorrower());
                txtAmount.setText(String.valueOf(person.getAmountBorrow()));
                proPic.setImageBitmap(BitmapFactory.decodeByteArray(person.getProfile(), 0, person.getProfile().length));
                break;
            case BORROW_ADD:
                txtLabel.setText("Add Borrower From Friend");
                id = getIntent().getIntExtra("id", -1);
                person = databaseHelper.getPersonById(id);
                txtName.setText(person.getName());
                txtPhoneNo.setText(person.getPhoneNo());
                proPic.setImageBitmap(BitmapFactory.decodeByteArray(person.getProfile(), 0, person.getProfile().length));
                break;
            case LOAN:
                txtLabel.setText("Add Loaner");
                txtName.setHint("Enter loner name");
                break;
            case LOAN_EDIT:
                txtLabel.setText("Edit Borrower");
                txtName.setHint("Enter loner name");
                id = getIntent().getIntExtra("id", -1);
                person = databaseHelper.getPersonById(id);
                txtName.setText(person.getName());
                txtPhoneNo.setText(person.getPhoneNo());
                txtDate.setText(person.getBorrowDate());
                txtTime.setText(person.getTimeBorrower());
                txtAmount.setText(String.valueOf(person.getAmountBorrow()));
                proPic.setImageBitmap(BitmapFactory.decodeByteArray(person.getProfile(), 0, person.getProfile().length));
                break;
            case LOAN_ADD:
                txtLabel.setText("Add Loaner From Friend");
                txtName.setHint("Enter loner name");
                id = getIntent().getIntExtra("id", -1);
                person = databaseHelper.getPersonById(id);
                txtName.setText(person.getName());
                txtPhoneNo.setText(person.getPhoneNo());
                proPic.setImageBitmap(BitmapFactory.decodeByteArray(person.getProfile(), 0, person.getProfile().length));
                break;
        }

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
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
                mTimePicker = new TimePickerDialog(AddClient.this, new TimePickerDialog.OnTimeSetListener() {
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
                DatePickerDialog dpd = new DatePickerDialog(AddClient.this,
                        dateListener,
                        year,month,day);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                dpd.getDatePicker().setSpinnersShown(true);
                dpd.getDatePicker().setCalendarViewShown(false);
                dpd.show();
            }

            private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int myear, int mmonth, int dayOfMonth) {
                    view.setMinDate(System.currentTimeMillis()-1000);
                    String showDate = dayOfMonth + "/"+ (mmonth+1) + "/"+myear;
                    day = dayOfMonth;
                    month = mmonth;
                    year = myear;
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
                amountS = txtAmount.getText().toString();

                if (name.isEmpty()){
                    txtName.setError("Enter Name!");
                }else if (date.isEmpty()){
                    txtDate.setError("Enter date!");
                }else if (phoneNo.isEmpty()){
                    txtPhoneNo.setError("Enter phone number!");
                }else if (amountS.isEmpty()){
                    txtAmount.setError("Enter amount");
                }else {
                    amount = Double.valueOf(amountS);

                    person.setName(name);
                    person.setPhoneNo(phoneNo);
                    Bitmap bitmap = ((BitmapDrawable)proPic.getDrawable()).getBitmap();
                    byte[] imageByte = getBitmapAsByteArray(bitmap);
                    person.setProfile(imageByte);
                    switch (type){
                        case BORROW:
                            person.setBorrow("T");
                            person.setLoan("F");
                            person.setFriend("F");
                            person.setLoanHasPaid(hasPaid);
                            person.setBorrowHasPaid(hasPaid);
                            person.setUserId(userId);
                            person.setBorrowDate(date);
                            person.setTimeBorrower(finalHour+":"+finalMinute);
                            person.setAmountBorrow(amount);

                            databaseHelper.addPerson(person);
                            break;
                        case BORROW_EDIT:
                            person.setBorrowDate(date);
                            person.setTimeBorrower(finalHour+":"+finalMinute);
                            person.setAmountBorrow(amount);
                            databaseHelper.updatePerson(person);
                            break;
                        case BORROW_ADD:
                            person.setBorrow("T");
                            person.setBorrowHasPaid(hasPaid);
                            person.setBorrowDate(date);
                            person.setTimeBorrower(finalHour+":"+finalMinute);
                            person.setAmountBorrow(amount);
                            databaseHelper.updatePerson(person);
                            break;
                        case LOAN:
                            person.setBorrow("F");
                            person.setLoan("T");
                            person.setFriend("F");
                            person.setLoanHasPaid(hasPaid);
                            person.setBorrowHasPaid(hasPaid);
                            person.setUserId(userId);
                            person.setLoanDate(date);
                            person.setTimeLoner(finalHour+":"+finalMinute);
                            person.setAmountLoan(amount);
                            databaseHelper.addPerson(person);
                            break;
                        case LOAN_EDIT:
                            person.setLoanDate(date);
                            person.setTimeLoner(finalHour+":"+finalMinute);
                            person.setAmountLoan(amount);
                            databaseHelper.updatePerson(person);
                            break;
                        case LOAN_ADD:
                            person.setLoan("T");
                            person.setLoanHasPaid(hasPaid);
                            person.setLoanDate(date);
                            person.setTimeLoner(finalHour+":"+finalMinute);
                            person.setAmountLoan(amount);
                            databaseHelper.updatePerson(person);
                            break;

                    }

                    if(databaseHelper.checkPerson(name, phoneNo)){
                        SetAlarm.SetAlarms(AddClient.this, month, day, finalHour, finalMinute);
                        backToParent();
                    }
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
                        Toast.makeText(AddClient.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    Toast.makeText(AddClient.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToParent();
    }

    private void backToParent(){
        switch (type){
            case BORROW:
            case BORROW_ADD:
            case BORROW_EDIT:
                startActivity(new Intent(AddClient.this,BorrowerListActivity.class));
                finish();
                break;
            case LOAN:
            case LOAN_EDIT:
            case LOAN_ADD:
                startActivity(new Intent(AddClient.this,LoanerListActivity.class));
                finish();
                break;
        }
    }
}
