package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void itsAboutMe(View v){
        Intent intent = new Intent(this, AboutMe.class);
        startActivity(intent);
        Log.d("aboutMe knapp", "button pressed");
    }

    public void startGame(View v){
        Intent intent = new Intent(this, PlayTheGame.class);
        finish();
        startActivity(intent);
        Log.d("playTheGame knapp", "spela");
    }


}
