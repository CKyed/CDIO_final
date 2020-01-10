package controller;

import model.Fields.Ownable;
import static controller.PathExpert.*;
import static controller.TextController.readFile;

public class SystemController {
    private GameController gameController;
    private ViewController viewController;
    private int numberOfPlayers;


    public SystemController(){
        this.gameController = new GameController();


        this.viewController = new ViewController(this.gameController.getBoardController().getBoard());
        setupPlayers();
        play();
    }
    //Setup players with an array of Strings witch the setupPlayers method in viewcontroller returns
    public void setupPlayers(){
        String[] playerNames = this.viewController.setupPlayers();
        gameController.setupPlayers(playerNames);
    }

    public void play(){

        int activePlayerId;
        //Plays turns
        while (true){
            activePlayerId = gameController.getActivePlayerId();

            if(gameController.getActivePlayer().isInJail()){

            } else{
                playTurn();
            }

            //Updates the balances and ownerships of all Players
            viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());
            viewController.updateOwnerships(gameController.getBoardController().getBoard());

            //Gives the turn to the next player
            gameController.updateActivePlayer();



        }



    }

    public void playTurn(){
        int activePlayerId = gameController.getActivePlayerId();
        int[] faceValues;
        int sum;
        int oldFieldId;

        //Plays turns
        while (true){
            //Gets die Roll and updates view
            faceValues = gameController.rollDice();
            oldFieldId = gameController.getActivePlayer().getPositionOnBoard();


            viewController.rollDiceAndMove(faceValues,activePlayerId,oldFieldId);

        landOnField();

        //Updates the balances of all Players
        viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());

    }

    public void playPropertyField(){


        if(gameController.getOwnerId()>=0 && gameController.getOwnerId()!= gameController.getActivePlayerId()){

            //If the property is owned by someone else
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


        } else if (gameController.getOwnerId()==-1){
            //If it is vacant - asks if player wants to buy

            viewController.updateOwnerships();

            //Gives the turn to the next player
            gameController.updateActivePlayer();
            activePlayerId = gameController.getActivePlayerId();

                //Updates the owner
                int currentFieldId = gameController.getActivePlayer().getPositionOnBoard();
                ((Ownable)gameController.getBoardController().getBoard().getFields()[currentFieldId]).setOwnerId(gameController.getActivePlayerId());
            }





        } else{
            //If the player owns it himself
            System.out.println("hej");



        }

    public void movePlayer(int[] diceValues){
        int playerStepsToMove = diceValues[0]+diceValues[1];
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
                gameController.movePlayer(30,20);

                break;
        }

        if(cantAfford==false){
            //TODO: Should handle if the players can't afford to pay
        }
    }




    //What does this getter do here? Can someone please explain later. Ida
    public GameController getGameController() {
        return gameController;
    }
}
