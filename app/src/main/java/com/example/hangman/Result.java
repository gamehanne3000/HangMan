package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Result extends AppCompatActivity {

    TextView txtWin;
    TextView txtLoss;
    TextView txtWordToBeGuessedShown;
    TextView txtTriesLeftShown;
    Button btnGetBackToMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        txtWin = findViewById(R.id._win);
        txtLoss = findViewById(R.id._loss);
        txtTriesLeftShown = findViewById(R.id._triesLeftShown);
        txtWordToBeGuessedShown = findViewById(R.id._rightWord);
        btnGetBackToMenu = findViewById(R.id._btnReturn);

        Intent intent = getIntent();

        // passa antalet försök över till denna aktiviteten
        String retrieveThatIWon = intent.getStringExtra("won");
        String retrieveThatILoss = intent.getStringExtra("loss");
        String retrieveTriesLeft = intent.getStringExtra("triesLeft");
        String retrieveRightWord = intent.getStringExtra("rightWord");
        txtWin.setText(retrieveThatIWon);
        txtLoss.setText(retrieveThatILoss);
        txtTriesLeftShown.setText(retrieveTriesLeft);
        txtWordToBeGuessedShown.setText(retrieveRightWord);

        /*
         * Gå tillbaka till Huvudmenyn
         */
        btnGetBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Result.this, MainActivity.class));
            }
        });
    }
}
