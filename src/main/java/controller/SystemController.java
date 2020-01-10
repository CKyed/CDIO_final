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

        //Land on field
        switch (activeFieldType){
            case "street":
                playPropertyField();





                break;
            case "ferry":



                break;




        }
    }




    //What does this getter do here? Can someone please explain later. Ida
    public GameController getGameController() {
        return gameController;
    }
}
