package com.systech.farha.datereminderapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.alarm.SetAlarm;
import com.systech.farha.datereminderapp.model.Person;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventBirthdayAdapter extends RecyclerView.Adapter<EventBirthdayAdapter.ViewHolder> {
    List<Person> personList;
    Context context;

    public EventBirthdayAdapter(List<Person> personList, Context context) {
        this.personList = personList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.person_list_item, viewGroup, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(personList.get(position).getName());
        holder.txtDate.setText(personList.get(position).getFriendDate());
        holder.txtPhoneNo.setText(personList.get(position).getPhoneNo());
        holder.proPic.setImageBitmap(BitmapFactory.decodeByteArray(personList.get(position).getProfile(), 0, personList.get(position).getProfile().length));
        String time = personList.get(position).getTimeFriend();
        if (time.isEmpty()){
            time = "0:0";
        }
        int[] t = SetAlarm.formatTime(time);
        if (t[2]==0){
            holder.txtTime.setText(t[0]+":"+t[1]+"PM");
        }else {
            holder.txtTime.setText(t[0]+":"+t[1]+"AM");
        }
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPhoneNo, txtDate, txtTime;
        CircleImageView proPic;
        public ViewHolder(@NonNull View view) {
            super(view);
            txtName = view.findViewById(R.id.txt_person_name);
            txtPhoneNo = view.findViewById(R.id.txt_person_phone_no);
            txtDate = view.findViewById(R.id.txt_person_date);
            txtTime = view.findViewById(R.id.txt_person_time);
            proPic = view.findViewById(R.id.person_list_profile);
        }
    }
}
