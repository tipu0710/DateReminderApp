package com.systech.farha.datereminderapp.activity.AuthActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.helper.SessionManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.systech.farha.datereminderapp.activity.Others.MainActivity.REG_PREFS_NAME;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtName, userNameEt, txtEmail, txtPass, txtConfPass;
    Button buttonRegister;

    private String name, email, pass, confPass, userName;
    private boolean userNameChecker, emailChecker, validEmail;

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
        txtEmail = findViewById(R.id.txt_user_name_login);
        txtPass = findViewById(R.id.txt_password);
        txtConfPass = findViewById(R.id.txt_conf_password);
        userNameEt = findViewById(R.id.txt_user_name);

        buttonRegister = findViewById(R.id.button_register);

      buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = txtName.getText().toString();
                email = txtEmail.getText().toString();
                pass = txtPass.getText().toString();
                confPass = txtConfPass.getText().toString();
                userName = userNameEt.getText().toString();

                if (name.isEmpty()){
                    txtName.setError("Enter Name!");
                }else if (userName.isEmpty() || !userNameChecker){
                    txtEmail.setError("Enter unique user name");
                }else if (!validEmail && !email.isEmpty()){
                    txtEmail.setError("Enter valid email!");
                }else if (!emailChecker && !email.isEmpty()){
                    txtEmail.setError("This email is already used!");
                }else if (pass.isEmpty()){
                    txtPass.setError("Enter password");
                }else if (confPass.isEmpty()){
                    txtConfPass.setError("Enter password again");
                }else {
                    if (!pass.equals(confPass)){
                        Toast.makeText(getApplicationContext(), "Password Not Matched!", Toast.LENGTH_LONG).show();
                    } else {

                        if (databaseHelper.checkUserName(userName)){
                            Toast.makeText(getApplicationContext(), "User Already Exists!", Toast.LENGTH_LONG).show();
                        } else {
                            if (email.isEmpty()){
                                email = "";
                            }
                           createPreference(name, userName, email, pass);
                           startActivity(new Intent(RegisterActivity.this, QuestionActivity.class));
                        }
                    }
                }
            }
        });


      userNameEt.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void afterTextChanged(Editable editable) {
              if (editable.length()>0){
                  if (databaseHelper.checkUserName(editable.toString())){
                      userNameEt.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable. ic_close_black_24dp, 0);
                      userNameChecker = false;
                  }else {
                      userNameEt.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable. ic_check_black_24dp, 0);
                      userNameChecker = true;
                  }
              }

          }
      });
      txtEmail.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void afterTextChanged(Editable editable) {
              validEmail = isEmailValid(editable.toString());
              if (!editable.toString().isEmpty()){
                  if (databaseHelper.checkUserEmail(editable.toString()) || !validEmail){
                      txtEmail.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable. ic_close_black_24dp, 0);
                      emailChecker = false;
                  }else {
                      txtEmail.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable. ic_check_black_24dp, 0);
                      emailChecker = true;
                  }
              }else {
                  txtEmail.setCompoundDrawablesWithIntrinsicBounds( 0, 0, 0, 0);
              }
          }
      });

    }

    private void createPreference(String name, String userName,String email, String password) {
        SharedPreferences preferences = getSharedPreferences(REG_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor ed;
        if (!preferences.contains("name")){
            ed = preferences.edit();
            ed.putString("name",name);
            ed.putString("userName",userName);
            ed.putString("password",password);
            ed.putString("email",email);
            ed.apply();
        }
    }

    public static boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }

}
