package com.example.quizzapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class QuizActivity extends Activity {

    private static final String TAG = "QuizActivity";

    private TextView tvUserTopic, tvQuestion;
    private Button btnAns1, btnAns2, btnAns3, btnAns4;

    private final ArrayList<String> questions = new ArrayList<>();
    private final ArrayList<String> answers = new ArrayList<>();

    private final HashMap<String, String> pairs = new HashMap<>();

    String currentQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG,"Application started");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //read in values from text file and fill arraylists
        loadTextFile();

        //link questions and answers
        createHash();

        //instantiate views
        tvUserTopic = findViewById(R.id.tvUserTopic);
        tvQuestion = findViewById(R.id.tvQuestion);

        btnAns1 = findViewById(R.id.btnA1);
        btnAns2 = findViewById(R.id.btnA2);
        btnAns3 = findViewById(R.id.btnA3);
        btnAns4 = findViewById(R.id.btnA4);

        //set onclick listeners
//        btnAns1.setOnClickListener(onButtonClicked);
//        btnAns2.setOnClickListener(onButtonClicked);
//        btnAns3.setOnClickListener(onButtonClicked);
//        btnAns4.setOnClickListener(onButtonClicked);

        //fill tvs and buttons with values
        newQuestion();

    }//end oncreate

    public View.OnClickListener onButtonClicked = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            //check the answer on the button against the real answer, apply colours to buttons.
            Button clickedButton = (Button)view;
            String buttonText = clickedButton.getText().toString();

            if (buttonText.matches(pairs.get(currentQuestion))) {
                //make that button green and the rest red
                //make a toast popup with "Correct!"
            } else {
                //make that button red
                //make a toast popup with "Incorrect!"
                //display correct answer?
            }
        }
    }; //end onclick

    private void loadTextFile() {

        InputStream inputStream;
        BufferedReader reader = null;

        try {
            inputStream = getAssets().open("TestQandAs.txt");

            if (inputStream != null) {

                reader = new BufferedReader(new InputStreamReader(inputStream));

                //while there are still lines to read, add q/a pairs
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
        //add try/catch to close resources
    }

    private void createHash() {

        //create relationship between questions and answers
        for (int i=0; i<questions.size();i++) {
            pairs.put(questions.get(i), answers.get(i));
        }

    }

    private void newQuestion() {

        currentQuestion = questions.get(0);

        ArrayList<String> choices = new ArrayList<>(4);
        Random rand = new Random();

        //add the correct answer to available choices
        choices.add(pairs.get(currentQuestion));

        //populate the rest of the choices with random answers
        for (int i=0;i<3;i++) {
            choices.add(answers.get(rand.nextInt(answers.size())));
        }

        //add validation to make sure all answers are unique

        //randomize the order the answers will appear
        Collections.shuffle(choices);

        tvQuestion.setText(currentQuestion);

        btnAns1.setText(choices.get(0));
        btnAns2.setText(choices.get(1));
        btnAns3.setText(choices.get(2));
        btnAns4.setText(choices.get(3));
    }

}
