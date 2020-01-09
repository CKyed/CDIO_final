package controller;

public class SystemController {
    private GameController gameController;
    private ViewController viewController;
    private int numberOfPlayers;


    public SystemController(){
        this.gameController = new GameController();


        this.viewController = new ViewController(this.gameController.getBoardController().getBoard());
        setupPlayers();
        play();
    }
    //Setup players with an array of Strings witch the setupPlayers method in viewcontroller returns
    public void setupPlayers(){
        String[] playerNames = this.viewController.setupPlayers();
        gameController.setupPlayers(playerNames);
    }

    public void play(){
        int activePlayerId = gameController.getActivePlayerId();
        int[] faceValues;
        int sum;
        int oldFieldId;

        //Plays turns
        while (true){
            //Gets dieRoll and updates view
            oldFieldId = gameController.getActivePlayer().getCurrentFieldId();
            faceValues = gameController.rollDice();
            sum = gameController.getDiceController().getSum();
            viewController.rollDiceAndMove(faceValues,sum,activePlayerId,oldFieldId);

            //Updates the balances of all Players
            viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());







            //Updates the balances of all Players
            viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());

            viewController.updateOwnerships();

            //Gives the turn to the next player
            gameController.updateActivePlayer();
            activePlayerId = gameController.getActivePlayerId();

        }



    }






    public GameController getGameController() {
        return gameController;
    }
}
