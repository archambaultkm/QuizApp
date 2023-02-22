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

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class QuizActivity extends Activity {

    private final int NUM_CHOICES = 4;
    private static final String TAG = "QuizActivity";

    private TextView tvUserTopic, tvQuestion;
    private ProgressBar pb;
    private Button btnAns1, btnAns2, btnAns3, btnAns4, btnNext;

    private final ArrayList<String> questions = new ArrayList<>();
    private final ArrayList<String> answers = new ArrayList<>();

    private final HashMap<String, String> pairs = new HashMap<>();

    String currentQuestion;

    int progressCount;
    int totalQuestions;
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
                if (progressCount == totalQuestions) {

                    Intent i = new Intent(QuizActivity.this, ResultsActivity.class);

                    //carry over count of correctly answered questions
                    i.putExtra("questions", totalQuestions);
                    i.putExtra("correct", correctlyAnsweredCount);

                    startActivity(i);

                } else {

                    questions.remove(0);
                    //call resetui function
                    newQuestion();
                }
            }
        }); //end onclick)

        //read in values from text file and fill arraylists
        loadTextFile();

        //link questions and answers
        createHash();

        //randomize order of questions
        Collections.shuffle(questions);
        currentQuestion = questions.get(0);

        totalQuestions = questions.size();

        //set the bounds of the progress bar
        pb.setMax(totalQuestions);

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
            if (buttonText.matches(pairs.get(currentQuestion))) {
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

    private void loadTextFile() {

        InputStream inputStream;
        BufferedReader reader = null;

        try {
            inputStream = getAssets().open("TestQandAs.txt");

            if (inputStream != null) {

                reader = new BufferedReader(new InputStreamReader(inputStream));

                //while there are still lines to read, add q/a to respective arraylists
                String line;
                String[] pair;

                while ((line = reader.readLine()) != null) {

                    pair = line.split("\\$\\$");

                    questions.add(pair[0]);
                    answers.add(pair[1]);
                }
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void createHash() {

        //create relationship between questions and answers
        for (int i=0; i<questions.size();i++) {
            pairs.put(questions.get(i), answers.get(i));
        }

    }

    private void newQuestion() {

        //probably move to "update ui" method
        btnNext.setVisibility(View.INVISIBLE);

        progressCount += 1;
        pb.setProgress(progressCount);

        currentQuestion = questions.get(0);

        ArrayList<String> choices = new ArrayList<>(4);
        Random rand = new Random();

        //add the correct answer to available choices
        choices.add(pairs.get(currentQuestion));

        //populate the rest of the choices with random answers
        for (int i = 0; i<(NUM_CHOICES-1); i++) {

            int randIndex = rand.nextInt(answers.size());

            //make sure all answers are unique
            while (choices.contains(answers.get(randIndex))) {
                randIndex = rand.nextInt(answers.size());
            }

            choices.add(answers.get(randIndex));
        }

        //randomize the order the answers will appear
        Collections.shuffle(choices);

        tvQuestion.setText(currentQuestion);

        btnAns1.setText(choices.get(0));
        btnAns2.setText(choices.get(1));
        btnAns3.setText(choices.get(2));
        btnAns4.setText(choices.get(3));
    }

}
