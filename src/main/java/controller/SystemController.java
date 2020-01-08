package controller;

public class SystemController {
    private GameController gameController;
    private ViewController viewController;


    public SystemController(){
        this.gameController = new GameController();
        this.viewController = new ViewController(this.gameController.getBoardController().getBoard());

    }

    public GameController getGameController() {
        return gameController;
    }
}
