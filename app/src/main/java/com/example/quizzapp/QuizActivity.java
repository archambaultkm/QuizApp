package com.example.quizzapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity {

    private TextView tvQuestion, tvProgressCount;
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
        tvQuestion = findViewById(R.id.tvQuestion);
        tvProgressCount = findViewById(R.id.tvProgressCount);

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

                //so long as the user hasn't progressed through all questions, continue asking more
                if (progressCount != quizManager.getTotalQuestions()) {

                    quizManager.removeAnsweredQuestion();
                    newQuestion();

                //move to results page once all questions are exhausted
                } else {

                    Intent i = new Intent(QuizActivity.this, ResultsActivity.class);

                    //carry over count of correctly answered questions
                    i.putExtra("questions", quizManager.getTotalQuestions());
                    i.putExtra("correct", correctlyAnsweredCount);

                    startActivity(i);

                }
            }
        }); //end onclick)

        quizManager = new QuizManager(getApplicationContext());
        //the QuizManager class loads the file, create arraylists and hash, and handle randomizing questions/answers

        //set the bounds of the progress bar
        pb.setMax(quizManager.getTotalQuestions());

        //initialize the count of questions answered correctly
        correctlyAnsweredCount = 0;

        //fill tvs and buttons with values
        newQuestion();

    }//end oncreate

    //onclick for answer buttons
    public View.OnClickListener onAnswerClicked = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            Button clickedButton = (Button)view;
            String buttonText = clickedButton.getText().toString();

            //check the answer on the button against the real answer, apply colours to buttons.
            if (buttonText.matches(quizManager.getCurrentAnswer())) {

                clickedButton.setBackground(getResources().getDrawable(R.drawable.button_correct, getTheme()));
                correctlyAnsweredCount += 1;

            } else {

                clickedButton.setBackground(getResources().getDrawable(R.drawable.button_incorrect, getTheme()));
                }

            //disable buttons to prevent multiple answers on the same question
            setButtonsEnabled(false);

            btnNext.setVisibility(View.VISIBLE);
        }
    }; //end onclick

    //updates ui
    private void newQuestion() {

        btnNext.setVisibility(View.INVISIBLE);
        setButtonsEnabled(true);
        resetButtons();

        updateProgressBar();
        //see if there's a way to not need this public method from
        quizManager.createChoiceSet();

        //update the text displayed
        tvQuestion.setText(quizManager.getCurrentQuestion());
        btnAns1.setText(quizManager.getChoices().get(0));
        btnAns2.setText(quizManager.getChoices().get(1));
        btnAns3.setText(quizManager.getChoices().get(2));
        btnAns4.setText(quizManager.getChoices().get(3));
    }

    private void updateProgressBar() {
        progressCount += 1;
        pb.setProgress(progressCount);

        tvProgressCount.setText("Question " + progressCount + " of " + quizManager.getTotalQuestions());
    }

    private void resetButtons() {
        btnAns1.setBackground(getResources().getDrawable(R.drawable.button_default, getTheme()));
        btnAns2.setBackground(getResources().getDrawable(R.drawable.button_default, getTheme()));
        btnAns3.setBackground(getResources().getDrawable(R.drawable.button_default, getTheme()));
        btnAns4.setBackground(getResources().getDrawable(R.drawable.button_default, getTheme()));
    }

    private void setButtonsEnabled(boolean toggle) {
        btnAns1.setEnabled(toggle);
        btnAns2.setEnabled(toggle);
        btnAns3.setEnabled(toggle);
        btnAns4.setEnabled(toggle);
    }
}
