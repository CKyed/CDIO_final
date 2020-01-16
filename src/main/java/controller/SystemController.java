package controller;

import model.ChanceCard;
import model.Fields.Ownable;
import static Utilities.PathExpert.*;
import static Utilities.FileReader.readFile;



public class SystemController {
    private GameController gameController;
    private ViewController viewController;
    private int numberOfPlayers;


    public SystemController() {
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
        int[] faceValues;
        //Plays turns
        while (true) {
            int oldFieldId = gameController.getActivePlayer().getPositionOnBoard();
            //Gets activePlayerID for later use
            activePlayerId = gameController.getActivePlayerId();
            //Asks player to proceed or buy/sell
            String newTurnMessage = String.format( readFile( turnMessagesPath, "newTurn" ), gameController.getActivePlayer().getName() );
            String selection = viewController.getUserButtonPressed( newTurnMessage, readFile( turnMessagesPath, "buySell" ), "OK" );
            if (selection.equals( readFile( turnMessagesPath, "buySell" ) )) {
                buyOrSellBeforeTurn();
            }
            //If the player is in jail
            boolean InJailTurn = gameController.getActivePlayer().isInJail();
            while (InJailTurn) {
                    String jailChoices[];
                    boolean couldafford = true;

                String askWhichChoice = String.format( readFile( turnMessagesPath, "askWhichChoice" ), gameController.getActivePlayer().getName() );
                // Give to player choices to choice
                    if(gameController.getPlayerController().getActivePlayer().isPrisonCard() && gameController.getActivePlayer().getAccountBalance()>=1000) {
                        jailChoices =  new String[3];
                        jailChoices[0] = readFile(turnMessagesPath, "payToPrison");
                        jailChoices[1] = readFile(turnMessagesPath, "rollDiceInPrison");
                        jailChoices[2] = readFile(turnMessagesPath, "release'scard");

                    }else{
                        jailChoices = new String[2];
                        jailChoices[0] = readFile(turnMessagesPath, "payToPrison");
                        jailChoices[1] = readFile(turnMessagesPath, "rollDiceInPrison");
                    }
                    askWhichChoice = viewController.getUserSelection( askWhichChoice, jailChoices );

                    if (askWhichChoice.equals(readFile(turnMessagesPath,"payToPrison"))) { // Pay 1000 kr
                        couldafford = gameController.payBail(activePlayerId);
                        gameController.getPlayerController().getPlayers()[activePlayerId].setInJail( false );
                        InJailTurn = false;

                    } else if (askWhichChoice.equals(readFile(turnMessagesPath,"rollDiceInPrison"))){ //Roll dice
                        // Check first if player already roll dice twice so player should pay 1000
                        if (gameController.getActivePlayer().getRollDiceInPrison() == 2) {
                            gameController.getDiceController().roll();
                            // first roll in prison
                            faceValues = gameController.getDiceController().getFaceValues();
                            viewController.rollDiceInPrison( faceValues );
                            if (faceValues[0] != faceValues[1]) { //rolls to different on the last try and are forced to pay
                                viewController.getUserButtonPressed( readFile( turnMessagesPath, "payPrisonLastTry" ), "Pay 1000" );
                                couldafford = gameController.payBail(activePlayerId);
                                gameController.getPlayerController().getPlayers()[activePlayerId].setInJail( false );
                                InJailTurn = false;
                            } else { //rolls 2 same on the last try
                                gameController.getPlayerController().getPlayers()[activePlayerId].setInJail( false );
                                playTurn( faceValues, oldFieldId );
                            }

                        }else{
                            gameController.getDiceController().roll();
                            // first roll in prison
                            faceValues = gameController.getDiceController().getFaceValues();
                            viewController.rollDiceInPrison( faceValues );
                            // roll dice in prison, and dice are not equal
                            if (!gameController.getDiceController().isSameValue()) {
                                gameController.getActivePlayer().setRollDiceInPrison( (gameController.getActivePlayer().getRollDiceInPrison() == 0) ? 1 : gameController.getActivePlayer().getRollDiceInPrison()+1 );
                            }
                            // dice are equal
                            if (gameController.getDiceController().isSameValue()) {
                                gameController.getPlayerController().getPlayers()[activePlayerId].setInJail( false );
                                playTurn( faceValues, oldFieldId);
                            }
                            InJailTurn = false;
                        }

                    } else if(askWhichChoice.equals(readFile(turnMessagesPath,"release'scard"))) { // Release card
                        // If the player has prison card, the player uses the prisoncard and gets out of jail
                        if (gameController.getPlayerController().getPlayers()[activePlayerId].isPrisonCard()) {
                            gameController.getPlayerController().getPlayers()[activePlayerId].setInJail( false );
                            gameController.getPlayerController().getPlayers()[activePlayerId].setPrisonCard( false );
                            InJailTurn = false;
                        }
//
                }
                if(!couldafford){
                    playerBankruptcy(activePlayerId);
                }
            }

            if (!gameController.getActivePlayer().isInJail()){
                //If not in jail, turn starts normally
                faceValues = gameController.rollDice();
                playTurn( faceValues, oldFieldId );
            }

            //Updates the balances and ownerships of all Players
            viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());
            viewController.updateOwnerships(gameController.getBoardController().getBoard());

            //Gives the turn to the next player
            gameController.updateActivePlayer();
        }
    }


    public void playTurn(int[] faceValues, int oldFieldId){
        int activePlayerId = gameController.getActivePlayerId();

        //Gets dieRoll and updates players position in model-layer
        int sum = faceValues[0] + faceValues[1];

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

            //If it is pawned, no rent is paid
            if (((Ownable)gameController.getBoardController().getBoard().getFields()[fieldId]).isPledged()){
                //Tells players that no rent is paid
                viewController.showMessage(String.format(readFile(turnMessagesPath,"pawnedNorent"),gameController.getBoardController().getBoard().getFields()[fieldId].getName()));

            } else { //If it is not pawned - rent must be paid

                String fromPlayerName = gameController.getActivePlayer().getName();
                String toPlayerName = gameController.getPlayerController().getPlayers()[gameController.getOwnerId()].getName();
                int rent=0;

                //rent is calculated in different ways
                switch ((gameController.getBoardController().getBoard().getFields()[fieldId]).getType()){
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
                    playerBankruptcy(gameController.getActivePlayerId());
                }
            }



        } else if (gameController.getOwnerId()==-1){//If it is vacant - asks if player wants to buy
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
            playerBankruptcy(gameController.getActivePlayerId());
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

        if (cardId <15){ //If it is a simple chancecard, where player recieves money
            //Plays card in method in model layer
            String message = gameController.getChanceCardController().playCard(cardId,gameController.getPlayerController(),gameController.getBoardController().getBoard());
            if (!message.isEmpty()) {
                viewController.showMessage(message);
            }
        } else if (cardId >14 && cardId <24 ){ //If it is about simple movement
            //Moves in model layer
            gameController.movePlayer(oldPos,sum);
            int virutalFaceValues[] = {10,10};
            //Updates view
            viewController.rollDiceAndMove(virutalFaceValues,sum,playerId,oldPos);
            viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());
            //PLays turn on new field
            landOnField();

        } else if (cardId>23 && cardId<26) { //If it is backwards movement
            gameController.movePlayerNoStartBonus(oldPos, 37);
            viewController.teleportPlayerCar(playerId, 37, oldPos);
            landOnField();
        }
    }

    public void playerBankruptcy(int playerId) {
        int owesAmount = gameController.getActivePlayer().getOwesAmount();
        int playerBalance = gameController.getPlayerController().getPlayers()[playerId].getAccountBalance();
        int creditorId = gameController.getPlayerController().getPlayers()[playerId].getAccount().getCreditorId();

        // Updates balances on view
        viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());

        //stillHasOptions tells if player can still sell or pawn more
        boolean stillHasOptions = true;
        //couldPay tells if player saved himself or not
        boolean couldPay = false;

        while (stillHasOptions && !couldPay) {
            //Updates player balances in view
            viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());

            //Shows message that player must sell something
            viewController.showMessage(String.format(readFile(endMessagePath,"hasToSell"),gameController.getPlayerController().getPlayers()[playerId].getName(),gameController.getPlayerController().getPlayers()[playerId].getAccount().getOwesAmount()));

            //Possible to sell houses
            sellHouses(playerId);
            //Possible to pawn ownables
            pawnStreets(playerId);

            //Updates player balances in view
            viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());


            //Pays debt again
            couldPay = gameController.getPlayerController().tryToPayDebt(playerId);

            //Sets true, if player still can pawn or sell
            stillHasOptions = gameController.getBoardController().getSellableStreetIds(playerId).length != 0 ||
                    gameController.getBoardController().getPawnableOrUnpawnableStreetIds(playerId, true, 0).length != 0;

            //Tells player to sell more
            if (stillHasOptions && !couldPay) {
                viewController.showMessage(String.format(readFile(turnMessagesPath, "needsToSell"), gameController.getPlayerController().getPlayers()[playerId].getName()));
            }

        }
        if (!couldPay) {
            //Handles the loser situation
            looserSituation(playerId);
        } else{
            //Tells that player is free of debt
            viewController.showMessage(String.format(readFile(turnMessagesPath,"freeOfDebt"),gameController.getPlayerController().getPlayers()[playerId].getName()));
        }

    }

    //Deals if has lost
    public void looserSituation(int playerId){
        int fieldId = gameController.getActivePlayer().getPositionOnBoard();

        //Reset the players account to 0
        gameController.getPlayerController().accountReset(gameController.getActivePlayerId());
        //Set the the player variale "hasPlayerLost" to true
        gameController.getPlayerController().getActivePlayer().setHasPlayerLost(true);
        //Releases all the players properties in both model and view
        //TODO her kan evt refaktureres, tjek om for-loop er nødvendigt
        for (int i = 0; i < gameController.getPlayerController().getPlayers().length; i++) {
            if (gameController.getPlayerController().getPlayers()[i].isHasPlayerLost()==true){
                viewController.updateOwnerships(gameController.getBoardController().getBoard());
                gameController.makeFieldsFree(i);
            }
        }

        viewController.looserMessage(gameController.getActivePlayerId());
        viewController.removeLoser(playerId, fieldId);
        if (!gameController.findWinner().isEmpty()){
            viewController.endGame(gameController.findWinner());
        }
    }

    public void buyOrSellBeforeTurn(){
        boolean buyOrSellMore = true;
        while(buyOrSellMore){
            String menuSelction = viewController.getUserButtonPressed("",readFile(turnMessagesPath
                    ,"buyHouses"),readFile(turnMessagesPath,"sellHouses"),readFile(turnMessagesPath,"pawnOrUnpawn"),readFile(turnMessagesPath,"exit"));

            if (menuSelction.equals(readFile(turnMessagesPath,"buyHouses"))){//If player wants to buy houses
                buyHouses();

            } else if (menuSelction.equals(readFile(turnMessagesPath,"sellHouses"))){//If player wants to sell houses
                sellHouses(gameController.getActivePlayerId());
            } else if (menuSelction.equals(readFile(turnMessagesPath,"pawnOrUnpawn"))){//If player wants to pawn streets
                    pawnOrUnPawnStreets();
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
                //TODO refakturer evt indre loop, da else statement er overflødig
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

    public void sellHouses(int playerId){
        boolean sellMore = true;
        int selectedStreetId =-1;
        String selectedStreet;
        while (sellMore){
            //gets array of street ids where player can sell houses
            int[] sellableStreetIds = gameController.getBoardController().getSellableStreetIds(playerId);

            if (sellableStreetIds.length==0){ //If player cant sell any houses
                //Shows message, that payer cant sell
                viewController.showMessage(String.format(readFile(turnMessagesPath,"cantSellHouse"),gameController.getPlayerController().getPlayers()[playerId].getName()));
                sellMore = false;

            } else { //If player can sell houses
                //Gets array of "sellable" street names
                String[] sellableStreetNames = new String[sellableStreetIds.length + 1];
                for (int i = 0; i < sellableStreetIds.length; i++) {
                    sellableStreetNames[i] = gameController.getBoardController().getBoard().getFields()[sellableStreetIds[i]].getName();
                }

                //gives the exit option
                sellableStreetNames[sellableStreetIds.length] = readFile(turnMessagesPath, "exit");
                String askWhichStreet = String.format(readFile(turnMessagesPath, "sellHouseWhere"), gameController.getPlayerController().getPlayers()[playerId].getName());

                //Asks where player wants to sell
                selectedStreet = viewController.getUserSelection(askWhichStreet, sellableStreetNames);
                if (selectedStreet.equals(readFile(turnMessagesPath,"exit"))) {
                    //If player chose "exit"
                    sellMore = false;
                    selectedStreetId=-1;
                } else {
                    for (int i = 0; i < sellableStreetIds.length; i++) {
                        //If it is the street name, that player selected
                        if (selectedStreet.equals(sellableStreetNames[i])){
                            selectedStreetId = sellableStreetIds[i];
                        }

                    }
                }
                if (selectedStreetId != -1){
                    gameController.sellHouses(selectedStreetId, 1, playerId);
                    viewController.showMessage(readFile(turnMessagesPath, "sellingSucceded"));

                }
            }
            //Updates balances and ownerships
            viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());
            viewController.updateOwnerships(gameController.getBoardController().getBoard());
        }
    }

    public void pawnOrUnPawnStreets(){
        boolean done = false;
        String selection;
        while (!done){
            selection = viewController.getUserButtonPressed("",readFile(turnMessagesPath,"pawn"),readFile(turnMessagesPath,"unpawn"),readFile(turnMessagesPath,"exit"));
            if (selection.equals(readFile(turnMessagesPath,"pawn"))){
                pawnStreets(gameController.getActivePlayerId());
            } else if (selection.equals(readFile(turnMessagesPath,"unpawn"))){
                unpawnStreets(gameController.getActivePlayerId());
            } else {
                done = true;
            }
        }
    }

    public void pawnStreets(int playerId){
        boolean pawnMore = true;
        int [] pawnableStreetIds;
        int selectedStreetId;

        while (pawnMore){

            //Gets array of pawnable streets for player
            pawnableStreetIds = gameController.getBoardController().getPawnableOrUnpawnableStreetIds(gameController.getActivePlayerId(),
                    true,gameController.getPlayerController().getPlayers()[playerId].getAccountBalance());

            if (pawnableStreetIds.length==0){
                //If the array was empty
                viewController.showMessage(readFile(turnMessagesPath,"noPawningPossible"));
                pawnMore = false;
            } else {
                //If there are possible pawnings
                String[] possibleStreetNames = new String[pawnableStreetIds.length+1];

                //Creates array of names for possible streets
                for (int i = 0; i < pawnableStreetIds.length; i++) {
                    //Sets the name of the street in possibleStreetNames
                    possibleStreetNames[i]=gameController.getBoardController().getBoard().getFields()[pawnableStreetIds[i]].getName();
                }
                //Sets the last element to "exit"
                possibleStreetNames[possibleStreetNames.length-1] = readFile(turnMessagesPath,"exit");

                //Asks which street player wants to pawn
                String selectedStreetName = viewController.getUserSelection(String.format(readFile(turnMessagesPath,"pawnWhere"),gameController.getPlayerController().getPlayers()[playerId].getName()),possibleStreetNames);
                selectedStreetId=0;

                //If the player did not choose exit
                if (!selectedStreetName.equals(readFile(turnMessagesPath,"exit"))){

                    //Goes through all streetNames and finds the correct id
                    for (int i = 0; i < pawnableStreetIds.length; i++) {
                        if (selectedStreetName.equals(possibleStreetNames[i])){
                            selectedStreetId = pawnableStreetIds[i];
                        }
                    }

                    //Pawns in the model layer
                    gameController.pawnStreet(playerId,selectedStreetId);
                } else{ //If player chose exit
                    pawnMore=false;
                }


                //Updates view layer
                viewController.updateOwnerships(gameController.getBoardController().getBoard());
                viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());
            }
        }
    }

    public void unpawnStreets(int playerId){
        //Gets array of unpawnable streets for player
        int[] unpawnableStreetIds = gameController.getBoardController().getPawnableOrUnpawnableStreetIds(gameController.getActivePlayerId(),false,
                gameController.getPlayerController().getPlayers()[playerId].getAccountBalance());

        if (unpawnableStreetIds.length==0){
            //If the array was empty
            viewController.showMessage(readFile(turnMessagesPath,"noUnpawningPossible"));
        } else {
            //If there are possible unpawnings
            String[] possibleStreetNames = new String[unpawnableStreetIds.length+1];

            //Creates array of names for possible streets
            for (int i = 0; i < unpawnableStreetIds.length; i++) {
                //Sets the name of the street in possibleStreetNames
                possibleStreetNames[i]=gameController.getBoardController().getBoard().getFields()[unpawnableStreetIds[i]].getName();
            }
            //Sets the last element to "exit"
            possibleStreetNames[possibleStreetNames.length-1] = readFile(turnMessagesPath,"exit");

            //Asks which street player wants to unpawn
            String selectedStreetName = viewController.getUserSelection("",possibleStreetNames);
            int selectedStreetId=0;

            //If the player did not choose exit
            if (!selectedStreetName.equals(readFile(turnMessagesPath,"exit"))){

                //Goes through all streetNames and finds the correct id
                for (int i = 0; i < unpawnableStreetIds.length; i++) {
                    if (selectedStreetName.equals(possibleStreetNames[i])){
                        selectedStreetId = unpawnableStreetIds[i];
                    }
                }

                //Pawns in the model layer
                gameController.unpawnStreet(playerId,selectedStreetId);
            }


            //Updates view layer
            viewController.updateOwnerships(gameController.getBoardController().getBoard());
            viewController.updatePlayerBalances(gameController.getPlayerController().getPlayerBalances());
        }
    }


}
