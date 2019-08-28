package com.systech.farha.datereminderapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.systech.farha.datereminderapp.R;
import com.systech.farha.datereminderapp.database.DatabaseHelper;
import com.systech.farha.datereminderapp.model.User;

import static com.systech.farha.datereminderapp.activity.MainActivity.REG_PREFS_NAME;

public class QuestionActivity extends AppCompatActivity {

    EditText firstQuestion, firstAnswer, secondQuestion, secondAnswer;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        firstQuestion = findViewById(R.id.question_1);
        secondQuestion = findViewById(R.id.question_2);
        firstAnswer = findViewById(R.id.answer_1);
        secondAnswer = findViewById(R.id.answer_2);
        nextButton = findViewById(R.id.question_next);

        if(getIntent().getBooleanExtra("fromEdit", false)){
            nextButton.setText("Change");
        }

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
                        if (databaseHelper.updateUser(user)){
                            Toast.makeText(QuestionActivity.this, "Update Successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(QuestionActivity.this, MainActivity.class));
                            finish();
                        }
                    }else {
                        createPreference(question1, answer1, question2, answer2);
                        startActivity(new Intent(QuestionActivity.this, ProfileActivity.class));
                    }

                }
            }
        });
    }

    private void createPreference(String ques1,String ans1, String ques2, String ans2) {
        SharedPreferences preferences = getSharedPreferences(REG_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor ed;
        ed = preferences.edit();
        ed.putString("ques1",ques1);
        ed.putString("ans1",ans1);
        ed.putString("ques2",ques2);
        ed.putString("ans2",ans2);
        ed.apply();
    }
}
