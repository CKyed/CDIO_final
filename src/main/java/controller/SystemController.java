package controller;

public class SystemController {
    private GameController gameController;
    private ViewController viewController;


    public SystemController(){
        this.gameController = new GameController();
        this.viewController = new ViewController(this.gameController.getBoardController().getBoard());

    }
    //Setup players with an array of Strings witch the setupPlayers method in viewcontroller returns
    public void setupPlayers(){
        String[] playerNames = this.viewController.setupPlayers();
        gameController.setupPlayers(playerNames);
    }

    public GameController getGameController() {
        return gameController;
    }
}
