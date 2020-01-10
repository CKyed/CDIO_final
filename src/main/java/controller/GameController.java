package controller;

import model.*;
import model.Fields.IncomeTax;
import model.Fields.OrdinaryTax;
import model.Fields.Ownable;
import model.Fields.OwnableFile.Street;
import model.Fields.Prison;

public class GameController {
    private BoardController boardController;
    private PlayerController playerController;
    private Player activePlayer;
    private int startBonus = 2000;



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
        activePlayerId =-1;
        updateActivePlayer();
    }
    //Pass-on method returns dice for use in systemcontroller and viewontroller
    public int[] rollDice(){
        int[] diceValues = diceController.diceValues();

        //Moves the players position
        movePlayer(playerController.getPlayers()[activePlayerId].getPositionOnBoard(),diceController.sum());
        return diceValues;
    }

    public void movePlayer(int currentFieldId, int dieSum){
        int numberOfFields = this.boardController.getBoard().getFields().length;
        //Calculates new field and adds startbonus if player passed start
        int newFieldId = (currentFieldId+dieSum)%numberOfFields;
        this.activePlayer.setPositionOnBoard(newFieldId);
        if (currentFieldId+dieSum>numberOfFields && !activePlayer.isInJail()){
                activePlayer.deposit(startBonus);
        }
    }


    public boolean safePaymentToBank(int playerId,int amount){
        //Calls the method in playerController and gets a boolean. If false, the player could not afford
        return playerController.safeTransferToBank(playerId,amount);

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
    public boolean payIncomeTax(int activePlayerId, boolean choice){
        boolean succesfulTransfer=true;
        if(choice){
            succesfulTransfer = safePaymentToBank(activePlayerId, ((IncomeTax)boardController.getBoard().getFields()[4]).getIncomeTax());
        }
        else{
            //TODO: create method that counts players total value
        }
        return succesfulTransfer; //change later
    }
    // The method withdraws bail from player
    public boolean payBail(int activePlayerId){
        return playerController.safeTransferToBank(activePlayerId,((Prison)boardController.getBoard().getFields()[30]).getBail());
    }



    public boolean safePaymentToPlayer(int fromPlayerId, int amount, int toPlayerId){
        return playerController.safeTransferToPlayer(fromPlayerId,amount,toPlayerId);
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
        if(!diceController.equals()){
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
        int activeFieldId = activePlayer.getPositionOnBoard();
        int ownerId = ((Ownable)this.boardController.getBoard().getFields()[activeFieldId]).getOwnerId();
        return ownerId;
    }

    public void buyFieldForPlayer(){
        //Gets price
        int price = ((Street)boardController.getBoard().getFields()[activePlayer.getPositionOnBoard()]).getPrice();

        //Pays
        safePaymentToBank(activePlayerId,price);

        //Gives ownership to player
        ((Street)boardController.getBoard().getFields()[activePlayer.getPositionOnBoard()]).setOwnerId(activePlayerId);

    }

    /**
     * Method that can be called when a player lands on the field called Ordinary Tax
     * @param activePlayerId
     * @return
     */

    //public boolean payOrdinaryTax(int activePlayerId){
        //return safePaymentToBank(activePlayerId, ((OrdinaryTax)boardController.getBoard().getFields()[38]).getTax());

    //}
}