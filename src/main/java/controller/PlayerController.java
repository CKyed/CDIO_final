package controller;
import model.Player;

public class PlayerController {
    private Player[] players;
    private int numberOfPlayers;

    public PlayerController(String[] playerNames){
        this.numberOfPlayers = playerNames.length;
        int startKapital=30000;
        players = new Player[numberOfPlayers];
        for (int i=0;i<numberOfPlayers;i++){
            players[i] = new Player(playerNames[i],startKapital);
        }
    }
}
