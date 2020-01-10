package controller;

import model.*;

public class GameController {
    private BoardController boardController;
    private PlayerController playerController;
    private Player activePlayer;
    private int startBonus = 2000;



    private int activePlayerId;
    private DiceController diceController;
    private ChanceCardController chanceCardController;

    public GameController(){
        this.boardController = new BoardController();
        this.diceController = new DiceController();
        this.chanceCardController = new ChanceCardController();
    }

    public BoardController getBoardController() {
        return boardController;
    }

    public void setupPlayers(String[] playerNames){
        this.playerController = new PlayerController(playerNames);
        activePlayerId =0;
        updateActivePlayer();
    }

    public int[] rollDice(){
        diceController.roll();
        int currentFieldId = this.activePlayer.getCurrentFieldId();
        int dieSum = diceController.getSum();
        int numberOfFields = this.boardController.getBoard().getFields().length;

        //Calculates new field and adds startbonus if player passed start
        int newFieldId = (currentFieldId+dieSum)%numberOfFields;
        this.activePlayer.setCurrentFieldId(newFieldId);
        if (currentFieldId+dieSum>numberOfFields){
            activePlayer.deposit(startBonus);
        }




        return diceController.getFaceValues();
    }


    /**
     * Calls the method safeTransferToBank in playerController that returns a boolean that tells if the player has enough money on account
     * If false, the player could not afford the
     * @param playerId
     * @param amount
     */

    public void safePaymentToBank(int playerId,int amount){
        boolean succes = playerController.safeTransferToBank(playerId,amount);
        if(succes==false){
            //The player could not afford to pay
            //TODO logic what happens when player goes bankrupt
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
        //Updates the activePlayer - only if last diceroll wasn't 2 of the same
        if(!diceController.isSameValue()){
            int numberOfPlayers = playerController.getPlayers().length;
            this.activePlayerId++;
            this.activePlayerId = this.activePlayerId % numberOfPlayers;
            this.activePlayer = playerController.getPlayers()[activePlayerId];
        }


    }

    public int getActivePlayerId() {
        return activePlayerId;
    }
}
