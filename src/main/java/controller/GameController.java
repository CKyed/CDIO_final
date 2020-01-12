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
        activePlayerId =-1;
        updateActivePlayer();
    }

    public int[] rollDice(){
        diceController.roll();
        int currentFieldId = this.activePlayer.getPositionOnBoard();
        int dieSum = diceController.getSum();

        //Moves the players position
        movePlayer(currentFieldId,dieSum);

        return diceController.getFaceValues();
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
        //Updates the activePlayer - only if last diceroll wasn't 2 of the same // TODO: or when I call looserSituation method in systemController, it makes the balance of player 0
        if(!diceController.isSameValue() ){ // || activePlayer.getAccount().getBalance() == 0
            do {
                int numberOfPlayers = playerController.getPlayers().length;
                this.activePlayerId++;
                // TODO what is the benefit of this statement?
                this.activePlayerId = this.activePlayerId % numberOfPlayers;
                // TODO why not setPlayers?
                this.activePlayer = playerController.getPlayers()[activePlayerId];
            }while (activePlayer.getAccount().getBalance() == 0);
        }else if (activePlayer.getAccount().getBalance() == 0) {
            do {
                int numberOfPlayers = playerController.getPlayers().length;
                this.activePlayerId++;
                // TODO what is the benefit of this statement?
                this.activePlayerId = this.activePlayerId % numberOfPlayers;
                // TODO why not setPlayers?
                this.activePlayer = playerController.getPlayers()[activePlayerId];
            } while (activePlayer.getAccount().getBalance() == 0);
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

    public boolean buyFieldForPlayer(){
        //Gets price
        int price = ((Street)boardController.getBoard().getFields()[activePlayer.getPositionOnBoard()]).getPrice();

        //Pays
        if (safePaymentToBank(activePlayerId,price)){
            //Gives ownership to player
            ((Street)boardController.getBoard().getFields()[activePlayer.getPositionOnBoard()]).setOwnerId(activePlayerId);
            return true;
        }else {
            return false;
        }
    }

    /**
     * Method that can be called when a player lands on the field called Ordinary Tax
     * @param activePlayerId
     * @return
     */

    //public boolean payOrdinaryTax(int activePlayerId){
        //return safePaymentToBank(activePlayerId, ((OrdinaryTax)boardController.getBoard().getFields()[38]).getTax());

    //}



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
    // The following method should: set the current prisonstatus of player to true
    //  also: return a boolean to tell if player is in prison or not
    // (maybe also return an int to tell how long the player has been in prison for later use)
    // if the player is in prison (System controller) then following method:
    // pay bail (1000 kr)





    //if isplayerinprison is false then setprisonstatus to true (in Systemcontroller)












}


