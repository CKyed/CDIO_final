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

    /**
     * Calls the method safeTransferToBank in playerController that returns a boolean that tells if the player has enough money on account
     * If false, the player could not afford the
     * @param playerId
     * @param amount
     */

    public void safePaymentToBank(int playerId,int amount){
        boolean succes = playerController.safeTransferToBank(playerId,amount);
        if(succes==false){
            //The player could not afford to pay
            //TODO logic what happens when player goes bankrupt
        }
    }

    public void safePaymentToPlayer(int fromPlayerId, int amount, int toPlayerId){
        boolean succes = playerController.safeTransferToPlayer(fromPlayerId,amount,toPlayerId);
        if(succes==false){
            //The player could not afford to pay
        }
    }
}
