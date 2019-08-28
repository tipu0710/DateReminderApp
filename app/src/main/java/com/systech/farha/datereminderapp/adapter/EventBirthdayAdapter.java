package com.systech.farha.datereminderapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.alarm.SetAlarm;
import com.systech.farha.datereminderapp.model.Person;

import java.util.List;

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
        ViewHolder holder = new ViewHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(personList.get(position).getName());
        holder.txtDate.setText(personList.get(position).getFriendDate());
        holder.txtPhoneNo.setText(personList.get(position).getPhoneNo());
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
        public ViewHolder(@NonNull View view) {
            super(view);
            txtName = view.findViewById(R.id.txt_person_name);
            txtPhoneNo = view.findViewById(R.id.txt_person_phone_no);
            txtDate = view.findViewById(R.id.txt_person_date);
            txtTime = view.findViewById(R.id.txt_person_time);
        }
    }
}
