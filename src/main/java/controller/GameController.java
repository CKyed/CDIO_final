package controller;

public class GameController {
    private BoardController boardController;

    public GameController(){
        this.boardController = new BoardController();
    }

    public BoardController getBoardController() {
        return boardController;
    }
}
