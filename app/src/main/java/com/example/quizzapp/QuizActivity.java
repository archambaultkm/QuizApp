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

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

public class QuizActivity extends Activity {

    private static final String TAG = "QuizActivity";

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
                Toast.makeText(QuizActivity.this, R.string.txtCorrect, Toast.LENGTH_SHORT).show();
                correctlyAnsweredCount += 1;

            } else {
                //clickedButton.setBackground(red button drawable);
                Toast.makeText(QuizActivity.this, R.string.txtIncorrect, Toast.LENGTH_SHORT).show();
            }

            //disable buttons to prevent multiple answers on the same question
            setButtonsEnabled(false);

            btnNext.setVisibility(View.VISIBLE);
        }
    }; //end onclick

    private void newQuestion() {

        btnNext.setVisibility(View.INVISIBLE);
        //resetButtons();
        setButtonsEnabled(true);

        updateProgressBar();

        tvQuestion.setText(quizManager.getCurrentQuestion());

        //I had to do this bc calling this method internally in the
        //getchoices() method was randomizing the return and I was getting duplicates
        quizManager.createChoiceSet();

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

    //only needed if you can get changing colours to work:
    //    private void resetButtons() {
//        btnAns1.setBackgroundColor(R.color);
//        btnAns2.refreshDrawableState();
//    }

    private void setButtonsEnabled(boolean toggle) {
        btnAns1.setEnabled(toggle);
        btnAns2.setEnabled(toggle);
        btnAns3.setEnabled(toggle);
        btnAns4.setEnabled(toggle);
    }
}
