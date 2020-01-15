package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardDeckTest {
public String[] testCardTexts = {"test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test"};
    @Test
    void swap() {
        assertTrue(true);

    }

     @Test
    void shuffleTest() {
        //Tests that cards are randomly shuffled
        CardDeck deck = new CardDeck(testCardTexts);
        int[] occurences = new int[testCardTexts.length];

        for (int i = 0; i < 10000 ; i++) {
            //Shuffles deck
            deck.shuffle();

            //Draws upper card and gets id - increments occurence
            occurences[deck.draw().getId()]++;
        }

        for (int i = 0; i < testCardTexts.length; i++) {
            assertTrue(occurences[i]<240 && occurences[i]> 160);
        }

    }

    @Test
    void draw() {
        int numberOfCards = testCardTexts.length;
        int numberOfDraws = 10000;

        //Tests that cards are put back in the bottom after being drawn
        int[] occurences = new int[testCardTexts.length];
        int occurence;

        CardDeck deck = new CardDeck(testCardTexts);
        for (int i = 0; i < numberOfDraws ; i++) {
            //puts top card in bottom
            deck.draw();

            //The occurence is the tpo card
            occurence= deck.getChanceCards()[0].getId();

            //increments occurence
            occurences[occurence]++;
        }

        for (int i = 0; i < testCardTexts.length; i++) {
            assertTrue(occurences[i]<(numberOfDraws/numberOfCards)+1 );
            assertTrue(occurences[i]> (numberOfDraws/numberOfCards)-1);
        }



    }
}