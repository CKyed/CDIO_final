package controller;
import model.Board;
import model.Player;
import model.Fields.*;
import model.Fields.OwnableFile.Street;

public class PlayerController {
    private Player[] players;
    private int numberOfPlayers;
    private int activePlayerId=0;
    private Player activePlayer;


    public PlayerController(String[] playerNames){
        //Setup the players with player names and start money
        this.numberOfPlayers = playerNames.length;
        int startKapital=30000;
        players = new Player[numberOfPlayers];
        for (int i=0;i<numberOfPlayers;i++){
            players[i] = new Player(playerNames[i],startKapital);
        }
        this.activePlayer = players[this.activePlayerId];
    }

    public void addMoneyToPlayer(int playerId, int amount){
        players[playerId].deposit(amount);
    }

    public void takeMoneyFromPlayer(int playerid, int amount){
        players[playerid].withdraw(amount);
    }

    /**
     * Checks is player has enough money on account, if yes then returns true
     * @param playerId
     * @param amount
     * @return
     */

    public boolean safeTransferToBank(int playerId,int amount) {
        boolean succes;
        int playerBalance = players[playerId].getAccountBalance();
        //If the player can afford
        if(amount<=playerBalance) {
            takeMoneyFromPlayer(playerId, amount);
            succes=true;
        //If the player cant afford
        } else{
            //Takes the remaining money he has left and pays it too the bank
            takeMoneyFromPlayer(playerId,amount-(amount-playerBalance));
            //Saves in the player how much he owes
            players[playerId].setOwesAmount(amount-playerBalance);
            //Marks bank as creditor
            players[playerId].getAccount().setCreditorId(-1);
            succes=false;
        }
        return succes;
    }

    public boolean safeTransferToPlayer(int fromPlayerId, int amount, int toPlayerId){
        boolean succes ;
        int playerBalance = players[fromPlayerId].getAccountBalance();
        //If the player can afford
        if(amount<=playerBalance) {
            takeMoneyFromPlayer(fromPlayerId,amount);
            addMoneyToPlayer(toPlayerId,amount);
            succes=true;
        //If the player can't afford
        }else{
            //Takes the remaining money he has left and pays it too the player
            takeMoneyFromPlayer(fromPlayerId,amount-(amount-playerBalance));
            addMoneyToPlayer(toPlayerId,amount-(amount-playerBalance));
            //Saves in the player how much he owes
            players[fromPlayerId].setOwesAmount(amount-playerBalance);
            //Marks toPlayer as creditor
            players[fromPlayerId].getAccount().setCreditorId(toPlayerId);
            succes=false;
        }

        return succes;
    }

    public Player[] getPlayers() {
        return players;
    }


    public int[] getFieldIds(){
        int[] fieldIds = new int[numberOfPlayers];
        for (int i =0; i< numberOfPlayers; i++){
            fieldIds[i] = players[i].getPositionOnBoard();
        }
        return fieldIds;
    }

    public int[] getPlayerBalances(){
        int[] playerBalances = new int[numberOfPlayers];
        for (int i =0;i<numberOfPlayers;i++){
            playerBalances[i] = players[i].getAccountBalance();
        }
        return playerBalances;
    }

    public void updateActivePlayer(){
        //Updates the activePlayer - only if last diceroll wasn't 2 of the same
        do {
            this.activePlayerId++;
            this.activePlayerId = this.activePlayerId % this.numberOfPlayers;
            this.activePlayer = players[activePlayerId];
        }
        while (this.activePlayer.isHasPlayerLost());
    }

    public int getActivePlayerId() {
        return activePlayerId;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayerId(int activePlayerId){
        this.activePlayerId = activePlayerId;
    }

    public int calculateTotalValue(int playerId, Board board){//TODO kan gøres lidt smartere omkring linje 134 osv.
        //Gets accountsBalance
        int totalValue = players[playerId].getAccountBalance();

        //Adds values from Board
        for (int i=0;i<board.getFields().length;i++){
            //If it is a street
            if (board.getFields()[i].getType().equals("street")){
                //If player owns it
                if ( ((Ownable)board.getFields()[i]).getOwnerId()==playerId){
                    //Adds street-price and price of houses
                    if (((Ownable)board.getFields()[i]).isPledged()){
                        totalValue += ((Ownable)board.getFields()[i]).getPrice()/2;
                    } else{
                        totalValue += ((Ownable)board.getFields()[i]).getPrice();
                    }
                    totalValue += ((Street)board.getFields()[i]).getHouseLevel()*((Street)board.getFields()[i]).getHousePrice();
                }

                //If it is a ferry or brewery
            } else if (board.getFields()[i].getType().equals("ferry") ||board.getFields()[i].getType().equals("brew")){
                //If player owns it
                if (((Ownable)board.getFields()[i]).getOwnerId()==playerId){
                    //Adds price
                    if (((Ownable)board.getFields()[i]).isPledged()){
                        totalValue += ((Ownable)board.getFields()[i]).getPrice()/2;
                    } else{
                        totalValue += ((Ownable)board.getFields()[i]).getPrice();
                    }
                }
            }


        }

     return totalValue;
    }

    // Make loser's account = 0
    public void accountReset(int playerId){
        players[playerId].getAccount().setBalance( 0 );
    }

    public int tenPctOfValue(int playerId, Board board){
        return calculateTotalValue(playerId, board)/10;

    }

    public boolean tryToPayDebt(int playerId){
        //Initializes
        int creditorId = players[playerId].getAccount().getCreditorId();
        int owedAmount = players[playerId].getAccount().getOwesAmount();
        int accountBalance = players[playerId].getAccount().getBalance();
        boolean succes=true;

        if (accountBalance>=owedAmount){//If player can pay off whole debt
            if (creditorId ==-1){ //If player owes bank money
                safeTransferToBank(playerId,owedAmount);
                players[playerId].getAccount().setOwesAmount(0);
            } else{ //if player owes player money
                safeTransferToPlayer(playerId,owedAmount,creditorId);
                players[playerId].getAccount().setOwesAmount(0);
            }
            succes= true;

        } else if (accountBalance < owedAmount){ //if player cant pay off whole debt
            if (creditorId ==-1){ //If player owes bank money
                players[playerId].getAccount().setOwesAmount(owedAmount-accountBalance);
                safeTransferToBank(playerId,accountBalance);

            } else{ //if player owes player money
                players[playerId].getAccount().setOwesAmount(owedAmount-accountBalance);
                safeTransferToPlayer(playerId,accountBalance,creditorId);
            }

            succes= false;
        }
        return succes;
    }

}
