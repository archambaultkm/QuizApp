package com.example.quizzapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity {

    private static final String TAG = "QuizActivity";

    private TextView tvUserTopic, tvQuestion;
    private ProgressBar pb;
    private Button btnAns1, btnAns2, btnAns3, btnAns4, btnNext;

    QuizManager quizManager;

    int progressCount;
    int correctlyAnsweredCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //instantiate views
        tvUserTopic = findViewById(R.id.tvUserTopic);
        tvQuestion = findViewById(R.id.tvQuestion);

        pb = findViewById(R.id.pbProgress);

        btnAns1 = findViewById(R.id.btnA1);
        btnAns2 = findViewById(R.id.btnA2);
        btnAns3 = findViewById(R.id.btnA3);
        btnAns4 = findViewById(R.id.btnA4);
        btnNext = findViewById(R.id.btnNext);

        //set onclick listeners
        btnAns1.setOnClickListener(onAnswerClicked);
        btnAns2.setOnClickListener(onAnswerClicked);
        btnAns3.setOnClickListener(onAnswerClicked);
        btnAns4.setOnClickListener(onAnswerClicked);

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //on app creation make a constant number for total starting questions
                if (progressCount == quizManager.getTotalQuestions()) {

                    Intent i = new Intent(QuizActivity.this, ResultsActivity.class);

                    //carry over count of correctly answered questions
                    i.putExtra("questions", quizManager.getTotalQuestions());
                    i.putExtra("correct", correctlyAnsweredCount);

                    startActivity(i);

                } else {

                    quizManager.removeAnsweredQuestion();
                    newQuestion();
                }
            }
        }); //end onclick)

        quizManager = new QuizManager(getApplicationContext());
        //the QuizManager class will:
        //load the file, create arraylists and hash, and handle randomizing questions/answers

        //set the bounds of the progress bar
        pb.setMax(quizManager.getTotalQuestions());

        //initialize the count of questions answered correctly
        correctlyAnsweredCount = 0;

        //fill tvs and buttons with values
        newQuestion();

    }//end oncreate

    public View.OnClickListener onAnswerClicked = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            //check the answer on the button against the real answer, apply colours to buttons.
            Button clickedButton = (Button)view;
            String buttonText = clickedButton.getText().toString();

            //for each button
            if (buttonText.matches(quizManager.getCurrentAnswer())) {
                //clickedButton.setBackground(green button drawable);
                //doesn't work:
                //clickedButton.setBackgroundColor(getResources().getColor(R.color.green));
                Toast.makeText(QuizActivity.this,"Correct!",Toast.LENGTH_SHORT).show();
                correctlyAnsweredCount += 1;

            } else {
                //clickedButton.setBackground(red button drawable);
                Toast.makeText(QuizActivity.this,"Incorrect!",Toast.LENGTH_SHORT).show();
            }

            //disable buttons
            //underline text to remind user which button they clicked
            btnNext.setVisibility(View.VISIBLE);
        }
    }; //end onclick

    private void newQuestion() {

        btnNext.setVisibility(View.INVISIBLE);

        progressCount += 1;
        pb.setProgress(progressCount);

        tvQuestion.setText(quizManager.getCurrentQuestion());

        //I had to do this bc calling this method internally in the
        // getchoices was randomizing the return and I was getting duplicates.
        //I need to find a way to not have to do this:
        quizManager.createChoiceSet();

        btnAns1.setText(quizManager.getChoices().get(0));
        btnAns2.setText(quizManager.getChoices().get(1));
        btnAns3.setText(quizManager.getChoices().get(2));
        btnAns4.setText(quizManager.getChoices().get(3));
    }

}
