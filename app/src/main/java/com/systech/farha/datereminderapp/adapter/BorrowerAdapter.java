package com.systech.farha.datereminderapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.design.widget.FloatingActionButton;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.activity.AddClient;
import com.systech.farha.datereminderapp.activity.BorrowerListActivity;
import com.systech.farha.datereminderapp.alarm.SetAlarm;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.model.Person;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class BorrowerAdapter  extends BaseAdapter {

    TextView txtName, txtPhoneNo, txtDate, txtTime, txtAmount;
    CircleImageView proPic;

    View mainView;
    private Activity activity;
    private List<Person> personArrayList;
    private LayoutInflater inflater = null;
    private boolean status = false;
    public static final String BORROW_EDIT = "borrow_edit";
    public static final String BORROW_ADD= "borrow_add";

    public BorrowerAdapter(Activity activity, List<Person> personArrayList, boolean status) {
        this.activity = activity;
        this.personArrayList = personArrayList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.status = status;
    }

    @Override
    public int getCount() {
        return personArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;

        if (convertView == null){
            view = inflater.inflate(R.layout.person_list_item, null);
            txtName = view.findViewById(R.id.txt_person_name);
            txtPhoneNo = view.findViewById(R.id.txt_person_phone_no);
            txtDate = view.findViewById(R.id.txt_person_date);
            txtAmount = view.findViewById(R.id.txt_person_amount);
            txtTime = view.findViewById(R.id.txt_person_time);
            mainView = view.findViewById(R.id.main_view);
            proPic = view.findViewById(R.id.person_list_profile);

            if (activity.getClass().getSimpleName()!="PersonListActivity")
            {
                txtAmount.setVisibility(View.VISIBLE);
            }
            Person person = personArrayList.get(position);

            proPic.setImageBitmap(BitmapFactory.decodeByteArray(person.getProfile(), 0, person.getProfile().length));

            txtName.setText(person.getName());
            txtPhoneNo.setText(person.getPhoneNo());
            txtDate.setText(person.getBorrowDate());

            if (status){
                txtTime.setVisibility(View.GONE);
            }else {
                txtTime.setVisibility(View.VISIBLE);
                int[] t = SetAlarm.formatTime(person.getTimeBorrower());
                if (t[2]==0){
                    txtTime.setText(t[0]+":"+t[1]+"PM");
                }else {
                    txtTime.setText(t[0]+":"+t[1]+"AM");
                }
            }

            txtAmount.setText(String.valueOf(person.getAmountBorrow()));

            if (personArrayList.get(position).getBorrowHasPaid()){
                txtName.setTextColor(Color.parseColor("#D28BC34A"));
                txtDate.setTextColor(Color.parseColor("#D28BC34A"));
                txtPhoneNo.setTextColor(Color.parseColor("#D28BC34A"));
                txtTime.setTextColor(Color.parseColor("#D28BC34A"));
                txtAmount.setTextColor(Color.parseColor("#D28BC34A"));
            }else {
                txtName.setTextColor(Color.parseColor("#C3FF5722"));
                txtDate.setTextColor(Color.parseColor("#C3FF5722"));
                txtPhoneNo.setTextColor(Color.parseColor("#C3FF5722"));
                txtTime.setTextColor(Color.parseColor("#C3FF5722"));
                txtAmount.setTextColor(Color.parseColor("#C3FF5722"));
            }

            //DialogBox after onLongClicked
            final Person finalPerson = person;

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
                    final View dialogView =LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.long_pressed_dialog, parent, false);
                    TextView editTv = dialogView.findViewById(R.id.dialog_edit_tv);
                    TextView deleteTv = dialogView.findViewById(R.id.dialog_delete_tv);
                    TextView makePaid = dialogView.findViewById(R.id.dialog_paid_tv);

                    if (finalPerson.getBorrowHasPaid()){
                        makePaid.setVisibility(View.GONE);
                    }else {
                        makePaid.setVisibility(View.VISIBLE);
                    }

                    mBuilder.setView(dialogView);
                    final AlertDialog dialog = mBuilder.create();

                    dialog.show();

                    makePaid.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Person p = personArrayList.get(position);
                            p.setBorrowHasPaid(true);
                            p.setName("tipu");

                            DatabaseHelper helper = new DatabaseHelper(activity);
                            helper.updatePerson(p);
                            dialog.dismiss();
                            activity.startActivity(new Intent(activity, BorrowerListActivity.class));
                        }
                    });

                    editTv.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(activity, AddClient.class);
                            intent.putExtra("type", BORROW_EDIT);
                            intent.putExtra("id", finalPerson.getId());
                            activity.startActivity(intent);

                            /*
                            dialog box for edit info
                            */
                            /*AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            View view1 =LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.add_persion_dialog, parent, false);
                            final TextView txtLabelD, txtDateD;
                            final EditText txtNameD, txtPhoneNoD, txtAmountD;
                            FloatingActionButton fabAddPersonD;
                            txtLabelD = view1.findViewById(R.id.txt_label);
                            txtNameD = view1.findViewById(R.id.txt_person_name);
                            txtPhoneNoD = view1.findViewById(R.id.txt_person_phone_no);
                            txtDateD = view1.findViewById(R.id.txt_person_date);
                            txtAmountD = view1.findViewById(R.id.txt_person_amount);
                            fabAddPersonD = view1.findViewById(R.id.fab_add_person);

                            txtLabelD.setText("Update");
                            txtNameD.setText(finalPerson.getName());
                            txtDateD.setText(finalPerson.getBorrowDate());
                            txtDateD.setInputType(InputType.TYPE_NULL);
                            txtPhoneNoD.setText(finalPerson.getPhoneNo());
                            txtAmountD.setVisibility(View.VISIBLE);
                            txtAmountD.setText(finalPerson.getAmountBorrow().toString());


                            txtDateD.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Calendar calendar = Calendar.getInstance(Locale.getDefault());
                                    int month = calendar.get(Calendar.MONTH);
                                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                                    int year = calendar.get(Calendar.YEAR);
                                    DatePickerDialog dpd = new DatePickerDialog(activity,
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
                                        txtDateD.setText(showDate);
                                    }
                                };
                            });

                            builder.setView(view1);
                            final AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            fabAddPersonD.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String name = txtNameD.getText().toString();
                                    String date = txtDateD.getText().toString();
                                    String phone = txtPhoneNoD.getText().toString();
                                    String amount = txtAmountD.getText().toString();

                                    if (name.isEmpty()){
                                        txtNameD.setError("Enter Name!");
                                    }else if (date.isEmpty()){
                                        txtDateD.setError("Enter date!");
                                    }else if (phone.isEmpty()){
                                        txtPhoneNoD.setError("Enter phone number!");
                                    }else if (amount.isEmpty()){
                                        txtAmountD.setError("Enter amount");
                                    }else {
                                        finalPerson.setName(name);
                                        finalPerson.setBorrowDate(date);
                                        finalPerson.setPhoneNo(phone);
                                        finalPerson.setAmountBorrow(Double.parseDouble(amount));

                                        DatabaseHelper databaseHelper = new DatabaseHelper(activity);
                                        databaseHelper.updatePerson(finalPerson);
                                        personArrayList.set(position, finalPerson);

                                        alertDialog.dismiss();
                                        Intent intent = new Intent(activity, BorrowerListActivity.class);
                                        activity.startActivities(new Intent[]{intent});
                                    }

                                }
                            });
                            dialog.dismiss();*/
                        }
                    });

                    deleteTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*
                            dialog box for delete info
                            */

                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("Attention! Do you really want to remove "+ finalPerson.getName()+"?");

                            builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    DatabaseHelper databaseHelper = new DatabaseHelper(activity);
                                    if (finalPerson.getFriend().equals("F")){
                                        databaseHelper.deletePerson(finalPerson);
                                    }else {
                                        finalPerson.setBorrow("F");
                                        finalPerson.setAmountBorrow(0.0);
                                        databaseHelper.updatePerson(finalPerson);
                                    }
                                    Intent intent = new Intent(activity, BorrowerListActivity.class);
                                    activity.startActivities(new Intent[]{intent});
                                }
                            });

                            builder.setNegativeButton("Cancel", null);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            dialog.dismiss();
                        }

                    });
                    return true;
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int id = finalPerson.getId();
                    final int[] finalMinute = { 0 };
                    final int[] finalHour = { 0 };
                    DatabaseHelper databaseHelper = new DatabaseHelper(activity);
                    final Person person1 = databaseHelper.getPersonById(id);

                    if (person1.getBorrow().equals("T")){
                        Toast.makeText(activity, "This person already added!", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(activity, AddClient.class);
                        intent.putExtra("id",person1.getId());
                        intent.putExtra("type", BORROW_ADD);
                        activity.startActivity(intent);
                        /*AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        View view1 =LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.add_persion_dialog, parent, false);
                        final TextView txtLabelD, txtDateD, txtTimeD;
                        final EditText txtNameD, txtPhoneNoD,txtAmountD;

                        FloatingActionButton fabAddPersonD;
                        txtLabelD = view1.findViewById(R.id.txt_label);
                        txtNameD = view1.findViewById(R.id.txt_person_name);
                        txtPhoneNoD = view1.findViewById(R.id.txt_person_phone_no);
                        txtDateD = view1.findViewById(R.id.txt_person_date);
                        txtTimeD = view1.findViewById(R.id.txt_person_time);
                        txtAmountD = view1.findViewById(R.id.txt_person_amount);
                        fabAddPersonD = view1.findViewById(R.id.fab_add_person);
                        txtAmountD.setVisibility(View.VISIBLE);

                        txtLabelD.setText("Add borrower from Friend");
                        txtNameD.setText(person1.getName());
                        txtDateD.setText(person1.getBorrowDate());
                        if (status){
                            txtTime.setVisibility(View.GONE);
                        }else {
                            txtTime.setVisibility(View.VISIBLE);
                            int[] time = SetAlarm.formatTime(person1.getTimeBorrower());
                            if (time[2]==0){
                                txtTimeD.setText(time[0]+":"+time[1]+"PM");
                            }else {
                                txtTimeD.setText(time[0]+":"+time[1]+"AM");
                            }
                        }

                        txtPhoneNoD.setText(person1.getPhoneNo());
                        txtAmountD.setHint("0.0");

                        txtTimeD.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar mcurrentTime = Calendar.getInstance();
                                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                                int minute = mcurrentTime.get(Calendar.MINUTE);
                                TimePickerDialog mTimePicker;
                                mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                        finalMinute[0] = selectedMinute;
                                        finalHour[0] = selectedHour;
                                        txtTimeD.setText( selectedHour + ":" + selectedMinute);
                                    }
                                }, hour, minute, false);//Yes 24 hour time
                                mTimePicker.setTitle("Select Time");
                                mTimePicker.show();

                            }
                        });

                        txtDateD.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                                int month = calendar.get(Calendar.MONTH);
                                int day = calendar.get(Calendar.DAY_OF_MONTH);
                                int year = calendar.get(Calendar.YEAR);
                                DatePickerDialog dpd = new DatePickerDialog(activity,
                                        dateListener,
                                        year,month,day);

                                dpd.getDatePicker().setSpinnersShown(true);
                                dpd.getDatePicker().setCalendarViewShown(false);
                                dpd.show();
                            }

                            private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int myear, int mmonth, int dayOfMonth) {
                                    String showDate = dayOfMonth + "/"+ (mmonth+1) + "/"+myear;
                                    txtDateD.setText(showDate);
                                }
                            };
                        });

                        builder.setView(view1);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        fabAddPersonD.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String name = txtNameD.getText().toString();
                                String date = txtDateD.getText().toString();
                                String phone = txtPhoneNoD.getText().toString();

                                if (name.isEmpty()){
                                    txtNameD.setError("Enter Name!");
                                }else if (date.isEmpty()){
                                    txtDateD.setError("Enter date!");
                                }else if (phone.isEmpty()){
                                    txtPhoneNoD.setError("Enter phone number!");
                                }else {
                                    person1.setName(name);
                                    person1.setBorrowDate(date);
                                    person1.setTimeBorrower(finalHour[0]+":"+finalMinute[0]);
                                    person1.setPhoneNo(phone);
                                    person1.setFriend("T");
                                    person1.setBorrow("T");
                                    person1.setAmountBorrow(Double.parseDouble(txtAmountD.getText().toString()));

                                    DatabaseHelper databaseHelper = new DatabaseHelper(activity);
                                    databaseHelper.updatePerson(person1);
                                    Toast.makeText(activity, "Added Successfully", Toast.LENGTH_SHORT).show();

                                    alertDialog.dismiss();
                                    Intent intent = new Intent(activity, BorrowerListActivity.class);
                                    activity.startActivities(new Intent[]{intent});
                                }
                            }

                        });*/
                    }
                }
            });

        }
        return view;
    }
}
