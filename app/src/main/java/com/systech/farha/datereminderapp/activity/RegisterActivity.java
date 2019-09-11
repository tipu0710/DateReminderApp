package com.systech.farha.datereminderapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.helper.SessionManager;
import static com.systech.farha.datereminderapp.activity.MainActivity.REG_PREFS_NAME;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtName, txtEmail, txtPass, txtConfPass;
    Button buttonRegister;

    private String name, email, pass, confPass;

    DatabaseHelper databaseHelper;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeView();

    }

    private void initializeView() {

        session = new SessionManager(getApplicationContext());
        databaseHelper = new DatabaseHelper(getApplicationContext());

        txtName = findViewById(R.id.txt_name);
        txtEmail = findViewById(R.id.txt_email);
        txtPass = findViewById(R.id.txt_password);
        txtConfPass = findViewById(R.id.txt_conf_password);

        buttonRegister = findViewById(R.id.button_register);

      buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = txtName.getText().toString();
                email = txtEmail.getText().toString();
                pass = txtPass.getText().toString();
                confPass = txtConfPass.getText().toString();

                if (name.isEmpty()){
                    txtName.setError("Enter Name!");
                }else if (email.isEmpty()){
                    txtEmail.setError("Enter email");
                }else if (pass.isEmpty()){
                    txtPass.setError("Enter password");
                }else if (confPass.isEmpty()){
                    txtConfPass.setError("Enter password again");
                }else {
                    if (!pass.equals(confPass)){
                        Toast.makeText(getApplicationContext(), "Password Not Matched!", Toast.LENGTH_LONG).show();
                    } else {

                        if (databaseHelper.checkUser(email)){
                            Toast.makeText(getApplicationContext(), "User Already Exists!", Toast.LENGTH_LONG).show();
                        } else {
                           createPreference(name, email, pass);
                           startActivity(new Intent(RegisterActivity.this, QuestionActivity.class));
                        }
                    }
                }
            }
        });
    }

    private void createPreference(String name,String email, String password) {
        SharedPreferences preferences = getSharedPreferences(REG_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor ed;
        if (!preferences.contains("name")){
            ed = preferences.edit();
            ed.putString("name",name);
            ed.putString("password",password);
            ed.putString("email",email);
            ed.apply();
        }
    }

}
