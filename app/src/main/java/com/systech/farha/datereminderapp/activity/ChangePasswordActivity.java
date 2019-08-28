package com.systech.farha.datereminderapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.helper.SessionManager;
import com.systech.farha.datereminderapp.model.User;

import java.util.HashMap;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText oldPassEt, newPassEt;
    Button changeBtn;

    HashMap<String, String> user;
    Integer userId;
    DatabaseHelper databaseHelper;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passwors);
        oldPassEt = findViewById(R.id.cng_old_pass);
        newPassEt = findViewById(R.id.cng_new_pass);
        changeBtn = findViewById(R.id.cng_pass_btn);


        session = new SessionManager(getApplicationContext());
        databaseHelper = new DatabaseHelper(getApplicationContext());
        user = session.getLoginDetails();
        userId = Integer.valueOf(user.get(SessionManager.KEY_ID));

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldpass = oldPassEt.getText().toString();
                String newPass = newPassEt.getText().toString();
                User user = databaseHelper.getUserById(userId);

                if (oldpass.isEmpty()){
                    oldPassEt.setError("Type your old password");
                }else if (newPass.isEmpty()){
                    newPassEt.setError("Type your new password");
                }else {
                    if (user.getPassword().equals(oldpass)){
                        user.setPassword(newPass);
                        if (databaseHelper.updateUser(user)){
                            Toast.makeText(ChangePasswordActivity.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                            session.logoutUser();
                        }else {
                            Toast.makeText(ChangePasswordActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Incorrect password!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
