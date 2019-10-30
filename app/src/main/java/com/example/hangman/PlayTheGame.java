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

    private Variable varible;
    ImageView ivHangmanSequence;
    TextView txtwordToBeGuessed;
    EditText editInput;
    TextView txtLetterTried;
    TextView txtTriesLeft;
    public static final String SHARED_PREF = "sharedPrefs";
    public static final String TRIES_LEFT = "TRIES_LEFT";
    public static final String WORD_TO_BE_GUESSED = "WORD_TO_BE_GUESSED";
    public static final String LETTERS_TRIED = "LETTERS_TRIED";
    public static final String PICTURE_INDEX = "PICTURE_INDEX";
    String hej = (varible.triesLeft + "ssssss");

    void initilizeGame() {
        // Slumpgenerera ett ord i array list och få tag på första elementet, ta sedan ta bort det så att det inte dycker upp igen.
        Collections.shuffle(varible.myListOfWords);
        varible.wordToBeGuessed = varible.myListOfWords.get(0);
        varible.myListOfWords.remove(0);

        // 1. initiera bilden
        ivHangmanSequence = findViewById(R.id._Hang_sequence); /* -> */
        ivHangmanSequence.setImageResource(R.drawable.gallow0);
        varible.pictureCounter = 1;


        // 2. initiera char array för att enklare tilldela varje bokstav '-'
        varible.wordDisplayCharArray = varible.wordToBeGuessed.toCharArray();
        for (int i = 0; i < varible.wordDisplayCharArray.length; i++) {
            varible.wordDisplayCharArray[i] = '-';
        }

        revealLetterInWord(varible.wordDisplayCharArray[0]);
        revealLetterInWord(varible.wordDisplayCharArray[varible.wordDisplayCharArray.length - 1]);

        // omvandla "wordDisplayCharArray" till string och visa ordet då metoden (tar emot String)
        varible.wordDisplayedString = String.valueOf(varible.wordDisplayCharArray);
        displayWordOnScreen();

        // 3. Töm eventuell bokstav
        editInput.setText("");

        // 3. Bokstäver som har använts
        // initiera en string för de för dem bokstäver som har testats med ett SPACE
        // när jag vill söka efter input letter .. och se ifall det ligger innaför "letterTried"
        // vid indexOf() metoden så söker programmet inte efter en tom sträng
        varible.letterTried = " ";

        // display on screen
        txtLetterTried.setText(varible.MESSAGE_WITH_LETTERS_TRIED);

        // 4. Försök kvar
        // initiera strängen för "txtTriesLeft"
        varible.triesLeft = 10;
        String messageToDisplayTriesLeft = (varible.triesLeft + varible.MESSAGE_WITH_TRIES_LEFT);
        txtTriesLeft.setText(messageToDisplayTriesLeft);
    }

    void revealLetterInWord(char letter) {

        int indexOfLetter = varible.wordToBeGuessed.indexOf(letter); // nummer som representerar index av bokstaven

        //tar index och och hämtar från string med samma ord + kolla om det finns fler än en av samma bokstav
        while (indexOfLetter >= 0) {
            varible.wordDisplayCharArray[indexOfLetter] = varible.wordToBeGuessed.charAt(indexOfLetter);
            indexOfLetter = varible.wordToBeGuessed.indexOf(letter, indexOfLetter + 1);
        }
        // uppdatera strängen också
        varible.wordDisplayedString = String.valueOf(varible.wordDisplayCharArray);
    }

    void displayWordOnScreen() {
        String formattedString = "";
        for (char character : varible.wordDisplayCharArray) {
            formattedString += character + " ";
        }
        txtwordToBeGuessed.setText(formattedString);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_the_game);

        // Initiera variablerna
        varible = Variable.getInstance();
        varible.myListOfWords = new ArrayList<>();
        txtwordToBeGuessed = findViewById(R.id._txtWordToBeGuessed); // finns redan i metoden wordGenrator
        editInput = findViewById(R.id._editInput);
        txtLetterTried = findViewById(R.id._txtLetterTried);
        txtTriesLeft = findViewById(R.id._TriesLeft);

        // sammanlinka med databasen och populera array med det givna orden i "database_file.txt"
        InputStream myInputStream = null;
        Scanner input = null;
        String aWord;

        // om filen finns skicka in vad som finns i textfilen.
        try {
            myInputStream = getAssets().open("Words_file.txt");
            input = new Scanner(myInputStream);

            // sålänge det finns ett ord så forsätt annars stoppa loop
            while (input.hasNext()) {
                aWord = input.next();
                varible.myListOfWords.add(aWord);
            }
        } catch (IOException e) {
            // typ av felmeddelande som uppstod och skickar ut ett meddelande motsvarat feltypen
            Toast.makeText(PlayTheGame.this, e.getClass().getSimpleName() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            // för att slippa ödslad minneshantering stänger jag ner Scanner så länge inte något att skickats in
            if (input != null) {
                input.close();
            }
            //Stäng ner "myInputStream"
            try {
                // Om filen har skapats så vill jag kunna stänga ner den.
                if (myInputStream != null) {
                    myInputStream.close();
                }
            } catch (IOException e) {
                Toast.makeText(PlayTheGame.this, e.getClass().getSimpleName() + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        initilizeGame();


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

        if (savedInstanceState != null){
            //loadDataAnsUpdate();
        }else
        {
            // hdvfj
        }
    }

    void checkIfLetterInWord(char letter) {

        // if letter is in alreadyGuessedString
        // return;

        // Om bokstaven hittades i ordet som gissas
        if (varible.wordToBeGuessed.indexOf(letter) >= 0) {

            // Om bokstaven inte finns i ordet (0 = utanför string)
            if (varible.wordDisplayedString.indexOf(letter) < 0) {
                // byt ut '-' med rätt gissad bokstav
                revealLetterInWord(letter);

                // Uppdatera ändringarna på skärmen
                displayWordOnScreen();

                winOrLoss();
            }
        }
        // Om INTE bokstaven hittades i "wordToBeGuessed"
        else {
            decreaseAndDisplayTriesLeft();
            winOrLoss();
        }
        // Om bokeatven finns i "wordToBeGuessed, print out"
        if (varible.letterTried.indexOf(letter) < 0) {
            varible.letterTried += letter + ", ";
            /* Kod missas -> sänk inte på antalet försök men vet ej hur jag gör*/
            String messageToBeDisplayed = (varible.MESSAGE_WITH_LETTERS_TRIED + varible.letterTried);
            txtLetterTried.setText(messageToBeDisplayed);
        }
    }

    /*
     *   Knappen "gissa" lyder med följande kod:
     *   - ny bild visas vid varje felgissad bokstav    -> kan ej uppdatera bilderna efter att ett spel är färdig
     *   - antalet försök registreas också här          -> antal försök kvar minskar även
     */
    public void decreaseAndDisplayTriesLeft() {
        if (varible.triesLeft >= 0) {
            // int pictureId = får värdet av bildens nummer i mappen drawable som ligger i package (drawable) (index på bilder)
            int pictureId = getResources().getIdentifier("gallow" + varible.pictureCounter, "drawable", getPackageName());
            ivHangmanSequence.setImageResource(pictureId); // skriver ut respektive bild beroende på bildnummer
            varible.pictureCounter++;

            // antal försök kvar
            varible.triesLeft--;
            String messageToDisplayTriesLeft = (varible.triesLeft + " Tries left");
            txtTriesLeft.setText(messageToDisplayTriesLeft);
        }
    }

    public void winOrLoss() {
        // kollar om man har förlorat
        if (varible.triesLeft <= 0) {
            ivHangmanSequence.setImageResource(R.drawable.gallow0);
            // initiera överföring av data för både antalet försök och det rätta ordet till aktiviten "resultat"
            String youLoseToResultActivity = varible.RESULT_MESSAGE_LOSS;
            String triesLeftToResultActivity = varible.triesLeft + " " + varible.MESSAGE_WITH_TRIES_LEFT;
            String rightWordToResultActivity = varible.RIGHT_WORD + varible.wordToBeGuessed;
            Intent intent = new Intent(this, Result.class);
            intent.putExtra("loss", youLoseToResultActivity);
            intent.putExtra("triesLeft", triesLeftToResultActivity);
            intent.putExtra("rightWord", rightWordToResultActivity);
            startActivity(intent);
        } else {
            // kollar om man har vunnit (! = om det inte finns några '-' kvar i "wordToBeGuessed")
            if (!varible.wordDisplayedString.contains("-")) {
                String youWonToResultActivity = varible.RESULT_MESSAGE_WON;
                String triesLeftToResultActivity = varible.triesLeft + " " + varible.MESSAGE_WITH_TRIES_LEFT;
                String rightWordToResultActivity = varible.RIGHT_WORD + varible.wordToBeGuessed;
                Intent intent = new Intent(this, Result.class);
                intent.putExtra("won", youWonToResultActivity);
                intent.putExtra("triesLeft", triesLeftToResultActivity);
                intent.putExtra("rightWord", rightWordToResultActivity);
                startActivity(intent);
            }
        }
    }

    /*
     * Följande methoder "saveData" och "loadData" och "updateData" bygger på sharedPreferences
     */
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        varible.prefHiddenWord = txtwordToBeGuessed.getText().toString();
        varible.prefLetterTried = txtLetterTried.getText().toString();

        editor.putString(WORD_TO_BE_GUESSED, varible.prefHiddenWord);
        editor.putInt(TRIES_LEFT, varible.triesLeft);
        editor.putString(LETTERS_TRIED, varible.prefLetterTried);

        editor.apply();
        Toast.makeText(PlayTheGame.this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    public void loadDataAnsUpdate() {
        SharedPreferences sharePref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);

        varible.setHiddenWord = txtwordToBeGuessed.getText().toString();
        varible.setletterTried = txtLetterTried.getText().toString();

        int pictureId = getResources().getIdentifier("gallow" + varible.pictureCounter, "drawable", getPackageName());
        ivHangmanSequence.setImageResource(pictureId);

        sharePref.getString(WORD_TO_BE_GUESSED,varible.setHiddenWord);
        //sharePref.getString(TRIES_LEFT, varible.triesLeftString);
        sharePref.getString(LETTERS_TRIED, varible.setletterTried);
        sharePref.getInt(PICTURE_INDEX, pictureId);

        //varible.triesLeftString = Integer.toString(varible.triesLeft);
        varible.currentPictureString = Integer.toString(varible.pictureCounter);

        ivHangmanSequence.setImageResource(pictureId);
        txtwordToBeGuessed.setText(varible.setHiddenWord);
        //txtTriesLeft.setText(varible.triesLeftString);
        txtLetterTried.setText(varible.setletterTried);

        Toast.makeText(PlayTheGame.this, "Data loaded and updated", Toast.LENGTH_SHORT).show();
    }

    public void updateData() {


        Toast.makeText(PlayTheGame.this, "Data uodated", Toast.LENGTH_SHORT).show();
    }

    /*
     * extra kod som inte är nödvändigt eller klar
     * spara värden i vertikalt läge med följande "onRestoreInstanceState" och "onSaveInstanceState" fungerar halvt och designen vertikalt är inte bra
     */

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        varible.setHiddenWord = txtwordToBeGuessed.getText().toString();
        varible.setletterTried = txtLetterTried.getText().toString();


        savedInstanceState.putString(WORD_TO_BE_GUESSED, varible.setHiddenWord);
        savedInstanceState.putString(TRIES_LEFT, hej);
        savedInstanceState.putString(LETTERS_TRIED, varible.setletterTried);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        savedInstanceState.getString(WORD_TO_BE_GUESSED);
        savedInstanceState.getString(TRIES_LEFT);
        savedInstanceState.getString(LETTERS_TRIED);

        txtwordToBeGuessed.setText(savedInstanceState.getString(WORD_TO_BE_GUESSED));
        hej = savedInstanceState.getString(TRIES_LEFT);
        txtLetterTried.setText(savedInstanceState.getString(LETTERS_TRIED));

        super.onRestoreInstanceState(savedInstanceState);
    }

    protected void onPause() {
        super.onPause();

    }

    protected void onRestart() {
        super.onRestart();

    }

    protected void onStop(){
        super.onStop();
        saveData();
    }

}


// This callback is called only when there is a saved instance that is previously saved by using
// onSaveInstanceState(). We restore some state in onCreate(), while we can optionally restore
// other state here, possibly usable after onStart() has completed.
// The savedInstanceState Bundle is same as the one used in onCreate().



/*
    -> antal försök kvar minskar om jag trycker på samma bokstav som är fel flera ggr
    -> spara värden i vertikalt läge med följande "onRestoreInstanceState" och "onSaveInstanceState" fungerar halvt och designen vertikalt är inte bra
    -> Följande methoder "saveData" och "loadData" och "updateData" bygger på sharedPreferences men har svårt att implementera det
    -> Kod missas -> sänk inte på antalet försök men vet ej hur jag gör
    / kan inte spara undan bilden
*/


