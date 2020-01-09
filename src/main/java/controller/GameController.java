package controller;

import model.*;

public class GameController {
    private BoardController boardController;
    private PlayerController playerController;
    private Player activePlayer;



    private int activePlayerId;
    private DiceController diceController;
    private ChanceCardController chanceCardController;

    //Constructor initiates instances of classes that the controller controls.
    public GameController(){
        this.boardController = new BoardController();
        this.diceController = new DiceController();
        this.chanceCardController = new ChanceCardController();
    }

    //Pass-on methods (are not used for anything else than to pass it on to classes above this class)
    public BoardController getBoardController() {
        return boardController;
    }

    public void setupPlayers(String[] playerNames){
        this.playerController = new PlayerController(playerNames);
        activePlayerId =0;
        updateActivePlayer();
    }
    //Pass-on method returns dice for use in systemcontroller and viewontroller
    public int[] rollDice(){
        int[] diceValues = diceController.diceValues();
        return diceValues;
    }


    public void safePaymentToBank(int playerId,int amount){
        //Calls the method in playerController and gets a boolean. If false, the player could not afford
        boolean succes = playerController.safeTransferToBank(playerId,amount);
        if(succes==false){
            //The player could not afford to pay
        }
    }

    public void safePaymentToPlayer(int fromPlayerId, int amount, int toPlayerId){
        boolean succes = playerController.safeTransferToPlayer(fromPlayerId,amount,toPlayerId);
        if(succes==false){
            //The player could not afford to pay
        }
    }

    public void setActivePlayer(Player player){
        this.activePlayer = player;
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public DiceController getDiceController() {
        return diceController;
    }

    public void updateActivePlayer(){
        //Updates the activePlayer
        int numberOfPlayers = playerController.getPlayers().length;
        this.activePlayerId++;
        this.activePlayerId = this.activePlayerId % numberOfPlayers;
        this.activePlayer = playerController.getPlayers()[activePlayerId];

    }

    public int getActivePlayerId() {
        return activePlayerId;
    }
}
