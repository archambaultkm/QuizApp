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

    private final ArrayList<String> questions;
    private final ArrayList<String> answers;

    private final HashMap<String, String> pairs;

    private ArrayList<String> choices;

    private String currentQuestion;
    private int totalQuestions;

    public QuizManager() {

        this.pairs = new HashMap<>();
        this.questions = new ArrayList<>();
        this.answers = new ArrayList<>();
    }

    public ArrayList<String> getChoices() {
        createChoiceSet();
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

    public void loadQuiz(Context context) {

        //parse the text file into q/a pairs
        loadTextFile(context);
        createHash();

        //randomize the order of questions
        Collections.shuffle(questions);
        currentQuestion = questions.get(0);
        totalQuestions = questions.size();
    }

    public void removeAnsweredQuestion() {
        //burn down the used question
        questions.remove(0);
        //move to next available question
        currentQuestion = questions.get(0);
    }

    //create a random new set of answers for a new question, and include the correct answer
    private void createChoiceSet() {

        int NUM_CHOICES = 4;
        choices = new ArrayList<>(NUM_CHOICES);
        Random rand = new Random();

        //add the correct answer to available choices
        choices.add(pairs.get(currentQuestion));

        //populate the rest of the choices with random answers
        for (int i = 0; i<(NUM_CHOICES -1); i++) {

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

    //parse delimited text file and populate arraylists
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
                    Log.e("BRClose", "Error closing BufferedReader/file");
                }
            }
        }
    }

    //create relationship between questions and answers, used to retrieve correct answer
    private void createHash() {

        for (int i=0; i<questions.size();i++) {
            pairs.put(questions.get(i), answers.get(i));
        }
    }
}
