package controller;

import model.ChanceCard;
import model.Fields.Ownable;
import static controller.PathExpert.*;
import static controller.TextController.readFile;

public class SystemController {
    private GameController gameController;
    private ViewController viewController;
    private int numberOfPlayers;


    public SystemController(){
        this.gameController = new GameController();
        this.viewController = new ViewController( gameController.getBoardController().getBoard() );

        //Setup players with an array of Strings from the viewcontroller
        String[] playerNames = this.viewController.setupPlayers();
        gameController.setPlayerController(new PlayerController(playerNames));

        //Plays game
        play();
    }

    public void play(){

        int activePlayerId = gameController.getActivePlayerId();
        //Plays turns
        while (true){
            //Displays message, showing who's turn it is
            viewController.newTurnMessage(gameController.getActivePlayerId());

            //If player owns streets, player can choose to buy or sell houses first
            buyBeforeTurn();

            //If the player is in jail
            if(gameController.getActivePlayer().isInJail()){
                boolean success = gameController.payBail(activePlayerId);
                gameController.getPlayerController().getPlayers()[activePlayerId].setInJail(false);

                if(success == false){
                    //TODO Method for handling loser-condition is called here
                }

            } else{
                //If not in jail, turn starts normally
                playTurn();
            }

            //Updates the balances and ownerships of all Players
            viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());
            viewController.updateOwnerships(gameController.getBoardController().getBoard());

            //Gives the turn to the next player
            gameController.updateActivePlayer();
        }
    }

    public void buyBeforeTurn(){
        //Gets array of id's of streets that can be built on
        int[] buildableStreetIds = gameController.getBoardController().getBuildableStreetIds(gameController.getActivePlayerId());

        //If the player can build and wants to
        if (buildableStreetIds.length != 0 && viewController.chooseToBuy(gameController.getActivePlayerId())){
            int wantedNumberOfHouses;
            //Goes through all buildable streets and asks for the wanted number of houses
            for (int i=0;i<buildableStreetIds.length;i++){
                wantedNumberOfHouses = viewController.getWantedNumberOfHouses(buildableStreetIds[i],gameController.getActivePlayerId());
                if(wantedNumberOfHouses!=0){
                    boolean succes = gameController.tryToBuyHouses(buildableStreetIds[i],wantedNumberOfHouses);
                    if (succes){
                        viewController.showMessage(String.format(readFile(turnMessagesPath,"buildingSucceeded"),wantedNumberOfHouses));
                        viewController.updateOwnerships(gameController.getBoardController().getBoard());
                    } else{
                        viewController.showMessage(String.format(readFile(turnMessagesPath,"notPossibleToBuild"),wantedNumberOfHouses));
                        //If player asked for something impossible, counter doesn't update
                        i--;

                    }
                }


            }


        }

    }

    public void playTurn(){
        int activePlayerId = gameController.getActivePlayerId();
        int oldFieldId = gameController.getActivePlayer().getPositionOnBoard();

        //Gets dieRoll and updates players position in model-layer
        int[] faceValues = gameController.rollDice();
        int sum = gameController.getDiceController().getSum();

        //Updates players position in view-layer
        viewController.rollDiceAndMove(faceValues,sum,activePlayerId,oldFieldId);

        //Does actions on new field
        landOnField();

//        if (gameController.getActivePlayer().getAccount().getBalance() > 0){
//            // Check the player's balance, because if the balance = 0
//            // so this player was a looser, therefor we don't need to call landOnField
//            landOnField((oldFieldId + sum) % 40, activePlayerId);// TODO fieldID throw parameter
//        } TODO check for for working


        //Updates the balances of all Players
        viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());

    }

    public void playPropertyField() {

        //If the property is owned by someone else
        if (gameController.getOwnerId() >= 0 && gameController.getOwnerId() != gameController.getActivePlayerId()) {
            //Gets data
            int fieldId = gameController.getActivePlayer().getPositionOnBoard();
            String fromPlayerName = gameController.getActivePlayer().getName();
            String toPlayerName = gameController.getPlayerController().getPlayers()[gameController.getOwnerId()].getName();
            int amount = ((Ownable) gameController.getBoardController().getBoard().getFields()[fieldId]).getRent();

            //Tries to pay rent
            if (gameController.getPlayerController().safeTransferToPlayer( gameController.getActivePlayerId(), amount, gameController.getOwnerId() )) {
                //Displays message
                String message = String.format( readFile( turnMessagesPath, "payRentFromTo" ), fromPlayerName, amount, toPlayerName );
                viewController.showMessage( message );

                //Updates player balances
                viewController.updatePlayerBalances( gameController.getPlayerController().getPlayerBalances() );
            } else {
                //Player can't afford the rent
                //Looser message
                looserSituation();


            }
            //If it is vacant - asks if player wants to buy
        } else if (gameController.getOwnerId() == -1) {
            //If it is vacant - asks if player wants to buy

            //If he chooses to buy
            if (viewController.buyFieldOrNot( gameController.getActivePlayerId(), gameController.getActivePlayer().getPositionOnBoard() )) {
                //If he chooses to buy

                //Withdraws money
                // return can pay = true or can't pay = false
                if (gameController.buyFieldForPlayer()) {
                    //Updates the owner
                    int currentFieldId = gameController.getActivePlayer().getPositionOnBoard();
                    ((Ownable) gameController.getBoardController().getBoard().getFields()[currentFieldId]).setOwnerId( gameController.getActivePlayerId() );
                } else {
                    //Player can't afford buying field
                    //Looser message
                    looserSituation( );
                }

            } else {//If the player owns it himself
                String selfOwnedMessage = readFile( turnMessagesPath, "selfOwned" );
                selfOwnedMessage = String.format( selfOwnedMessage, gameController.getActivePlayer().getName(),
                        gameController.getBoardController().getBoard().getFields()[gameController.getActivePlayer().getPositionOnBoard()].getName() );
                viewController.showMessage( selfOwnedMessage );

            }

        }
    }

    public void landOnField(){
        String activeFieldType = gameController.getBoardController().getBoard().getFields()[gameController.getActivePlayer().getPositionOnBoard()].getType();
        int activePlayer = gameController.getActivePlayerId();
        boolean cantAfford=true;

        //Land on field
        switch (activeFieldType){
            case "street":
                playPropertyField();


                break;
            case "ferry":

                break;
            case "incomeTax":
                //TODO: Add correct text message here
                boolean choice = viewController.payIncomeTax("Test message");
                cantAfford = gameController.payIncomeTax(activePlayer, choice);
                break;
            case "ordinaryTax":
                //TODO: Add some text message
                cantAfford = gameController.payOrdinaryTax(activePlayer);
                break;
            case "prison":
                    //TODO add text-message
                gameController.getPlayerController().getPlayers()[activePlayer].setInJail(true);
                int oldFieldId = gameController.getActivePlayer().getPositionOnBoard();
                gameController.movePlayer(30,20);
                int virutalFaceValues[] = {10,10};
                viewController.rollDiceAndMove(virutalFaceValues,20,activePlayer,oldFieldId);
                break;
            case "chance":
                playChanceCard();
                break;

        }

        if(cantAfford==false){
            //TODO: Should handle if the players can't afford to pay
            //Player can't afford the tax
            //Looser message
            looserSituation();
        }
    }

    public void playChanceCard(){
        ChanceCard card = gameController.getChanceCardController().getCardDeck().draw();
        int cardId = card.getId();
        String cardText = card.getText();

        //Shows chancecard
        viewController.showChanceCard(cardText);

        //Shows message telling that player landed on Chance
        String landedOnChanceMsg = readFile(turnMessagesPath,"landedOnChance");
        landedOnChanceMsg = String.format(landedOnChanceMsg,gameController.getPlayerController().getPlayers()[gameController.getPlayerController().getActivePlayerId()].getName());
        viewController.showMessage(landedOnChanceMsg);

        //If it is a complicated chance card
        if(cardId ==50 ||cardId==51){

        } else{
            String message = gameController.getChanceCardController().playCard(cardId,gameController.getPlayerController(),gameController.getBoardController().getBoard());
            if (!message.isEmpty()) {
                viewController.showMessage(message);
            }

        }


    }






    // Handel looser situation
    public void looserSituation(){
        int fieldId = gameController.getActivePlayer().getPositionOnBoard();

        gameController.getPlayerController().accountReset(gameController.getActivePlayerId());
        viewController.looserMessage(fieldId);
        viewController.removeLoser( gameController.getActivePlayerId(), fieldId);
    }
}
