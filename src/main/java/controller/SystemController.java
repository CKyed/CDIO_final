package controller;

import model.Fields.Ownable;

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
/**
 * If the player is in jail, try to pay the bail, and set inJail to false
 * If the player can't pay the bail, then the player loses (to be written)
 */
            if(gameController.getActivePlayer().isInJail()){
                boolean success = gameController.payBail(activePlayerId);
                gameController.getPlayerController().getPlayers()[activePlayerId].setInJail(false);

                if(success == false){
                    //TODO Method for handling loser-condition is called here
                }

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

        //Gets dieRoll and updates view and logic
        oldFieldId = gameController.getActivePlayer().getCurrentFieldId();
        faceValues = gameController.rollDice();
        sum = gameController.getDiceController().getSum();

        viewController.rollDiceAndMove(faceValues,sum,activePlayerId,oldFieldId);

        landOnField();

        //Updates the balances of all Players
        viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());

    }

    public void playPropertyField(){
        if(gameController.getOwnerId()>=0 && gameController.getOwnerId()!= gameController.getActivePlayerId()){
            //If the property is owned by someone else

        } else if (gameController.getOwnerId()==-1){
            //If it is vacant - asks if player wants to buy

            if (viewController.buyFieldOrNot(gameController.getActivePlayerId(),gameController.getActivePlayer().getCurrentFieldId())){
                //If he chooses to buy

                //Withdraws money
                gameController.buyFieldForPlayer();

                //Updates the owner
                int currentFieldId = gameController.getActivePlayer().getCurrentFieldId();
                ((Ownable)gameController.getBoardController().getBoard().getFields()[currentFieldId]).setOwnerId(gameController.getActivePlayerId());
            }


        } else{
            //If the player owns it himself

        }

    }

    public void landOnField(){
        String activeFieldType = gameController.getBoardController().getBoard().getFields()[gameController.getActivePlayer().getCurrentFieldId()].getType();
        int activePlayer = gameController.getActivePlayerId();
        boolean cantAfford=true;

        //Land on field
        switch (activeFieldType) {
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
                int oldFieldId = gameController.getActivePlayer().getCurrentFieldId();
                int virutalFaceValues[] = {10,10};
                viewController.rollDiceAndMove(virutalFaceValues,20,activePlayer,oldFieldId);
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
