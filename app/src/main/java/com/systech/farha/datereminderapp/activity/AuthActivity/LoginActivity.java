package com.systech.farha.datereminderapp.activity.AuthActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.activity.Others.MainActivity;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.helper.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText txtUserName, txtPassword;
    private TextView txtRegister, txtAccount, txtForgot;
    Button buttonLogin;
    String userName, password;

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

        txtUserName = findViewById(R.id.txt_user_name_login);
        txtPassword = findViewById(R.id.txt_password);
        buttonLogin = findViewById(R.id.button_login);
        txtRegister = findViewById(R.id.txt_register);
        txtAccount  = findViewById(R.id.txt_account) ;
        txtForgot  = findViewById(R.id.txt_forgot) ;

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = txtUserName.getText().toString();
                password = txtPassword.getText().toString();

                if (userName.isEmpty()){
                    txtUserName.setError("Enter username");
                }else if (password.isEmpty()){
                    txtPassword.setError("Enter password");
                }else {
                    if (databaseHelper.checkUserName(userName)){
                        if(databaseHelper.validateUser(userName, password)){

                            int userId = databaseHelper.getUserIdByUserName(userName);

                            session.storeLoginSession(String.valueOf(userId), userName);

                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            Toast.makeText(LoginActivity.this, "User name or password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, "User name not found!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
