package com.example.quizzapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvUserTopic;
    private Button btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiate widgets
        tvUserTopic = findViewById(R.id.tvUserTopic);
        btnPlay = findViewById(R.id.btnPlay);

        //this will need to be read in from the text file if I keep this feature.
        tvUserTopic.setText("This is the imported topic");

        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //use intent to move to next Screen
                startActivity(new Intent(MainActivity.this, QuizActivity.class));
            }
        }); //end onclick


    }//end oncreate
}