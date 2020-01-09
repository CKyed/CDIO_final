package controller;

public class GameController {
    private BoardController boardController;
    private PlayerController playerController;

    public GameController(){
        this.boardController = new BoardController();

    }

    public BoardController getBoardController() {
        return boardController;
    }

    public void setupPlayers(String[] playerNames){
        this.playerController = new PlayerController(playerNames);

    }
}
