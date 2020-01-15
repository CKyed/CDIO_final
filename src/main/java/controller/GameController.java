package controller;

//import com.sun.javafx.sg.prism.NGAmbientLight;
import model.*;
import model.Fields.IncomeTax;
import model.Fields.OrdinaryTax;
import model.Fields.Ownable;
import model.Fields.OwnableFile.*;
import model.Fields.Prison;


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

    public void movePlayerNoStartBonus(int currentFieldId, int dieSum){
        int numberOfFields = this.boardController.getBoard().getFields().length;
        //Calculates new field and adds startbonus if player passed start
        int newFieldId = (currentFieldId+dieSum)%numberOfFields;
        playerController.getActivePlayer().setPositionOnBoard(newFieldId);
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

    //the user has chosen either 0 or 1, 0 is 4000 kr and 1 is 10% of total player value
    public boolean payIncomeTax(int activePlayerId, boolean choice,int tenPctOfValues){
        boolean succesfulTransfer=true;
        if(choice){
            succesfulTransfer = safePaymentToBank(activePlayerId, ((IncomeTax)boardController.getBoard().getFields()[4]).getIncomeTax());
        }
        else{

            succesfulTransfer = safePaymentToBank(activePlayerId,tenPctOfValues);
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
        if (!diceController.isSameValue() || playerController.getActivePlayer().isInJail()){
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
        int price = ((Ownable)boardController.getBoard().getFields()[playerController.getActivePlayer().getPositionOnBoard()]).getPrice();

        //Pays
        safePaymentToBank(playerController.getActivePlayerId(),price);

        //Gives ownership to player
        ((Ownable)boardController.getBoard().getFields()[playerController.getActivePlayer().getPositionOnBoard()]).setOwnerId(playerController.getActivePlayerId());

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

    public void sellHouses(int fieldId, int numberOfHouses, int playerId){
        int totalGain = numberOfHouses*((Street)boardController.getBoard().getFields()[fieldId]).getHousePrice()/2;
        playerController.getPlayers()[playerId].deposit(totalGain);
        boardController.sellHouses(fieldId,numberOfHouses);

    }

    public ChanceCardController getChanceCardController() {
        return chanceCardController;
    }

    public int chanceSum(int oldPos, int newPos){
        int chancesum = newPos - oldPos;
        if(oldPos>newPos){
            chancesum = chancesum + 40;
        }
        return chancesum;
    }

    public int newPos(int chancekortID){
        int numberOfFieldstoBeMoved = 0;

        switch(chancekortID) {
            case 15:
            case 16:
                numberOfFieldstoBeMoved=chanceSum(playerController.getActivePlayer().getPositionOnBoard(),0);
                break;
            case 17:
                numberOfFieldstoBeMoved= 3;
                break;
            case 18:
                numberOfFieldstoBeMoved=chanceSum(playerController.getActivePlayer().getPositionOnBoard(),11);
                break;
            case 19:
                numberOfFieldstoBeMoved=chanceSum(playerController.getActivePlayer().getPositionOnBoard(),15);
                break;
            case 20:
                numberOfFieldstoBeMoved=chanceSum(playerController.getActivePlayer().getPositionOnBoard(),24);
                break;
            case 21:
                numberOfFieldstoBeMoved=chanceSum(playerController.getActivePlayer().getPositionOnBoard(),32);
                break;
            case 22:
                numberOfFieldstoBeMoved=chanceSum(playerController.getActivePlayer().getPositionOnBoard(),19);
                break;
            case 23:
                numberOfFieldstoBeMoved=chanceSum(playerController.getActivePlayer().getPositionOnBoard(),39);
                break;

        }
        return numberOfFieldstoBeMoved;
    }

    public String findWinner(){
        int totalLostPlayers = 0;

        //Everytime a player looses, the counter goes up by 1
       for (int i = 0; i < playerController.getPlayers().length; i++) {
            if (playerController.getPlayers()[i].isHasPlayerLost()==true){
                makeFreeField(i);
                totalLostPlayers++;
            }
        }

        String msg = "";

        //Check if there is one winner left
        if (totalLostPlayers == playerController.getPlayers().length - 1) {
            for (int i = 0; i <  playerController.getPlayers().length; i++) {
                if ( playerController.getPlayers()[i].getAccount().getBalance() > 0) {
                    // "looser" does not exist in winner name
                    msg = playerController.getPlayers()[i].getName();
                }
            }
        }
       return msg;
    }

    //Releases all the fields that the looser owns
    //This method might need to be deleted in because of new bankruptcy rules
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

    public void pawnStreet(int playerId,int fieldId){
        //Deposits money
        int amount = ((Ownable)boardController.getBoard().getFields()[fieldId]).getPrice()/2;
        playerController.getPlayers()[playerId].deposit(amount);

        //changes pawn status
        ((Ownable)boardController.getBoard().getFields()[fieldId]).setPledged(true);
    }

    public void unpawnStreet(int playerId,int fieldId){
        //Withdraws money
        int amount = ((Ownable)boardController.getBoard().getFields()[fieldId]).getPrice()/2;
        amount = (int)(amount*1.1);

        //Checks that player can afford it
        if (!playerController.safeTransferToBank(playerId,amount)){
            System.out.println("FEJl i pawnStreet()-metoden. Spilleren har ikke råd");
        }

        //changes pawn status
        ((Ownable)boardController.getBoard().getFields()[fieldId]).setPledged(false);
    }

}