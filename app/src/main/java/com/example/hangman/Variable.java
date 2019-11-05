package com.example.hangman;

import java.util.ArrayList;

public class Variable {
    private static final Variable ourInstance = new Variable();

    /* variabler för _PlayTheGame */
    int pictureCounter = 2; // börjar efter menybilden
    int triesLeft = 10;
    String wordToBeGuessed; // ligger i bakgrunden
    String wordDisplayedString; // vad som visas på skärmen
    String letterTried;
    char[] wordDisplayCharArray; // för att underlätta att ändra bokstäverna till '-'
    ArrayList<String> myListOfWords;
    final String MESSAGE_WITH_LETTERS_TRIED = "Letters tried: ";
    final String RESULT_MESSAGE_WON = "You won the game";
    final String RESULT_MESSAGE_LOSS = "You loss the game";
    final String RIGHT_WORD = "The right word was: ";

    static Variable getInstance() {
        return ourInstance;
    }

    private Variable() {
    }
}
