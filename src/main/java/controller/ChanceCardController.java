package controller;

import model.*;
import static Utilities.PathExpert.*;
import static Utilities.FileReader.readFile;

public class ChanceCardController {
    private CardDeck cardDeck;


    public ChanceCardController(){
        String[] cardTexts = new String[26];
        for (int i =0;i<26;i++){
            cardTexts[i] = readFile(chanceCardPath,"card"+i);
        }

        this.cardDeck = new CardDeck(cardTexts);
        //this.cardDeck.shuffle();
        this.cardDeck.swap(0,24);
    }

    public CardDeck getCardDeck() {
        return cardDeck;
    }



    public String playCard(int cardId,PlayerController playerController, Board board){
        String message ="";

        //Switches on the chanceCardId
        switch (cardId){
            case 0:
            case 1:
                //Deposits 500 to player
                playerController.getActivePlayer().getAccount().deposit(500);
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                //Deposits 1000 to player
                playerController.getActivePlayer().getAccount().deposit(1000);
                break;
            case 10:
                //Deposits 3000 to player
                playerController.getActivePlayer().getAccount().deposit(3000);
                break;
            case 11:
                //Deposits 200 to player
                playerController.getActivePlayer().getAccount().deposit(200);
                break;
            case 12:
                //Calculates if players qualifies ( values >15000 )
                boolean qualifies = 15000 >= playerController.calculateTotalValue(playerController.getActivePlayerId(),board);
                if (qualifies){
                    message = readFile(turnMessagesPath,"qualifies");
                    //Deposits 40000 to player
                    playerController.getActivePlayer().getAccount().deposit(40000);
                } else{
                    message = readFile(turnMessagesPath,"qualifiesNot");
                }
                message = String.format(message,playerController.getActivePlayer().getName(),playerController.getActivePlayer().getName());

                break;
            case 13:
            case 14:
                //Gives the player the "get out of prison" card
                playerController.getActivePlayer().setPrisonCard(true);
                message = String.format(readFile(turnMessagesPath,"getsPrisonCard"),playerController.getActivePlayer().getName());
                break;

        }
        return message;

    }
}
