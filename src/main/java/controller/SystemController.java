package controller;

import model.Fields.Ownable;
import static controller.PathExpert.*;
import static controller.TextController.readFile;

public class SystemController {
    private GameController gameController;
    private ViewController viewController;

    public SystemController(){
        //Initializes controllers
        gameController = new GameController();
        viewController = new ViewController(gameController.getBoardController().getBoard());

        //Setup players with an array of Strings from the viewcontroller
        String[] playerNames = this.viewController.setupPlayers();
        gameController.setPlayerController(new PlayerController(playerNames));

        //Plays game
        play();
    }

    public void play(){

        //Plays turns
        while (true){
            //Displays message, showing who's turn it is
            viewController.newTurnMessage(gameController.getActivePlayerId());

            //If player owns streets, player can choose to buy or sell houses first
            buyBeforeTurn();

            //If the player is in jail
            if(gameController.getActivePlayer().isInJail()){

            } else{ //Otherwise, player rolls dice and plays normal turn
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

        //Updates the balances of all Players
        viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());

    }

    public void playPropertyField(){

        //If the property is owned by someone else
        if(gameController.getOwnerId()>=0 && gameController.getOwnerId()!= gameController.getActivePlayerId()){
            //Gets data
            int fieldId = gameController.getActivePlayer().getPositionOnBoard();
            String fromPlayerName = gameController.getActivePlayer().getName();
            String toPlayerName = gameController.getPlayerController().getPlayers()[gameController.getOwnerId()].getName();
            int amount = ((Ownable)gameController.getBoardController().getBoard().getFields()[fieldId]).getRent();

            //Tries to pay rent
            if(gameController.getPlayerController().safeTransferToPlayer(gameController.getActivePlayerId(),amount,gameController.getOwnerId())){
                //Displays message
                String message = String.format(readFile(turnMessagesPath,"payRentFromTo"),fromPlayerName,amount,toPlayerName);
                viewController.showMessage(message);

                //Updates player balances
                viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());
            } else {
                //Player can't afford the rent
                //TODO
                System.out.println("HER SKAL VI GØRE NOGET");

            }
            //If it is vacant - asks if player wants to buy
        } else if (gameController.getOwnerId()==-1){

            //If he chooses to buy
            if (viewController.buyFieldOrNot(gameController.getActivePlayerId(),gameController.getActivePlayer().getPositionOnBoard())){

                //Withdraws money
                gameController.buyFieldForPlayer();

                //Updates the owner
                int currentFieldId = gameController.getActivePlayer().getPositionOnBoard();
                ((Ownable)gameController.getBoardController().getBoard().getFields()[currentFieldId]).setOwnerId(gameController.getActivePlayerId());
            }

        } else{//If the player owns it himself
            String selfOwnedMessage = readFile(turnMessagesPath,"selfOwned");
            selfOwnedMessage = String.format(selfOwnedMessage,gameController.getActivePlayer().getName(),
                    gameController.getBoardController().getBoard().getFields()[gameController.getActivePlayer().getPositionOnBoard()].getName());
            viewController.showMessage(selfOwnedMessage);

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
                //Player can't get out of prison atm, so it is outcommented for testing purposes
                //Also, player gets in prison when landing on visit, I think - Peter

//                gameController.getPlayerController().getPlayers()[activePlayer].setInJail(true);
//                gameController.movePlayer(30,20);
//                int oldFieldId = gameController.getActivePlayer().getPositionOnBoard();
//                int virutalFaceValues[] = {10,10};
//                viewController.rollDiceAndMove(virutalFaceValues,20,activePlayer,oldFieldId);
                break;
        }

        if(cantAfford==false){
            //TODO: Should handle if the players can't afford to pay
        }
    }



}
