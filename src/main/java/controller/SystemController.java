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
                    playerBankruptcy();
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
                playerBankruptcy();
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
            playerBankruptcy();
        }
    }

    public void playChanceCard(){
        ChanceCard card = gameController.getChanceCardController().getCardDeck().draw();
        int cardId = card.getId();
        String cardText = card.getText();

        //Shows chancecard
        viewController.showChanceCard(cardText);

        //Shows message telling that player landed on Chance
        String landedOnChanceMsg = readFile(turnMessagesPath,"landedOnChance");
        landedOnChanceMsg = String.format(landedOnChanceMsg,gameController.getPlayerController().getPlayers()[gameController.getPlayerController().getActivePlayerId()].getName());
        viewController.showMessage(landedOnChanceMsg);

        //If it is a complicated chance card
        if(cardId ==50 ||cardId==51){

        } else{
            String message = gameController.getChanceCardController().playCard(cardId,gameController.getPlayerController(),gameController.getBoardController().getBoard());
            if (!message.isEmpty()) {
                viewController.showMessage(message);
            }
        }
    }

    public void playerBankruptcy(){
        int activePlayerId = gameController.getActivePlayerId();
        boolean couldPay = false;
        //This method should give the option for a player to sell out before it allows the player to loose.
        //The player should be able to sell houses and pledge properties
        //The method should know who the creditor is
        //The methoud should maybe have a boolean that indicates if the player could pay or if he lost.

        if(!couldPay){
            looserSituation();
        }
    }

    //Deals if has lost
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
        while(buyOrSellMore){
            String menuSelction = viewController.getUserButtonPressed("",readFile(turnMessagesPath
                    ,"buyHouses"),readFile(turnMessagesPath,"sellHouses"),readFile(turnMessagesPath,"exit"));

            if (menuSelction.equals(readFile(turnMessagesPath,"buyHouses"))){//If player wants to buy houses
                buyHouses();

            } else if (menuSelction.equals(readFile(turnMessagesPath,"sellHouses"))){//If player wants to sell houses
                sellHouses();
                } else{
                //Updates balances and ownerships
                viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());
                viewController.updateOwnerships(gameController.getBoardController().getBoard());
                buyOrSellMore = false;
            }
        }




    }

    public void buyHouses(){
        int selectedStreetId =-1;
        String selectedStreet;
        boolean buyMore = true;
        while (buyMore){
            //gets array of buildable street ids
            int[] buildableStreetIds = gameController.getBoardController().getBuildableStreetIds(gameController.getActivePlayerId(),gameController.getActivePlayer().getAccountBalance());

            if (buildableStreetIds.length==0){ //If player cant build anything
                //Shows message, that payer cant buy
                viewController.showMessage(String.format(readFile(turnMessagesPath,"cantBuild"),gameController.getActivePlayer().getName()));
                buyMore = false;

            } else { //If player can build somewhere
                //Gets array of buildable street names
                String[] buildableStreetNames = new String[buildableStreetIds.length + 1];
                for (int i = 0; i < buildableStreetIds.length; i++) {
                    buildableStreetNames[i] = gameController.getBoardController().getBoard().getFields()[buildableStreetIds[i]].getName();
                }

                //gives the exit option
                buildableStreetNames[buildableStreetIds.length] = readFile(turnMessagesPath, "exit");
                String askWhichStreet = String.format(readFile(turnMessagesPath, "buildHouseWhere"), gameController.getActivePlayer().getName());

                selectedStreet = viewController.getUserSelection(askWhichStreet, buildableStreetNames);
                if (selectedStreet.equals(readFile(turnMessagesPath,"exit"))) {
                    //If player chose "exit"
                    selectedStreetId = -1;
                    buyMore = false;
                }else {
                    for (int i = 0; i < buildableStreetIds.length; i++) {
                        if (selectedStreet.equals(buildableStreetNames[i])){
                            selectedStreetId = buildableStreetIds[i];
                        }
                    }
                }


                //If player chose to build on a street
                if (selectedStreetId != -1){
                    if (gameController.tryToBuyHouses(selectedStreetId, 1))
                        viewController.showMessage(readFile(turnMessagesPath, "buildingSucceeded"));
                    else  //Service errormessage
                        viewController.showMessage("HOV - SPILLEREN BURDE KUNNE BYGGE HUSET");
                }
            }
            //Updates balances and ownerships
            viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());
            viewController.updateOwnerships(gameController.getBoardController().getBoard());
        }// while(buyMore)
    }

    public void sellHouses(){
        boolean sellMore = true;
        int selectedStreetId =-1;
        String selectedStreet;
        while (sellMore){
            //gets array of street ids where player can sell houses
            int[] sellableStreetIds = gameController.getBoardController().getSellableStreetIds(gameController.getActivePlayerId());

            if (sellableStreetIds.length==0){ //If player cant sell any houses
                //Shows message, that payer cant sell
                viewController.showMessage(String.format(readFile(turnMessagesPath,"cantSellHouse"),gameController.getActivePlayer().getName()));
                sellMore = false;

            } else { //If player can sell houses
                //Gets array of "sellable" street names
                String[] sellableStreetNames = new String[sellableStreetIds.length + 1];
                for (int i = 0; i < sellableStreetIds.length; i++) {
                    sellableStreetNames[i] = gameController.getBoardController().getBoard().getFields()[sellableStreetIds[i]].getName();
                }

                //gives the exit option
                sellableStreetNames[sellableStreetIds.length] = readFile(turnMessagesPath, "exit");
                String askWhichStreet = String.format(readFile(turnMessagesPath, "sellHouseWhere"), gameController.getActivePlayer().getName());

                //Asks where player wants to sell
                selectedStreet = viewController.getUserSelection(askWhichStreet, sellableStreetNames);
                if (selectedStreet.equals(readFile(turnMessagesPath,"exit"))) {
                    //If player chose "exit"
                    sellMore = false;
                    selectedStreetId=-1;
                } else {
                    for (int i = 0; i < sellableStreetIds.length; i++) {
                        selectedStreetId = sellableStreetIds[i];
                    }
                }
                if (selectedStreetId != -1){
                    gameController.sellHouses(selectedStreetId, 1);
                    viewController.showMessage(readFile(turnMessagesPath, "sellingSucceded"));

                }
            }
            //Updates balances and ownerships
            viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());
            viewController.updateOwnerships(gameController.getBoardController().getBoard());
        }
    }



}
