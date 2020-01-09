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

    public void safeTransferToBank(int playerId,int amount) {
        if (amount <= players[playerId].getAccountBalance()) {
            takeMoneyFromPlayer(playerId, amount);
        } else {
            //Should call a method which handles, if the player dosen't have enough money to pay
        }
    }

    public void safeTransferToPlayer(int fromPlayerId, int amount, int toPlayerId){
        //Returns true if transfer is successful. Otherwise returns false and ends game.
        if(amount<=players[fromPlayerId].getAccountBalance()){
            takeMoneyFromPlayer(fromPlayerId,amount);
            addMoneyToPlayer(toPlayerId,amount);
        } else {
            //Should call a method which handles, if the player dosen't have enough money to pay
        }
    }
}
