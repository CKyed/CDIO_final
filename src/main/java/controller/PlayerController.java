package controller;
import model.Player;

public class PlayerController {
    private Player[] players;
    private int numberOfPlayers;

    /**
     * Sets number of players equal to length og playerNames array
     * Gives each player a name from array playerNames and 30000 in startmoney
     * @param playerNames
     */

    public PlayerController(String[] playerNames){
        this.numberOfPlayers = playerNames.length;
        int startKapital=30000;
        players = new Player[numberOfPlayers];
        for (int i=0;i<numberOfPlayers;i++){
            players[i] = new Player(playerNames[i],startKapital);
        }
    }

    /**
     * Adds an ammount of money to a players account
     * @param playerId
     * @param amount
     */

    public void addMoneyToPlayer(int playerId, int amount){
        players[playerId].deposit(amount);
    }

    /**
     * Withdraws an ammount of money from a players account.
     * @param playerid
     * @param amount
     */

    public void takeMoneyFromPlayer(int playerid, int amount){
        players[playerid].withdraw(amount);
    }

    /**
     *
     * @param playerId
     * @param amount
     * @return Boolean
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
            fieldIds[i] = players[i].getCurrentFieldId();
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

}
