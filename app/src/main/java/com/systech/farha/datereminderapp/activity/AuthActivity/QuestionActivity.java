package com.systech.farha.datereminderapp.activity.AuthActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.activity.Others.BorrowerListActivity;
import com.systech.farha.datereminderapp.activity.Others.MainActivity;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.model.User;

import static com.systech.farha.datereminderapp.activity.Others.MainActivity.REG_PREFS_NAME;

public class QuestionActivity extends AppCompatActivity {

    EditText firstQuestion, firstAnswer, secondQuestion, secondAnswer;
    Button nextButton, skipBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        firstQuestion = findViewById(R.id.question_1);
        secondQuestion = findViewById(R.id.question_2);
        firstAnswer = findViewById(R.id.answer_1);
        secondAnswer = findViewById(R.id.answer_2);
        nextButton = findViewById(R.id.question_next);
        skipBtn = findViewById(R.id.question_skip);

        if(getIntent().getBooleanExtra("fromEdit", false)){
            int id = getIntent().getIntExtra("id", -1);
            User user = new DatabaseHelper(this).getUserById(id);
            firstQuestion.setText(user.getQuestion1());
            secondQuestion.setText(user.getQuestion2());
            firstAnswer.setText(user.getAnswer1());
            secondAnswer.setText(user.getAnswer2());
            nextButton.setText("Change");
            skipBtn.setVisibility(View.GONE);
        }

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
                builder.setTitle("Attention! If you skip you will not be able to recover your password.");

                builder.setPositiveButton("SKIP", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createPreference("", "", "", "", true);
                        startActivity(new Intent(QuestionActivity.this, ProfileActivity.class));
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question1 = firstQuestion.getText().toString();
                String answer1 = firstAnswer.getText().toString();
                String question2 = secondQuestion.getText().toString();
                String answer2 = secondAnswer.getText().toString();

                if (question1.isEmpty()){
                    firstQuestion.setError("This field is required!");
                }else if (answer1.isEmpty()){
                    firstAnswer.setError("This field is required!");
                }else if (question2.isEmpty()){
                    secondQuestion.setError("This field is required!");
                }else if (answer2.isEmpty()){
                    secondAnswer.setError("This field is required!");
                }else {
                    if (getIntent().getBooleanExtra("fromEdit", false)){
                        int id = getIntent().getIntExtra("id", -1);
                        DatabaseHelper databaseHelper = new DatabaseHelper(QuestionActivity.this);
                        User user = databaseHelper.getUserById(id);
                        user.setQuestion1(question1);
                        user.setQuestion2(question2);
                        user.setAnswer1(answer1);
                        user.setAnswer2(answer2);
                        user.setQuestionSkipped(false);
                        if (databaseHelper.updateUser(user)){
                            Toast.makeText(QuestionActivity.this, "Update Successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(QuestionActivity.this, MainActivity.class));
                            finish();
                        }
                    }else {
                        createPreference(question1, answer1, question2, answer2, false);
                        startActivity(new Intent(QuestionActivity.this, ProfileActivity.class));
                    }

                }
            }
        });
    }

    private void createPreference(String ques1,String ans1, String ques2, String ans2, boolean isQuestionSkipped) {
        SharedPreferences preferences = getSharedPreferences(REG_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor ed;
        ed = preferences.edit();
        ed.putString("ques1",ques1);
        ed.putString("ans1",ans1);
        ed.putString("ques2",ques2);
        ed.putString("ans2",ans2);
        ed.putBoolean("isQuestionSkipped",isQuestionSkipped);
        ed.apply();
    }
}
