package com.systech.farha.datereminderapp.activity.AuthActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class PasswordRecoveryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);
        final EditText userNameEt = findViewById(R.id.recovery_email);
        Button nextBtn = findViewById(R.id.recovery_next);

        final DatabaseHelper databaseHelper = new DatabaseHelper(this);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = userNameEt.getText().toString();
                if (userName.isEmpty()){
                    userNameEt.setError("Enter your username!");
                }else {
                    if (databaseHelper.checkUserName(userName)){
                        int id = databaseHelper.getUserIdByUserName(userName);
                        User user = databaseHelper.getUserById(id);
                        if (user.isQuestionSkipped()){
                            showWarning();
                        }else {
                            Intent intent = new Intent(PasswordRecoveryActivity.this, RecoverQuestionActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                        }
                    }else {
                        Toast.makeText(PasswordRecoveryActivity.this, "User name dose'nt exist", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    private void showWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PasswordRecoveryActivity.this);
        builder.setTitle("You cannot change your password. Please try to remember your password and login.");

        builder.setPositiveButton("GO TO LOGIN PAGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(PasswordRecoveryActivity.this, LoginActivity.class));
            }
        });


        builder.setNegativeButton("Or, create another account", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(PasswordRecoveryActivity.this, RegisterActivity.class));
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
