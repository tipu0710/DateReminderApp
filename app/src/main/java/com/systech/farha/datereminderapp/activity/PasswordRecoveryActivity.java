package com.systech.farha.datereminderapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.database.DatabaseHelper;

public class PasswordRecoveryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);
        final EditText email = findViewById(R.id.recovery_email);
        Button nextBtn = findViewById(R.id.recovery_next);

        final DatabaseHelper databaseHelper = new DatabaseHelper(this);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = email.getText().toString();
                if (mail.isEmpty()){
                    email.setError("Enter your login email!");
                }else {
                    if (databaseHelper.checkUser(mail)){
                        int id = databaseHelper.getUserIdByEmail(mail);
                        Intent intent = new Intent(PasswordRecoveryActivity.this, RecoverQuestionActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }else {
                        Toast.makeText(PasswordRecoveryActivity.this, "This email dose'nt exist", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
}
