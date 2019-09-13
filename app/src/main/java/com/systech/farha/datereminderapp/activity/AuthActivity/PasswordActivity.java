package com.systech.farha.datereminderapp.activity.AuthActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.model.User;

public class PasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        final int id = getIntent().getIntExtra("id", -1);
        final EditText passEt = findViewById(R.id.new_pass);
        Button button = findViewById(R.id.change_pass_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passEt.getText().toString().isEmpty()){
                    passEt.setError("Enter new password");
                }else {
                    DatabaseHelper databaseHelper = new DatabaseHelper(PasswordActivity.this);
                    User user = databaseHelper.getUserById(id);
                    user.setPassword(passEt.getText().toString());
                    boolean b = databaseHelper.updateUser(user);
                    if (b){
                        Toast.makeText(PasswordActivity.this, "Password change successfully!", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(PasswordActivity.this, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }else {
                        Toast.makeText(PasswordActivity.this, "Some thing went wrong", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
