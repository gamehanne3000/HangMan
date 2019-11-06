package com.example.hangman;

import java.util.ArrayList;

public class Variable {
    private static final Variable ourInstance = new Variable();

    /* variabler för _PlayTheGame */
    int pictureCounter = 2; // börjar efter menybilden
    int triesLeft = 10;
    int currentPicture;

    String wordToBeGuessed; // ligger i bakgrunden
    String wordDisplayedString; // vad som visas på skärmen
    String letterTried;
    String DisplayCurrentWord;
    String getLettersTried;
    String triesLeftToString;
    String messageToDisplayTriesLeft = (triesLeftToString + MESSAGE_WITH_TRIES_LEFT);

    char[] wordDisplayCharArray; // för att underlätta att ändra bokstäverna till '-'
    ArrayList<String> myListOfWords;

    static final String MESSAGE_WITH_LETTERS_TRIED = "Letters tried: ";
    static final String RESULT_MESSAGE_WON = "You won the game";
    static final String RESULT_MESSAGE_LOSS = "You loss the game";
    static final String RIGHT_WORD = "The right word was: ";
    static final String MESSAGE_WITH_TRIES_LEFT = (" tries left");

    static Variable getInstance() {
        return ourInstance;
    }

    private Variable() {
    }
}
