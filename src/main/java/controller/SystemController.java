package controller;

import model.ChanceCard;
import model.Fields.Ownable;
import static controller.PathExpert.*;
import static controller.TextController.readFile;



public class SystemController {
    private GameController gameController;
    private ViewController viewController;
    private int numberOfPlayers;


    public SystemController(){
        //Initializes controllers
        this.gameController = new GameController();
        this.viewController = new ViewController(gameController.getBoardController().getBoard());

        //Setup players with an array of Strings from the viewcontroller
        String[] playerNames = this.viewController.setupPlayers();
        gameController.setPlayerController(new PlayerController(playerNames));

        //Plays game
        play();
    }

    public void play(){

        int activePlayerId;
        //Plays turns
        while (true){
            //Gets activePlayerID for later use
            activePlayerId = gameController.getActivePlayerId();

            //Asks player to proceed or buy/sell
            String newTurnMessage = String.format( readFile( turnMessagesPath, "newTurn" ), gameController.getActivePlayer().getName() );
            String selection = viewController.getUserButtonPressed(newTurnMessage,readFile(turnMessagesPath,"buySell"),"OK");
            if (selection.equals(readFile(turnMessagesPath,"buySell"))){
                buyOrSellBeforeTurn();
            }


            //If the player is in jail
            if(gameController.getActivePlayer().isInJail()){
                boolean success = true;
                //shows that player is in prison
                viewController.showMessage(String.format(readFile(turnMessagesPath,"playerPaysBail"),gameController.getActivePlayer().getName()));

                //If the player has prison card, the player uses the prisoncard and gets out of jail
                if(gameController.getPlayerController().getPlayers()[activePlayerId].isPrisonCard()){
                    gameController.getPlayerController().getPlayers()[activePlayerId].setInJail(false);
                    gameController.getPlayerController().getPlayers()[activePlayerId].setPrisonCard(false);
                }else {
                    //If the player doesn't have a prison card, the player pays the bail and gets out of jail
                    success = gameController.payBail(activePlayerId);
                    gameController.getPlayerController().getPlayers()[activePlayerId].setInJail(false);
                }
                if(success == false){
                    //TODO Method for handling loser-condition is called here
                }

            } else{
                //If not in jail, turn starts normally
                playTurn();
            }

            //Updates the balances and ownerships of all Players
            viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());
            viewController.updateOwnerships(gameController.getBoardController().getBoard());

            //Gives the turn to the next player
            gameController.updateActivePlayer();
        }
    }

    public void buyBeforeTurn(){
        //Gets array of id's of streets that can be built on
        int[] buildableStreetIds = gameController.getBoardController().getBuildableStreetIds(gameController.getActivePlayerId());

        //If the player can build and wants to
        if (buildableStreetIds.length != 0 && viewController.chooseToBuy(gameController.getActivePlayerId())){
            int wantedNumberOfHouses;
            //Goes through all buildable streets and asks for the wanted number of houses
            for (int i=0;i<buildableStreetIds.length;i++){
                wantedNumberOfHouses = viewController.getWantedNumberOfHouses(buildableStreetIds[i],gameController.getActivePlayerId());
                if(wantedNumberOfHouses!=0){
                    boolean succes = gameController.tryToBuyHouses(buildableStreetIds[i],wantedNumberOfHouses);
                    if (succes){
                        viewController.showMessage(String.format(readFile(turnMessagesPath,"buildingSucceeded"),wantedNumberOfHouses));
                        viewController.updateOwnerships(gameController.getBoardController().getBoard());
                    } else{
                        viewController.showMessage(String.format(readFile(turnMessagesPath,"notPossibleToBuild"),wantedNumberOfHouses));
                        //If player asked for something impossible, counter doesn't update
                        i--;

                    }
                }
            }
        }
    }

    public void playTurn(){
        int activePlayerId = gameController.getActivePlayerId();
        int oldFieldId = gameController.getActivePlayer().getPositionOnBoard();

        //Gets dieRoll and updates players position in model-layer
        int[] faceValues = gameController.rollDice();
        int sum = gameController.getDiceController().getSum();

        //Updates players position in view-layer
        viewController.rollDiceAndMove(faceValues,sum,activePlayerId,oldFieldId);

        //Updates player balances in view layer
        viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());

        //Does actions on new field
        landOnField();

        //Updates the balances of all Players
        viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());

    }

    public void playPropertyField(){

        //If the property is owned by someone else
        if(gameController.getOwnerId()>=0 && gameController.getOwnerId()!= gameController.getActivePlayerId()){
            //Gets data
            int fieldId = gameController.getActivePlayer().getPositionOnBoard();
            String fromPlayerName = gameController.getActivePlayer().getName();
            String toPlayerName = gameController.getPlayerController().getPlayers()[gameController.getOwnerId()].getName();
            int rent=0;

            //rent is calculated in different ways
            switch (((Ownable)gameController.getBoardController().getBoard().getFields()[fieldId]).getType()){
                case "street":
                case "ferry":
                    //Gets rent
                    rent = ((Ownable)gameController.getBoardController().getBoard().getFields()[fieldId]).getRent();
                break;
                case "brew":
                    rent = ((Ownable)gameController.getBoardController().getBoard().getFields()[fieldId]).getRent();
                    //Multiplies by dieSum
                    rent = rent*gameController.getDiceController().getSum();
                    break;
            }


            //hvis ejeren af feltet er i fængsel, skal man ikke betale noget
            if(gameController.getPlayerController().getPlayers()[gameController.getOwnerId()].isInJail()){
            String message = String.format(readFile(turnMessagesPath, "ownerInPrison"));
            viewController.showMessage(message);
            //transfers 0 money from player to player
            gameController.getPlayerController().safeTransferToPlayer(gameController.getActivePlayerId(),0,gameController.getOwnerId());
                }

            //Tries to pay rent
            else if(gameController.getPlayerController().safeTransferToPlayer(gameController.getActivePlayerId(),rent,gameController.getOwnerId())){

                //Displays message
                String message = String.format(readFile(turnMessagesPath,"payRentFromTo"),fromPlayerName,rent,toPlayerName);
                viewController.showMessage(message);

                //Updates player balances
                viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());
            } else {
                //Player can't afford the rent
                //TODO: Calls a method that handle looser condition
                System.out.println("HER SKAL VI GØRE NOGET");
                looserSituation();

            }
            //If it is vacant - asks if player wants to buy
        } else if (gameController.getOwnerId()==-1){
            //If it is vacant

            int currentFieldId = gameController.getActivePlayer().getPositionOnBoard();

            //If player can afford it
            if (gameController.getPlayerController().getActivePlayer().getAccountBalance()>=((Ownable)gameController.getBoardController().getBoard().getFields()[currentFieldId]).getPrice()){

                //If he chooses to buy
                if (viewController.buyFieldOrNot(gameController.getActivePlayerId(),gameController.getActivePlayer().getPositionOnBoard())){

                    //Withdraws money
                    gameController.buyFieldForPlayer();

                    //Updates the owner
                    ((Ownable)gameController.getBoardController().getBoard().getFields()[currentFieldId]).setOwnerId(gameController.getActivePlayerId());
                    gameController.getBoardController().updateAllRents();

                }
            } else{
                //If player can't afford it, tells on board
                String msg = readFile(turnMessagesPath,"cantAffordOwnable");
                msg = String.format(msg, gameController.getActivePlayer().getName(),gameController.getBoardController().getBoard().getFields()[currentFieldId].getName());
                viewController.showMessage(msg);
            }



        } else{//If the player owns it himself
            String selfOwnedMessage = readFile(turnMessagesPath,"selfOwned");
            selfOwnedMessage = String.format(selfOwnedMessage,gameController.getActivePlayer().getName(),
                    gameController.getBoardController().getBoard().getFields()[gameController.getActivePlayer().getPositionOnBoard()].getName());
            viewController.showMessage(selfOwnedMessage);

        }

    }

    public void landOnField(){
        String activeFieldType = gameController.getBoardController().getBoard().getFields()[gameController.getActivePlayer().getPositionOnBoard()].getType();
        int activePlayerId = gameController.getActivePlayerId();
        boolean canAfford=true;

        //Land on field
        switch (activeFieldType){
            case "street":
                playPropertyField();


                break;
            case "ferry":
                playPropertyField();
                break;
            case "brew":
                playPropertyField();
                break;

            case "incomeTax":
                //Asks how player wants to pay
                String incTaxMsg = readFile(turnMessagesPath,"chooseIncomeTaxType");
                incTaxMsg = String.format(incTaxMsg,gameController.getActivePlayer().getName());
                boolean choice = viewController.payIncomeTax(incTaxMsg);

                //Pays tax in model-layer
                int tenPctOfValues = gameController.getPlayerController().tenPctOfValue(activePlayerId,gameController.getBoardController().getBoard());
                canAfford = gameController.payIncomeTax(activePlayerId, choice, tenPctOfValues);

                //Shows how much player payed, if player chose 10%
                if (!choice){
                    String playerPayedMsg = readFile(turnMessagesPath,"playerPayed");
                    playerPayedMsg = String.format(playerPayedMsg,gameController.getActivePlayer().getName(),tenPctOfValues);
                    viewController.showMessage(playerPayedMsg);
                }
                break;
            case "ordinaryTax":
                //Shows message telling that player must pay
                String ordTaxMsg = readFile(turnMessagesPath,"ordTax");
                ordTaxMsg = String.format(ordTaxMsg,gameController.getActivePlayer().getName());
                viewController.showMessage(ordTaxMsg);

                //Pays tax in model-layer
                canAfford = gameController.payOrdinaryTax(activePlayerId);
                break;
            case "prison":
                //Shows message on board
                viewController.showMessage(String.format(readFile(turnMessagesPath,"goesToJail"),gameController.getActivePlayer().getName()));
                //Sets player in prison in model-layer
                gameController.getPlayerController().getPlayers()[activePlayerId].setInJail(true);

                //Moves player in model-layer
                int oldFieldId = gameController.getActivePlayer().getPositionOnBoard();
                gameController.movePlayer(oldFieldId,20);

                //Moves player on screen
                int virutalFaceValues[] = {10,10};
                viewController.rollDiceAndMove(virutalFaceValues,20,activePlayerId,oldFieldId);
                break;
            case "chance":
                playChanceCard();
                break;

        }

        if(canAfford==false){
            //TODO: Should handle if the players can't afford to pay
            //Player can't afford the tax
            //Looser message
            looserSituation();
        }
    }

    public void playChanceCard(){
        ChanceCard card = gameController.getChanceCardController().getCardDeck().draw();
        int cardId = card.getId();
        String cardText = card.getText();
        int sum = gameController.newPos(card.getId());
        int oldPos = gameController.getPlayerController().getActivePlayer().getPositionOnBoard();
        int playerId =gameController.getActivePlayerId();

        //Shows chancecard
        viewController.showChanceCard(cardText);

        //Shows message telling that player landed on Chance
        String landedOnChanceMsg = readFile(turnMessagesPath,"landedOnChance");
        landedOnChanceMsg = String.format(landedOnChanceMsg,gameController.getPlayerController().getPlayers()[gameController.getPlayerController().getActivePlayerId()].getName());
        viewController.showMessage(landedOnChanceMsg);

        //If it is a chancecard that includes movement on the board
        if(cardId >= 30 && cardId <= 31){
            gameController.movePlayerNoStartBonus(oldPos,37);
            viewController.teleportPlayerCar(playerId,37,oldPos);
            landOnField();
        }

        else if(cardId >= 32 && cardId <= 40 || cardId==27 || cardId==28){
            gameController.movePlayer(oldPos,sum);
            int virutalFaceValues[] = {10,10};
            viewController.rollDiceAndMove(virutalFaceValues,sum,playerId,oldPos);
            landOnField();
            //here we call switch case and related methods from gamecontroller

        } else{
            String message = gameController.getChanceCardController().playCard(cardId,gameController.getPlayerController(),gameController.getBoardController().getBoard());
            if (!message.isEmpty()) {
                viewController.showMessage(message);
            }
        }

    }


    // Handel looser situation
    public void looserSituation(){
        int fieldId = gameController.getActivePlayer().getPositionOnBoard();


        //Reset the players account to 0
        gameController.getPlayerController().accountReset(gameController.getActivePlayerId());
        //Set the the player variale "hasPlayerLost" to true
        gameController.getPlayerController().getActivePlayer().setHasPlayerLost(true);

        viewController.looserMessage(gameController.getActivePlayerId());
        viewController.removeLoser( gameController.getActivePlayerId(), fieldId);
        if (!gameController.findWinner().isEmpty()){
            viewController.endGame(gameController.findWinner());
        }
    }

    public void buyOrSellBeforeTurn(){
        boolean buyOrSellMore = true;
        boolean buyMore;
        boolean sellMore;
        String selectedStreet;
        int selectedStreetId=0;
        while(buyOrSellMore){
            String menuSelction = viewController.getUserButtonPressed("",readFile(turnMessagesPath
                    ,"buyHouses"),readFile(turnMessagesPath,"sellHouses"),readFile(turnMessagesPath,"exit"));

            if (menuSelction.equals(readFile(turnMessagesPath,"buyHouses"))){
                buyMore = true;
                while (buyMore){
                    //gets array of buildable street ids
                    int[] buildableStreetIds = gameController.getBoardController().getBuildableStreetIds(gameController.getActivePlayerId());

                    //Gets array of buildable street names
                    String[] buildableStreetNames = new String[buildableStreetIds.length+1];
                    for (int i = 0; i < buildableStreetIds.length; i++) {
                        buildableStreetNames[i] = gameController.getBoardController().getBoard().getFields()[buildableStreetIds[i]].getName();
                    }

                    //gives the exit option
                    buildableStreetNames[buildableStreetIds.length] = readFile(turnMessagesPath,"exit");
                    String askWhichStreet =String.format(readFile(turnMessagesPath,"buildHouseWhere"),gameController.getActivePlayer().getName()) ;

                    selectedStreet = viewController.getUserSelection(askWhichStreet,buildableStreetNames);
                    for (int i = 0; i <buildableStreetNames.length ; i++) {
                        if (selectedStreet.equals(buildableStreetNames[i])){
                            selectedStreetId = buildableStreetIds[i];
                        }
                    }

                    if (selectedStreetId == buildableStreetIds[buildableStreetIds.length-1]){
                        buyMore = false;
                    }

                }



            } else if (menuSelction.equals(readFile(turnMessagesPath,"sellHouses"))){
                sellMore = true;
                while (sellMore){
                    //gets array of buildable street ids
                    int[] buildableStreetIds = gameController.getBoardController().getBuildableStreetIds(gameController.getActivePlayerId());

                    //Gets array of buildable street names
                    String[] buildableStreetNames = new String[buildableStreetIds.length+1];
                    for (int i = 0; i < buildableStreetIds.length; i++) {
                        buildableStreetNames[i] = gameController.getBoardController().getBoard().getFields()[buildableStreetIds[i]].getName();
                    }

                    //gives the exit option
                    buildableStreetNames[buildableStreetIds.length] = readFile(turnMessagesPath,"exit");
                    String askWhichStreet =String.format(readFile(turnMessagesPath,"buildHouseWhere"),gameController.getActivePlayer().getName()) ;

                    selectedStreet = viewController.getUserSelection(askWhichStreet,buildableStreetNames);
                    for (int i = 0; i <buildableStreetNames.length ; i++) {
                        if (selectedStreet.equals(buildableStreetNames[i])){
                            selectedStreetId = buildableStreetIds[i];
                        }
                    }

                    if (selectedStreetId == buildableStreetIds[buildableStreetIds.length-1]){
                        sellMore = false;
                    }

                }
            } else{
                buyOrSellMore = false;
            }


        }




    }



}
