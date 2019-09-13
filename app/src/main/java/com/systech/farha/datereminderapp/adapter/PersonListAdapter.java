package com.systech.farha.datereminderapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.activity.Others.AddFriend;
import com.systech.farha.datereminderapp.activity.Others.FriendListActivity;
import com.systech.farha.datereminderapp.alarm.SetAlarm;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.model.Person;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonListAdapter extends BaseAdapter {

    private TextView txtName, txtPhoneNo, txtDate, txtAmount, txtTime;
    CircleImageView proPic;

    private Activity activity;
    private List<Person> personArrayList;
    private LayoutInflater inflater = null;

    public PersonListAdapter(Activity activity, List<Person> personArrayList) {
        this.activity = activity;
        this.personArrayList = personArrayList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View view = convertView;

        if (convertView == null){
            view = inflater.inflate(R.layout.person_list_item, null);

            txtName = view.findViewById(R.id.txt_person_name);
            txtPhoneNo = view.findViewById(R.id.txt_person_phone_no);
            txtDate = view.findViewById(R.id.txt_person_date);
            txtAmount = view.findViewById(R.id.txt_person_amount);
            txtTime = view.findViewById(R.id.txt_person_time);
            proPic = view.findViewById(R.id.person_list_profile);

            if (!activity.getClass().getSimpleName().equals("PersonListActivity"))
            {
                txtAmount.setVisibility(View.VISIBLE);
            }
            Person person = personArrayList.get(position);
            proPic.setImageBitmap(BitmapFactory.decodeByteArray(person.getProfile(), 0, person.getProfile().length));
            final Person finalPerson = person;


            txtName.setText(person.getName());
            txtPhoneNo.setText(person.getPhoneNo());
            txtDate.setText(person.getFriendDate());
            int[] t = SetAlarm.formatTime(person.getTimeFriend());
            if (t[2]==0){
                txtTime.setText(t[0]+":"+t[1]+"PM");
            }else {
                txtTime.setText(t[0]+":"+t[1]+"AM");
            }
            txtAmount.setVisibility(View.GONE);

            //DialogBox after onLongClicked
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
                    View dialogView =LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.long_pressed_dialog, parent, false);
                    TextView editTv = dialogView.findViewById(R.id.dialog_edit_tv);
                    TextView deleteTv = dialogView.findViewById(R.id.dialog_delete_tv);
                    TextView makePaid = dialogView.findViewById(R.id.dialog_paid_tv);

                    mBuilder.setView(dialogView);
                    final AlertDialog dialog1 = mBuilder.create();


                    makePaid.setVisibility(View.GONE);

                    editTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, AddFriend.class);
                            intent.putExtra("id",finalPerson.getId());
                            intent.putExtra("edit", true);
                            activity.startActivity(intent);
                            dialog1.dismiss();
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
                                    if (finalPerson.getLoan().equals("F") && finalPerson.getBorrow().equals("F")){
                                        databaseHelper.deletePerson(finalPerson);
                                    }else {
                                        finalPerson.setFriend("F");
                                        databaseHelper.updatePerson(finalPerson);
                                    }
                                    Intent intent = new Intent(activity, FriendListActivity.class);
                                    activity.startActivities(new Intent[]{intent});
                                }
                            });

                            builder.setNegativeButton("Cancel", null);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            dialog1.dismiss();
                        }

                    });

                    dialog1.show();
                    return true;
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }

        return view;
    }

}
