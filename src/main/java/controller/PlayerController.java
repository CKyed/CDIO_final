package controller;
import model.Player;

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
        boolean succes=true;
        if (amount <= players[playerId].getAccountBalance()) {
            takeMoneyFromPlayer(playerId, amount);
        } else {
            succes=false;
        }
        return succes;
    }

    public boolean safeTransferToPlayer(int fromPlayerId, int amount, int toPlayerId){
        boolean succes = true;
        if(amount<=players[fromPlayerId].getAccountBalance()){
            takeMoneyFromPlayer(fromPlayerId,amount);
            addMoneyToPlayer(toPlayerId,amount);
        } else {
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
            this.activePlayerId++;
            this.activePlayerId = this.activePlayerId % this.numberOfPlayers;
            this.activePlayer = players[activePlayerId];
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
}
