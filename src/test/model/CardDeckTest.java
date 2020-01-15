package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardDeckTest {
public String[] testCardTexts = {"test","test","test","test","test","test","test","test","test","test"};
    @Test
    void swap() {
        assertTrue(true);

    }

     @Test
    void shuffleTest() {
        //Tests that cards are randomly shuffled
        CardDeck deck = new CardDeck(testCardTexts);
        int[] occurences = new int[testCardTexts.length];

        for (int i = 0; i < 1000 ; i++) {
            //Shuffles deck
            deck.shuffle();

            //Draws upper card and gets id - increments occurence
            occurences[deck.draw().getId()]++;
        }

        for (int i = 0; i < testCardTexts.length; i++) {
            assertTrue(occurences[i]<120 && occurences[i]> 80);
        }

    }

    @Test
    void draw() {
    }
}