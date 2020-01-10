package controller;
import model.Player;

public class PlayerController {
    private Player[] players;
    private int numberOfPlayers;

    public PlayerController(String[] playerNames){
        //Setup the players with player names and start money
        this.numberOfPlayers = playerNames.length;
        int startKapital=30000;
        players = new Player[numberOfPlayers];
        for (int i=0;i<numberOfPlayers;i++){
            players[i] = new Player(playerNames[i],startKapital);
        }
    }

    public void addMoneyToPlayer(int playerId, int amount){
        players[playerId].deposit(amount);
    }

    public void takeMoneyFromPlayer(int playerid, int amount){
        players[playerid].withdraw(amount);
    }

    public boolean safeTransferToBank(int playerId,int amount) {
        boolean succes=false;
        if(amount<=players[playerId].getAccountBalance()) {
            succes=true;
        }
        takeMoneyFromPlayer(playerId, amount);
        return succes;
    }

    public boolean safeTransferToPlayer(int fromPlayerId, int amount, int toPlayerId){
        boolean succes = false;
        if(amount<=players[fromPlayerId].getAccountBalance()) {
            succes=true;
        }
        takeMoneyFromPlayer(fromPlayerId,amount);
        addMoneyToPlayer(toPlayerId,amount);
        return succes;
    }
}