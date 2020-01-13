package controller;

import model.*;
import model.Fields.IncomeTax;
import model.Fields.OrdinaryTax;
import model.Fields.Ownable;
import model.Fields.OwnableFile.*;
import model.Fields.Prison;

import static controller.PathExpert.endMessagePath;
import static controller.TextController.readFile;

public class GameController {
    private BoardController boardController;
    private PlayerController playerController;
    private int startBonus = 4000;
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


    public int[] rollDice(){
        diceController.roll();
        int currentFieldId = playerController.getActivePlayer().getPositionOnBoard();
        int dieSum = diceController.getSum();

        //Moves the players position
        movePlayer(currentFieldId,dieSum);

        return diceController.getFaceValues();
    }

    public void movePlayer(int currentFieldId, int dieSum){
        int numberOfFields = this.boardController.getBoard().getFields().length;
        //Calculates new field and adds startbonus if player passed start
        int newFieldId = (currentFieldId+dieSum)%numberOfFields;
        playerController.getActivePlayer().setPositionOnBoard(newFieldId);

        //IF player passes start and is not marked as "inJail", player recieves startbonus
        if (currentFieldId+dieSum>(numberOfFields-1) && !playerController.getActivePlayer().isInJail()){
                playerController.getActivePlayer().deposit(startBonus);
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
            int totalPlayerValue = playerController.calculateTotalValue(activePlayerId,boardController.getBoard());
            int tax = totalPlayerValue/10;
            succesfulTransfer = safePaymentToBank(activePlayerId,tax);
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

    public PlayerController getPlayerController() {
        return playerController;
    }

    public Player getActivePlayer() {
        return playerController.getActivePlayer();
    }

    public DiceController getDiceController() {
        return diceController;
    }

    public void updateActivePlayer(){
        if (!diceController.isSameValue()){
            this.playerController.updateActivePlayer();
        }
    }

    public int getActivePlayerId() {
        return playerController.getActivePlayerId();
    }


    public int getOwnerId(){
        int activeFieldId = playerController.getActivePlayer().getPositionOnBoard();
        int ownerId = ((Ownable)this.boardController.getBoard().getFields()[activeFieldId]).getOwnerId();
        return ownerId;
    }

    public void buyFieldForPlayer(){
        //Gets price
        int price = ((Street)boardController.getBoard().getFields()[playerController.getActivePlayer().getPositionOnBoard()]).getPrice();

        //Pays
        safePaymentToBank(playerController.getActivePlayerId(),price);

        //Gives ownership to player
        ((Street)boardController.getBoard().getFields()[playerController.getActivePlayer().getPositionOnBoard()]).setOwnerId(playerController.getActivePlayerId());

    }

    /**
     * Method that can be called when a player lands on the field called Ordinary Tax
     * @param //activePlayerId
     * @return
     */

    //public boolean payOrdinaryTax(int activePlayerId){
        //return safePaymentToBank(activePlayerId, ((OrdinaryTax)boardController.getBoard().getFields()[38]).getTax());

    //}


    public void setPlayerController(PlayerController playerController) {
        this.playerController = playerController;
    }

    public boolean tryToBuyHouses(int fieldId, int numberOfHouses){
        int totalCost = numberOfHouses*((Street)boardController.getBoard().getFields()[fieldId]).getHousePrice();
        //If the player can't afford, or more houses can't be built
        if(totalCost>getActivePlayer().getAccountBalance() ||numberOfHouses + ((Street)boardController.getBoard().getFields()[fieldId]).getHouseLevel()>5){
            return false;
        } else{
            //If the player can afford and expansion is possible, withdraws money and builds houses
            safePaymentToBank(getActivePlayerId(),totalCost);
            boardController.buildHouses(fieldId,numberOfHouses);
            return true;
        }

    }

    public ChanceCardController getChanceCardController() {
        return chanceCardController;
    }

    public String findWinner(){
        int counterForWinner = 0;
        for (int i = 0; i < playerController.getPlayers().length; i++) {
            if (playerController.getPlayers()[i].getAccount().getBalance() == 0){
                makeFreeField(i);
                counterForWinner++;
            }
        }
        String msg = "";
        if (counterForWinner == playerController.getPlayers().length - 1) {
            for (int i = 0; i <  playerController.getPlayers().length; i++) {
                if ( playerController.getPlayers()[i].getAccount().getBalance() > 0) {
                    // "looser" does not exist in winner name
                    msg = playerController.getPlayers()[i].getName();
                }
            }
        }
       return msg;
    }

    public void makeFreeField(int playerIndex){
        for (int i = 0; i < boardController.getBoard().getFields().length ; i++) {
            if (boardController.getBoard().getFields()[i] instanceof Ownable){
                if (((Ownable)boardController.getBoard().getFields()[i]).getOwnerId() == playerIndex){
                    ((Ownable)boardController.getBoard().getFields()[i]).setOwnerId(-1);
                    //TODO : update view
                }
            }
        }

    }




}