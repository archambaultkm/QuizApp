package com.example.quizzapp;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class QuizManager {

    private final ArrayList<String> questions = new ArrayList<>();
    private final ArrayList<String> answers = new ArrayList<>();

    private final HashMap<String, String> pairs = new HashMap<>();

    private final int NUM_CHOICES = 4;
    private ArrayList<String> choices;

    private String currentQuestion;
    private final int totalQuestions;

    public QuizManager(Context context) {

        loadTextFile(context);

        createHash();

        Collections.shuffle(questions);
        currentQuestion = questions.get(0);
        totalQuestions = questions.size();
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public String getCurrentQuestion() {
        return currentQuestion;
    }

    public String getCurrentAnswer() {
        return pairs.get(currentQuestion);
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void removeAnsweredQuestion() {
        questions.remove(0);
        currentQuestion = questions.get(0);
    }

    public void createChoiceSet() {

        choices = new ArrayList<>(NUM_CHOICES);
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
    }

    private void loadTextFile(Context context) {

        InputStream inputStream;
        BufferedReader reader = null;

        try {
            inputStream = context.getAssets().open("TestQandAs.txt");

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

            Log.e("FileIOError", "Error with file io");

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    Log.e("BRClose", "Error closing BufferedReader");
                }
            }
        }
    }

    private void createHash() {

        //create relationship between questions and answers, used to retrieve correct answer
        for (int i=0; i<questions.size();i++) {
            pairs.put(questions.get(i), answers.get(i));
        }

    }
}
