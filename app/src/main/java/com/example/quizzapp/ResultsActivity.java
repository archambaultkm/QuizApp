package com.example.quizzapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultsActivity extends Activity {

    private TextView tvUserTopic;
    private TextView tvScore;
    private Button btnPlayAgain;

    int totalQuestions;
    int answeredCorrectly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //instantiate widgets
        tvUserTopic = findViewById(R.id.tvUserTopic);
        tvScore = findViewById(R.id.tvUserScore);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            totalQuestions = extras.getInt("questions");
            answeredCorrectly = extras.getInt("correct");
        }

        //this will need to be read in from the text file if I keep this feature.
        tvUserTopic.setText("Imported Topic");
        //set text to be passed user score / passed total questions
        tvScore.setText(answeredCorrectly + "/" + totalQuestions);

        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //restart the same quiz
                startActivity(new Intent(ResultsActivity.this, QuizActivity.class));
            }
        });
    }
}
