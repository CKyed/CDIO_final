package controller;

import model.*;
import static Utilities.PathExpert.*;
import static Utilities.FileReader.readFile;

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

    public CardDeck getCardDeck() {
        return cardDeck;
    }

    public String playCard(int cardId,PlayerController playerController, Board board){
        String message ="";

        //Switches on the chanceCardId
        switch (cardId){
            case 1:

                break;
            case 2:
                break;

            case 11:
            case 12:
                //Deposits 500 to player
                playerController.getActivePlayer().getAccount().deposit(500);
                break;
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
                //Deposits 1000 to player
                playerController.getActivePlayer().getAccount().deposit(1000);
                break;
            case 21:
                //Deposits 3000 to player
                playerController.getActivePlayer().getAccount().deposit(3000);
                break;
            case 22:
                //Deposits 200 to player
                playerController.getActivePlayer().getAccount().deposit(200);
                break;
            case 23:
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
            case 41:
            case 42:
                //Gives the player the "get out of prison" card
                playerController.getActivePlayer().setPrisonCard(true);
                message = String.format(readFile(turnMessagesPath,"getsPrisonCard"),playerController.getActivePlayer().getName());
                break;
        }
        return message;

    }
}
