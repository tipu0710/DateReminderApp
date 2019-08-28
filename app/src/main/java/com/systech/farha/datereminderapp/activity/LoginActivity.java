package com.systech.farha.datereminderapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.helper.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText txtEmail, txtPassword;
    private TextView txtRegister, txtAccount, txtForgot;
    Button buttonLogin;
    String userEmail, password;

    SessionManager session;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeView();
    }

    private void initializeView() {

        session = new SessionManager(getApplicationContext());
        databaseHelper = new DatabaseHelper(getApplicationContext());

        if (session.isLoggedIn()){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }

        txtEmail = findViewById(R.id.txt_email);
        txtPassword = findViewById(R.id.txt_password);
        buttonLogin = findViewById(R.id.button_login);
        txtRegister = findViewById(R.id.txt_register);
        txtAccount  = findViewById(R.id.txt_account) ;
        txtForgot  = findViewById(R.id.txt_forgot) ;

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmail = txtEmail.getText().toString();
                password = txtPassword.getText().toString();

                if (userEmail.isEmpty()){
                    txtEmail.setError("Enter Email");
                }else if (password.isEmpty()){
                    txtPassword.setError("Enter password");
                }else {
                    if (databaseHelper.checkUser(userEmail)){
                        if(databaseHelper.validateUser(userEmail, password)){

                            int userId = databaseHelper.getUserIdByEmail(userEmail);

                            session.storeLoginSession(String.valueOf(userId), userEmail);

                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                }
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
        txtAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, PasswordRecoveryActivity.class));
            }
        });

    }

}
