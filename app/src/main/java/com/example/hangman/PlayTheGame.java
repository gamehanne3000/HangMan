package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class PlayTheGame extends AppCompatActivity {

    private Variable variable;
    ImageView ivHangmanSequence;
    TextView txtwordToBeGuessed;
    EditText editInput;
    TextView txtLetterTried;
    TextView txtTriesLeft;
    Button btnloadAndUpdateTest; // knappen har skapats för att visa att sharedpreference fungerar

    public static final String SHARED_PREF = "SHARED_PREF";
    public static final String SAVE_TRIES_LEFT = "SAVE_TRIES_LEFT";
    public static final String WORD_TO_BE_GUESSED = "WORD_TO_BE_GUESSED";
    public static final String LETTERS_TRIED = "LETTERS_TRIED";
    public static final String PICTURE_INDEX = "PICTURE_INDEX";

    void init() {
        Collections.shuffle(variable.myListOfWords); // Slumpgenerera ett ord i från txt filen
        variable.wordToBeGuessed = variable.myListOfWords.get(0); // hämta ordet
        variable.myListOfWords.remove(0); // ta bort det så att det inte dycker upp igen

        // Initiera startbilden
        ivHangmanSequence = findViewById(R.id._Hang_sequence);
        ivHangmanSequence.setImageResource(R.drawable.gallow0);
        variable.pictureCounter = 1;


        // Initiera char array för att enklare tilldela varje bokstav '-'
        variable.wordDisplayCharArray = variable.wordToBeGuessed.toCharArray();
        for (int i = 0; i < variable.wordDisplayCharArray.length; i++) {
            variable.wordDisplayCharArray[i] = '-';
        }

        // omvandla "wordDisplayCharArray" till string och visa ordet då metoden (tar emot String)
        variable.wordDisplayedString = String.valueOf(variable.wordDisplayCharArray);
        displayWordOnScreen();

        // Töm eventuell bokstav
        editInput.setText("");

        // Bokstäver som har använts:
        // initiera en string för de bokstäver som har testats med ett SPACE
        // när jag vill söka efter input letter .. och se ifall det ligger innaför "letterTried"
        // vid indexOf() metoden så söker programmet inte efter en tom sträng
        variable.letterTried = " ";
        txtLetterTried.setText(variable.MESSAGE_WITH_LETTERS_TRIED);

        // initiera strängen för "txtTriesLeft"
        variable.triesLeft = 10;
        String messageToDisplayTriesLeft = (variable.triesLeft + variable.MESSAGE_WITH_TRIES_LEFT);
        txtTriesLeft.setText(messageToDisplayTriesLeft);

    }

    void revealLetterInWord(char letter) {

        // Tar index och och hämtar från string och kollar om det finns fler än en av samma bokstav
        int indexOfLetter = variable.wordToBeGuessed.indexOf(letter); // nummer som representerar index av bokstaven
        while (indexOfLetter >= 0) {
            variable.wordDisplayCharArray[indexOfLetter] = variable.wordToBeGuessed.charAt(indexOfLetter);
            indexOfLetter = variable.wordToBeGuessed.indexOf(letter, indexOfLetter + 1);
        }
        // Uppdatera strängen också
        variable.wordDisplayedString = String.valueOf(variable.wordDisplayCharArray);
    }

    void displayWordOnScreen() {
        String formattedString = "";
        for (char character : variable.wordDisplayCharArray) {
            formattedString += character + " ";
        }
        txtwordToBeGuessed.setText(formattedString);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_the_game);

            // Initiera variablerna
            variable = Variable.getInstance();
            variable.myListOfWords = new ArrayList<>();
            txtwordToBeGuessed = findViewById(R.id._txtWordToBeGuessed); // finns redan i metoden wordGenrator
            editInput = findViewById(R.id._editInput);
            txtLetterTried = findViewById(R.id._txtLetterTried);
            txtTriesLeft = findViewById(R.id._TriesLeft);
            btnloadAndUpdateTest = findViewById(R.id._loadAndUpdate);

            // Sammanlinka med databasen och populera array med det givna orden i "database_file.txt"
            InputStream myInputStream = null;
            Scanner input = null;
            String aWord;

            // Om filen finns ta in orden som finns i textfilen.
            try {
                myInputStream = getAssets().open("Words_file.txt");
                input = new Scanner(myInputStream);

                // Sålänge det finns ett ord så forsätt annars stoppa loop
                while (input.hasNext()) {
                    aWord = input.next();
                    variable.myListOfWords.add(aWord);
                }

            } catch (IOException e) {
                // Typ av felmeddelande som uppstod och skickar ut ett meddelande motsvarat feltypen
                Toast.makeText(PlayTheGame.this, e.getClass().getSimpleName() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();

            } finally {
                // För att slippa ödslad minneshantering stänger jag ner Scanner så länge inte något att skickats in
                if (input != null) {
                    input.close();
                }

                // Stäng ner "myInputStream om filen har skapats"
                try {
                    if (myInputStream != null) {
                        myInputStream.close();
                    }

                } catch (IOException e) {
                    Toast.makeText(PlayTheGame.this, e.getClass().getSimpleName() + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        btnloadAndUpdateTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDataAnsUpdate();
            }
        });

        editInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // kollar om en bokstav har skrivits in i ( PlainText "editInput")
                if (s.length() != 0) {
                    checkIfLetterInWord(s.charAt(0));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        init();
    }

    void checkIfLetterInWord(char letter) {

        // if letter is in alreadyGuessedString
        // return;

        // Om bokstaven hittades i ordet som gissas
        if (variable.wordToBeGuessed.indexOf(letter) >= 0) {

            // Uppdatera om bokstaven inte finns i ordet (0 = utanför string)
            if (variable.wordDisplayedString.indexOf(letter) < 0) {
                // byt ut '-' med rätt gissad bokstav
                revealLetterInWord(letter);
                displayWordOnScreen();

                winOrLoss();
            }
        }
        // Om INTE bokstaven hittades i "wordToBeGuessed"
        else {
            decreaseAndDisplayTriesLeft();
            winOrLoss();
        }
        // skriv ut de gissade bokstäverna
        if (variable.letterTried.indexOf(letter) < 0) {
            variable.letterTried += letter + ", ";
            String messageToBeDisplayed = (variable.MESSAGE_WITH_LETTERS_TRIED + variable.letterTried);
            txtLetterTried.setText(messageToBeDisplayed);
        }
    }

    /*
     *   Knappen "gissa" lyder med följande kod:
     *      - Ny bild visas vid varje felgissad bokstav
     *      - Antalet försök registreas också här
     */
    public void decreaseAndDisplayTriesLeft() {
        if (variable.triesLeft >= 0) {
            // int pictureId = får värdet av bildens nummer i mappen drawable som ligger i package (drawable) och därmed (index på bilder)
            int pictureId = getResources().getIdentifier("gallow" + variable.pictureCounter, "drawable", getPackageName());
            ivHangmanSequence.setImageResource(pictureId); // skriver ut respektive bild beroende på bildnummer
            variable.pictureCounter++;

            // Antal försök kvar
            variable.triesLeft--;
            String messageToDisplayTriesLeft = (variable.triesLeft + " Tries left");
            txtTriesLeft.setText(messageToDisplayTriesLeft);
        }
    }

    public void winOrLoss() {
        // Kollar om man har förlorat
        if (variable.triesLeft <= 0) {
            ivHangmanSequence.setImageResource(R.drawable.gallow0);

            // initiera överföring av data för både antalet försök och det rätta ordet till aktiviten "resultat"
            String youLoseToResultActivity = variable.RESULT_MESSAGE_LOSS;
            String triesLeftToResultActivity = variable.triesLeft + " " + variable.MESSAGE_WITH_TRIES_LEFT;
            String rightWordToResultActivity = variable.RIGHT_WORD + variable.wordToBeGuessed;
            Intent intent = new Intent(this, Result.class);
            intent.putExtra("loss", youLoseToResultActivity);
            intent.putExtra("triesLeft", triesLeftToResultActivity);
            intent.putExtra("rightWord", rightWordToResultActivity);
            startActivity(intent);
        } else {

            // Kollar om man har vunnit (! = om det inte finns några '-' kvar i "wordToBeGuessed")
            if (!variable.wordDisplayedString.contains("-")) {
                String youWonToResultActivity = variable.RESULT_MESSAGE_WON;
                String triesLeftToResultActivity = variable.triesLeft + " " + variable.MESSAGE_WITH_TRIES_LEFT;
                String rightWordToResultActivity = variable.RIGHT_WORD + variable.wordToBeGuessed;
                Intent intent = new Intent(this, Result.class);
                intent.putExtra("won", youWonToResultActivity);
                intent.putExtra("triesLeft", triesLeftToResultActivity);
                intent.putExtra("rightWord", rightWordToResultActivity);
                startActivity(intent);
            }
        }
    }

    /*
     * Spara datan mha sharedPreferences
     */
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        variable.messageToDisplayTriesLeft = (variable.triesLeft + variable.MESSAGE_WITH_TRIES_LEFT);
        int pictureId = getResources().getIdentifier("gallow" + variable.pictureCounter, "drawable", getPackageName());

        // spara undan värderna
        editor.putInt(PICTURE_INDEX, pictureId);
        editor.putString(WORD_TO_BE_GUESSED, txtwordToBeGuessed.getText().toString());
        editor.putString(SAVE_TRIES_LEFT, variable.messageToDisplayTriesLeft);
        editor.putString(LETTERS_TRIED, txtLetterTried.getText().toString());

        editor.apply();
        Toast.makeText(PlayTheGame.this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    /*
     * Hämta datan mha sharedPreferences
     * HJÄLP = är if statement korrekt och också isåfall ska jag initiera spelet mha ("else" init)
     */
    public void loadDataAnsUpdate() {
        SharedPreferences sharePref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);

        // ifall det finns något sparat = visa
        if (sharePref != null){
            variable.currentPicture = sharePref.getInt(PICTURE_INDEX, 0);
            variable.DisplayCurrentWord = sharePref.getString(WORD_TO_BE_GUESSED, "");
            variable.triesLeftToString = sharePref.getString(SAVE_TRIES_LEFT, "");
            variable.getLettersTried = sharePref.getString(LETTERS_TRIED, "");

            // uppdatera elementet
            ivHangmanSequence.setImageResource(variable.currentPicture);
            txtwordToBeGuessed.setText(variable.DisplayCurrentWord);
            txtTriesLeft.setText(variable.triesLeftToString);
            txtLetterTried.setText(variable.getLettersTried);

            Toast.makeText(PlayTheGame.this, "Data loaded and updated", Toast.LENGTH_SHORT).show();
            Toast.makeText(PlayTheGame.this, variable.currentPicture, Toast.LENGTH_LONG).show();
        } else {
            init();
        }
    }

    /*
     * extra kod som inte är nödvändigt eller klar
     * spara värden i vertikalt läge med följande "onRestoreInstanceState" och "onSaveInstanceState" fungerar halvt och designen vertikalt är inte bra
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        variable.messageToDisplayTriesLeft = (variable.triesLeft + variable.MESSAGE_WITH_TRIES_LEFT);
        int pictureId = getResources().getIdentifier("gallow" + variable.pictureCounter, "drawable", getPackageName());

        // Spara undan värderna
        savedInstanceState.putInt(PICTURE_INDEX, pictureId );
        savedInstanceState.putString(WORD_TO_BE_GUESSED, txtwordToBeGuessed.getText().toString());
        savedInstanceState.putString(SAVE_TRIES_LEFT, variable.messageToDisplayTriesLeft);
        savedInstanceState.putString(LETTERS_TRIED, txtLetterTried.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        // hämtar värden
        variable.currentPicture = savedInstanceState.getInt(PICTURE_INDEX, 0);
        variable.DisplayCurrentWord = savedInstanceState.getString(WORD_TO_BE_GUESSED);
        variable.triesLeftToString = savedInstanceState.getString(SAVE_TRIES_LEFT);
        variable.getLettersTried = savedInstanceState.getString(LETTERS_TRIED);

        // uppdatera elementet
        ivHangmanSequence.setImageResource(variable.currentPicture);
        txtwordToBeGuessed.setText(variable.DisplayCurrentWord);
        txtTriesLeft.setText(variable.triesLeftToString);
        txtLetterTried.setText(variable.getLettersTried);

        super.onRestoreInstanceState(savedInstanceState);
    }

    /*
     * om mitt program inte skulle ändra mina sparade värden skulle jag välja
     * att använda koden nedan istället för knappen

    protected void onStart() {
        super.onStart();
        loadDataAnsUpdate();
    }
    */

    protected void onStop(){
        super.onStop();
        saveData();
    }
}



/*
    -> antal försök kvar minskar om jag trycker på samma bokstav som är fel flera ggr
*/


