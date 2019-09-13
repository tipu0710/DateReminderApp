package com.systech.farha.datereminderapp.activity.AuthActivity;

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
import com.systech.farha.datereminderapp.model.User;

public class RecoverQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_question);
        final int id = getIntent().getIntExtra("id",-1);
        TextView question1 = findViewById(R.id.recovery_question_1);
        TextView question2 = findViewById(R.id.recovery_question_2);
        final EditText answer1 = findViewById(R.id.recovery_answer_1);
        final EditText answer2 = findViewById(R.id.recovery_answer_2);
        Button button = findViewById(R.id.recovery_question_next);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        final User user = databaseHelper.getUserById(id);

        question1.setText(user.getQuestion1());
        question2.setText(user.getQuestion2());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ans1 = answer1.getText().toString();
                String ans2 = answer2.getText().toString();

                if (ans1.isEmpty()){
                    answer1.setError("You mast enter the answer!");
                }else if (ans2.isEmpty()){
                    answer2.setError("You mast enter the answer!");
                }else {
                    if (!ans1.equals(user.getAnswer1())){
                        Toast.makeText(RecoverQuestionActivity.this, "Answer dose'nt match", Toast.LENGTH_LONG).show();
                    }else if (!ans2.equals(user.getAnswer2())){
                        Toast.makeText(RecoverQuestionActivity.this, "Answer dose'nt match", Toast.LENGTH_LONG).show();
                    }else {
                        Intent intent = new Intent(RecoverQuestionActivity.this, PasswordActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
