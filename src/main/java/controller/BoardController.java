package controller;

import model.Board;

public class BoardController {
    private Board board;


    public BoardController(){
        this.board = new Board();

    }

    public Board getBoard() {
        return board;
    }
}
