package controller;

public class SystemController {
    private GameController gameController;
    private ViewController viewController;
    private int numberOfPlayers;


    public SystemController(){
        this.gameController = new GameController();


        this.viewController = new ViewController(this.gameController.getBoardController().getBoard());
        setupPlayers();
    }
    //Setup players with an array of Strings witch the setupPlayers method in viewcontroller returns
    public void setupPlayers(){
        String[] playerNames = this.viewController.setupPlayers();
        gameController.setupPlayers(playerNames);
    }

    public void play(){
        int activePlayerId = 0;


        //Plays turns
        while (true){
            //Gets dieRoll and updates view
            int[] faceValues = gameController.rollDice();
            int sum = gameController.getDiceController().getSum();
            int[] oldFieldIds = gameController.getPlayerController().getFieldIds();

            viewController.rollDiceAndMove(faceValues,sum,activePlayerId,oldFieldIds,numberOfPlayers);


            //Updates the balances of all Players
            viewController.updatePlayerBalances();

            viewController.updateOwnerships();

            //Gives the turn to the next player
            gameController.updateActivePlayer();

        }

    }



    public GameController getGameController() {
        return gameController;
    }
}
