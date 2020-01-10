package controller;

import model.*;
import model.Fields.IncomeTax;
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


    public boolean safePaymentToBank(int playerId,int amount){
        //Calls the method in playerController and gets a boolean. If false, the player could not afford
        boolean succes = playerController.safeTransferToBank(playerId,amount);
        if(succes==false){
            //The player could not afford to pay
        }
        return succes;
    }

    /**
     * Method that can be called when a player lands on the field called Ordinary Tax
     * @param activePlayerId
     * @return
     */

    public boolean payOrdinaryTax(int activePlayerId){
        return safePaymentToBank(activePlayerId, ((OrdinaryTax)boardController.getBoard().getFields()[38]).getTax());

    }

    //the user has chosen either 0 or 1, 0 is 4000 kr and 1 is 10%
    public boolean payIncomeTax(int activePlayerId, int choice){
        if(choice == 0){
            return safePaymentToBank(activePlayerId, ((IncomeTax)boardController.getBoard().getFields()[4]).getIncomeTax());
        }
        else if(choice == 1){
            //TODO: create method that counts players total value
        }
        return true; //change later
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
}
