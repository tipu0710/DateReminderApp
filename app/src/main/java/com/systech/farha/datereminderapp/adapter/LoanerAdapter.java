package com.systech.farha.datereminderapp.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.text.InputType;
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
import com.systech.farha.datereminderapp.activity.LoanerListActivity;
import com.systech.farha.datereminderapp.alarm.SetAlarm;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.helper.SessionManager;
import com.systech.farha.datereminderapp.model.Person;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoanerAdapter extends BaseAdapter {

    public static final String LOAN_EDIT = "loan_edit";
    public static final String LOAN_ADD = "loan_add";
    TextView txtName, txtPhoneNo, txtDate,txtTime, txtAmount;
    View mainView;
    CircleImageView proPic;
    private Activity activity;
    private List<Person> personArrayList;
    private LayoutInflater inflater = null;
    boolean status= false;

    public LoanerAdapter(Activity activity, List<Person> personArrayList,boolean status) {
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
            txtDate.setText(person.getLoanDate());
            if (status){
                txtTime.setVisibility(View.GONE);
            }else {
                txtTime.setVisibility(View.VISIBLE);
                int[] t = SetAlarm.formatTime(person.getTimeLoner());
                if (t[2]==0){
                    txtTime.setText(t[0]+":"+t[1]+"PM");
                }else {
                    txtTime.setText(t[0]+":"+t[1]+"AM");
                }
            }
            txtAmount.setText(String.valueOf(person.getAmountLoan()));

            if (personArrayList.get(position).getLoanHasPaid()){
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
                    View dialogView =LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.long_pressed_dialog, parent, false);
                    TextView editTv = dialogView.findViewById(R.id.dialog_edit_tv);
                    TextView deleteTv = dialogView.findViewById(R.id.dialog_delete_tv);
                    TextView makePaid = dialogView.findViewById(R.id.dialog_paid_tv);

                    if (personArrayList.get(position).getLoanHasPaid()){
                        makePaid.setVisibility(View.GONE);
                    }


                    mBuilder.setView(dialogView);
                    final AlertDialog dialog = mBuilder.create();

                    dialog.show();

                    makePaid.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseHelper databaseHelper = new DatabaseHelper(activity);
                            personArrayList.get(position).setLoanHasPaid(true);
                            databaseHelper.updatePerson(personArrayList.get(position));
                            activity.startActivity(new Intent(activity,LoanerListActivity.class));
                        }
                    });


                    editTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, AddClient.class);
                            intent.putExtra("id", finalPerson.getId());
                            intent.putExtra("type", LOAN_EDIT);
                            activity.startActivity(intent);

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
                                    if (finalPerson.getFriend().equals("F") && finalPerson.getBorrow().equals("F")){
                                        databaseHelper.deletePerson(finalPerson);
                                    }else {
                                        finalPerson.setLoan("F");
                                        finalPerson.setAmountLoan(0.0);
                                        databaseHelper.updatePerson(finalPerson);
                                    }
                                    Intent intent = new Intent(activity, LoanerListActivity.class);
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
                    DatabaseHelper databaseHelper = new DatabaseHelper(activity);
                    final Person person1 = databaseHelper.getPersonById(id);

                    if (person1.getLoan().equals("T")){
                        Toast.makeText(activity, "This person already added!", Toast.LENGTH_SHORT).show();
                    }else {

                        Intent intent = new Intent(activity, AddClient.class);
                        intent.putExtra("id", finalPerson.getId());
                        intent.putExtra("type", LOAN_ADD);
                        activity.startActivity(intent);

                        /*AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        View view1 =LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.add_persion_dialog, parent, false);
                        final TextView txtLabelD, txtDateD;
                        final EditText txtNameD, txtPhoneNoD,txtAmountD;
                        FloatingActionButton fabAddPersonD;
                        txtLabelD = view1.findViewById(R.id.txt_label);
                        txtNameD = view1.findViewById(R.id.txt_person_name);
                        txtPhoneNoD = view1.findViewById(R.id.txt_person_phone_no);
                        txtDateD = view1.findViewById(R.id.txt_person_date);
                        txtAmountD = view1.findViewById(R.id.txt_person_amount);
                        fabAddPersonD = view1.findViewById(R.id.fab_add_person);
                        txtAmountD.setVisibility(View.VISIBLE);

                        txtLabelD.setText("Add loaner from Friend");
                        txtNameD.setText(person1.getName());
                        txtDateD.setText(person1.getLoanDate());
                        txtDateD.setInputType(InputType.TYPE_NULL);
                        txtPhoneNoD.setText(person1.getPhoneNo());
                        txtAmountD.setHint("0.0");

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
                                String amount = txtAmountD.getText().toString();

                                if (name.isEmpty()){
                                    txtNameD.setError("Enter Name!");
                                }else if (date.isEmpty()){
                                    txtDateD.setError("Enter date!");
                                }else if (phone.isEmpty()){
                                    txtPhoneNoD.setError("Enter phone number!");
                                }else if(amount.isEmpty()){
                                    txtAmountD.setError("Enter amount");
                                } else {
                                    person1.setName(name);
                                    person1.setName(date);
                                    person1.setPhoneNo(phone);
                                    person1.setLoan("T");
                                    person1.setFriend("T");
                                    person1.setAmountLoan(Double.parseDouble(txtAmountD.getText().toString()));

                                    DatabaseHelper databaseHelper = new DatabaseHelper(activity);
                                    databaseHelper.updatePerson(person1);

                                    alertDialog.dismiss();
                                    Intent intent = new Intent(activity, LoanerListActivity.class);
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
