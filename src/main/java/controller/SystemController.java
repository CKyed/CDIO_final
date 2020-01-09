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
            faceValues = gameController.rollDice();
            sum = gameController.getDiceController().getSum();
            oldFieldId = gameController.getActivePlayer().getCurrentFieldId();


            viewController.rollDiceAndMove(faceValues,sum,activePlayerId,oldFieldId);






            //Updates the balances of all Players
            viewController.updatePlayerBalances();

            viewController.updateOwnerships();

            //Gives the turn to the next player
            gameController.updateActivePlayer();
            activePlayerId = gameController.getActivePlayerId();

        }
    }

    public void movePlayer(int[] diceValues){
        int playerStepsToMove =
    }





    public GameController getGameController() {
        return gameController;
    }
}
