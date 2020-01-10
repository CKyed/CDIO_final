package controller;

import model.*;
import model.Fields.OrdinaryTax;
import model.Fields.Ownable;
import model.Fields.OwnableFile.Street;

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


    public int getOwnerId(){
        int activeFieldId = activePlayer.getCurrentFieldId();
        int ownerId = ((Ownable)this.boardController.getBoard().getFields()[activeFieldId]).getOwnerId();
        return ownerId;
    }

    public void buyFieldForPlayer(){
        //Gets price
        int price = ((Street)boardController.getBoard().getFields()[activePlayer.getCurrentFieldId()]).getPrice();

        //Pays
        safePaymentToBank(activePlayerId,price);

        //Gives ownership to player
        ((Street)boardController.getBoard().getFields()[activePlayer.getCurrentFieldId()]).setOwnerId(activePlayerId);



    }





    //Make prison field logic:
    //Step by step:
    // when a player lands on field no 31 (OR when player picks certain chance card,) the following happens:
    // - For the rest of the ongoing turn nothing happens
    // - Next turn he has to pay 1000 kr before rolling dice
    //
    // improvements for later:
    // 1 - The player can choose between paying the bail or try to roll a double
    // 2 - This choice is avalaible only three turns in a row, otherwise the player is forced to pay
    // 3 - The player who is imprisoned does not get rent from other players
    // 4 - The player can use chancecards to get out of Jail
    //
    // The following method should: return a boolean to tell if player is in prison or not
    // (maybe also return an int to tell how long the player has been in prison for later use)
    // if the player is in prison (System controller) then following method:
    // pay bail (1000 kr)












}


