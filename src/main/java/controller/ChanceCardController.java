package controller;

import model.*;
import static controller.PathExpert.*;
import static controller.TextController.getFieldData;
import static controller.TextController.readFile;

public class ChanceCardController {
    private CardDeck cardDeck;


    public ChanceCardController(){
        String[] cardTexts = new String[45];
        for (int i =0;i<45;i++){
            cardTexts[i] = readFile(chanceCardPath,"card"+i);
        }

        this.cardDeck = new CardDeck(cardTexts);
        this.cardDeck.shuffle();
    }

}
