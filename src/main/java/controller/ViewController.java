package controller;
import static Utilities.PathExpert.*;
import static Utilities.FileReader.readFile;

import gui_fields.*;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
import gui_fields.GUI_Refuge;
import gui_fields.GUI_Start;
import gui_main.GUI;
import model.*;
import model.Fields.*;
import model.Fields.OwnableFile.*;
//import org.w3c.dom.css.RGBColor;

import java.awt.*;

public class ViewController {
    private String[] colorsPicked = {"null","null","null","null","null","null"};
    private GUI gui;
    private GUI_Field[] fields;
    private GUI_Player[] guiPlayers;
    private GUI_Car[] guiCars;
    private String[] fieldSubtexts;
    private int counterForWinner = 0;
    private boolean[][][][] combinationsPicked = new boolean[4][4][4][4];





    public ViewController(Board board) {
        Color bgcolor = new Color(151,90,22);
        GUI_Field[] fields = createFields(board);
        this.fields = fields;
        this.gui = new GUI(fields,bgcolor);
        //All  256 combinations are initially set to false (this is used in manuallyCreateGuiPlayer())
        for ( int i = 0; i < this.combinationsPicked.length; i++ ) {
            for ( int j = 0; j < this.combinationsPicked[i].length; j++ ) {
                for ( int k = 0; k < this.combinationsPicked[i][j].length; k++ ) {
                    for ( int l = 0; l < this.combinationsPicked[i][j][k].length; l++ ) {
                        this.combinationsPicked[i][j][k][l] = true;
                    }
                }
            }
        }
    }

    public GUI_Field[] createFields(Board board){
        Color mBlue = new Color(49,130,209);
        Color mOrange = new Color(221,107,32);
        Color mGreen = new Color(104,211,145);
        Color mGray = new Color(160,174,192);
        Color mRed = new Color(229,62,62);
        Color mWhite = new Color(255,255,255);
        Color mBrew = new Color(39,103,73);
        Color mPrison = new Color(113,128,150);
        Color mBlack = new Color(0,0,0);
        Color mYellow = new Color(246,224,94);
        Color mPurple = new Color(151,38,109);
        int numberOfFields = board.getFields().length;
        fieldSubtexts = new String[numberOfFields];
        GUI_Field[] guiFields = new GUI_Field[numberOfFields];
        //typer bliver sat op for at sammenligne med model attributter.
        int[] fieldColorIDs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
        Color[] guiFieldColors = {
                mBlue,mOrange,mGreen,
                mGray,mRed,mWhite,
                mYellow,mPurple,mWhite,
                mPrison,mWhite,mWhite,
                mBlack,mWhite,mBrew
        };
        //Loops through all fields
        for (int i = 0; i < numberOfFields; i++) {

            //Loops through all colors
            for (int j = 0; j < fieldColorIDs.length; j++) {
                switch (board.getFields()[i].getType()){
                    case ("start"):
                        guiFields[i] = new GUI_Start();
                        break;

                    case ("street"):
                        guiFields[i] = new GUI_Street();

                        fieldSubtexts[i] = "<h2>" + readFile(setupMessagesPath,"owner") + " " + readFile(setupMessagesPath,"none") +"<br>"
                                + readFile(setupMessagesPath,"price") +" " + ((Street)board.getFields()[i]).getPrice() + "<br>"
                                + readFile(setupMessagesPath,"housePrice") +" " + ((Street)board.getFields()[i]).getHousePrice() + "<br>"
                                + readFile(setupMessagesPath,"rent") +" " + ((Street)board.getFields()[i]).getRentLevels()[((Street)board.getFields()[i]).getHouseLevel()] + "<br>"
                        ;

                        break;
                    case ("incomeTax"):
                        guiFields[i] = new GUI_Tax();
                        break;
                    case("ordinaryTax"):
                        guiFields[i] = new GUI_Tax();
                        fieldSubtexts[i] = readFile(setupMessagesPath,"tax");
                        break;
                    case ("chance"):
                        guiFields[i] = new GUI_Chance();
                        fieldSubtexts[i] = readFile(setupMessagesPath,"");
                        break;
                    case ("brew"):
                        guiFields[i] = new GUI_Brewery();
                        fieldSubtexts[i] = "<h2>" + readFile(setupMessagesPath,"owner") + " " + readFile(setupMessagesPath,"none") +"<br>"
                                + readFile(setupMessagesPath,"price") +" " + ((Brewery)board.getFields()[i]).getPrice() + "<br>"
                                + readFile(setupMessagesPath,"rent") +" " + ((Brewery)board.getFields()[i]).getRent() + "<br>";

                        break;
                    case ("prison"):
                        guiFields[i] = new GUI_Jail();
                        break;
                    case ("visit"):
                        guiFields[i] = new GUI_Jail();
                        break;
                    case ("ferry"):
                        guiFields[i] = new GUI_Shipping();
                        fieldSubtexts[i] = "<h2>" + readFile(setupMessagesPath,"owner") + " " + readFile(setupMessagesPath,"none") +"<br>"
                                + readFile(setupMessagesPath,"price") +" " + ((Ferry)board.getFields()[i]).getPrice() + "<br>"
                                + readFile(setupMessagesPath,"rent") +" " + ((Ferry)board.getFields()[i]).getRent() + "<br>"
                        ;
                        break;
                    case ("parking"):
                        guiFields[i] = new GUI_Refuge();
                        break;
                }
            }
            for (int j = 0; j < fieldColorIDs.length; j++) {
                if (fieldColorIDs[j] == board.getFields()[i].getGroup()) {
                    guiFields[i].setBackGroundColor(guiFieldColors[j]);
                    if (fieldColorIDs[j] == 12)
                        guiFields[i].setForeGroundColor(mGreen);
                }
            }
            guiFields[i].setTitle(board.getFields()[i].getName());
            guiFields[i].setDescription(board.getFields()[i].getDescription());
            guiFields[i].setSubText("");
            if(board.getFields()[i].getType().equals("street")) guiFields[i].setDescription(fieldSubtexts[i]);
            if(board.getFields()[i].getType().equals("ferry")) guiFields[i].setDescription(fieldSubtexts[i]);
            if(board.getFields()[i].getType().equals("brew")) guiFields[i].setDescription(fieldSubtexts[i]);

        }
        return guiFields;
    }

    private GUI_Player manuallyCreateGuiPlayer(String playerName){
        /**
         * The manuallyCreateGuiPlayer method is a recursion function
         * that lets the player pick colors, type and pattern of his vehicle
         * thereafter if checks if his preferred combination matches with a
         * previously picked combination. if it doesn't, then his choice will
         * be stored in the combinationsPicked array
         */
        //This is the player that we will create
        GUI_Player player;

        //User will check for unique combination later.
        int playerCombination[] = {0,0,0,0}; //new int[4]

        //The choices are initiated with default values to avoid nullPointers (Testing)
        GUI_Car.Type guiCarChoice = GUI_Car.Type.TRACTOR;
        Color guiPrimaryColorChoice = Color.BLACK;
        Color guiSecondaryColorChoice = Color.WHITE;
        GUI_Car.Pattern guiPatternChoice = GUI_Car.Pattern.DIAGONAL_DUAL_COLOR;

        //CarTypeOptions
        String[] cars = {"car", "racecar", "tractor", "ufo"};
        GUI_Car.Type[] guiCars = {
                GUI_Car.Type.CAR,
                GUI_Car.Type.RACECAR,
                GUI_Car.Type.TRACTOR,
                GUI_Car.Type.UFO};

        //CarPrimaryColorOptions
        String[] primaryColors = {"red","blue","green","yellow"};
        Color[] guiPrimaryColors = {
                new Color(245,101,101),
                new Color(99,179,237),
                new Color(104,143,51),
                new Color(250,240,137)};

        //CarSecondaryColorOptions
        String[] secondaryColors = {"red","blue","gray","black"};
        Color[] guiSecondaryColors = {
                new Color(155,44,44),
                new Color(42,67,101),
                new Color(160,174,192),
                new Color(26,32,44 )};

        //CarPatternOptions
        String[] patterns = {"checkered","gradient","fill","zebra"};
        GUI_Car.Pattern[] guiPatterns = {
                GUI_Car.Pattern.CHECKERED,
                GUI_Car.Pattern.HORIZONTAL_GRADIANT,
                GUI_Car.Pattern.FILL,
                GUI_Car.Pattern.ZEBRA};

        //The player picks his vehicel
        String carChoice = gui.getUserButtonPressed(
                playerName + " Please pick a vehicel",
                cars[0],cars[1],cars[2],cars[3]
        );
        for ( int i = 0; i < cars.length; i++ ) {
            if (carChoice.equals(cars[i])) {
                guiCarChoice = guiCars[i];
                playerCombination[0] = i;
            }
        }

        //The player picks his primary color
        String primaryColorChoice = gui.getUserButtonPressed(
                playerName + "Please pick a primary color",
                primaryColors[0],primaryColors[1],primaryColors[2],primaryColors[3]
        );
        for ( int i = 0; i < primaryColors.length; i++ ) {
            if (primaryColorChoice.equals(primaryColors[i])) {
                guiPrimaryColorChoice = guiPrimaryColors[i];
                playerCombination[1] = i;
            }
        }

        //The player picks his secondary color
        String secondaryColorChoice = gui.getUserButtonPressed(
                playerName + "Please pick a secondary color",
                secondaryColors[0],secondaryColors[1],secondaryColors[2],secondaryColors[3]
        );
        for ( int i = 0; i < primaryColors.length; i++ ) {
            if (secondaryColorChoice.equals(secondaryColors[i])) {
                guiSecondaryColorChoice = guiSecondaryColors[i];
                playerCombination[2] = i;
            }
        }

        //The player picks his pattern
        String carPatternChoice = gui.getUserButtonPressed(
                playerName + "Please pick a pattern",
                patterns[0],patterns[1],patterns[2],patterns[3]
        );
        for ( int i = 0; i < primaryColors.length; i++ ) {
            if (carPatternChoice.equals(patterns[i])) {
                guiPatternChoice = guiPatterns[i];
                playerCombination[3] = i;
            }
        }

        //The program checks if the players combination is unique
        if (this.combinationsPicked[playerCombination[0]][playerCombination[1]][playerCombination[2]][playerCombination[3]] == false){
            gui.showMessage("Some one else has the same try something else");
            return manuallyCreateGuiPlayer(playerName);
        }
        else{
            this.combinationsPicked[playerCombination[0]][playerCombination[1]][playerCombination[2]][playerCombination[3]] = false;
            player = new GUI_Player(playerName,30000,new GUI_Car(guiPrimaryColorChoice,guiSecondaryColorChoice,guiCarChoice,guiPatternChoice));
            gui.showMessage("To the happiness your choice is unique.");
        }
        return player;
        //
    }
    private void theStaticCars(){
        /**
         * This method just automatically creates some different cars for the players.
         * the player will have no choice to make his own car.
         */
        this.guiCars = new GUI_Car[]{
                new GUI_Car(Color.GREEN, Color.GREEN, GUI_Car.Type.CAR, GUI_Car.Pattern.FILL),
                new GUI_Car(Color.RED, Color.RED, GUI_Car.Type.CAR, GUI_Car.Pattern.FILL),
                new GUI_Car(Color.YELLOW, Color.YELLOW, GUI_Car.Type.CAR, GUI_Car.Pattern.FILL),
                new GUI_Car(Color.WHITE, Color.WHITE, GUI_Car.Type.CAR, GUI_Car.Pattern.FILL),
                new GUI_Car(Color.BLUE, Color.BLUE, GUI_Car.Type.CAR, GUI_Car.Pattern.FILL),
                new GUI_Car(Color.BLACK, Color.BLACK, GUI_Car.Type.CAR, GUI_Car.Pattern.FILL)};
    }

    public String[] setupPlayers(){
        /**
         * This method is central for the game, it creates all the players that
         * you see on the board.
         */
        gui.showMessage(readFile(setupMessagesPath,"welcome"));
        int numberOfPlayers = Integer.parseInt(gui.getUserSelection(readFile(setupMessagesPath,"choosePlayerNumber"),"3","4","5","6"));
        String[] playerNames = new String[numberOfPlayers];
        //theStaticCars();
        String playerNameMessage;
        int i=0;
        int nameErrors;

        //Loops through all players and asks for names, checking that they are valid
        while (i<numberOfPlayers) {
            nameErrors=0;
            playerNameMessage = readFile(setupMessagesPath,"player") + " " + (i + 1) +" " + readFile(setupMessagesPath,"writeName");
            playerNames[i] = gui.getUserString(playerNameMessage);

            //If someone else has same name
            for (int j=0;j<i;j++){
                if(playerNames[i].equals(playerNames[j])){
                    nameErrors++;
                }
            }

            //If name is empty or just " "
            if (playerNames[i].isEmpty() || playerNames[i].equals(" ")){
                nameErrors++;
            }

            if (nameErrors==0){
                //If name is valid, it is next players turn
                i++;
            } else{ //else, shows errorMessage
                gui.showMessage(readFile(setupMessagesPath,"nameError"));
            }
        }



        setupGuiPlayers(playerNames);

        return playerNames;
    }
    
    public void rollDiceAndMove(int[] faceValues, int sum,int activePlayerId, int oldFieldId){
        String guiActivePlayerName = guiPlayers[activePlayerId].getName();

        if ((guiActivePlayerName.indexOf( "tabt" )) == -1  && counterForWinner != guiPlayers.length - 1) {
            // "looser" does not exist in playerName and  there are players on board more than one

            gui.setDice( faceValues[0], faceValues[1] );

            for (int i = 0; i < sum; i++) {
                teleportPlayerCar( activePlayerId, 1, (oldFieldId + i) % fields.length );
                try {
                    Thread.sleep( 200);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        } else if (counterForWinner == guiPlayers.length - 1){
            // There is one player on board Todo
        }
    }

    public void rollDiceInPrison(int[] faceValues){
//        gui.getUserButtonPressed("", "rollDice");
        getUserButtonPressed( "" ,"rollDice" );
        gui.setDice( faceValues[0], faceValues[1] );
    }

    public void teleportPlayerCar(int playerId, int dieRoll, int oldFieldId){
        //Moves the player dieRoll fields forward
        //If fromJail, the player won't get his bonus when passing start

        int newPosition = (oldFieldId+dieRoll)%fields.length;

        //removes the guiPlayer from the old position
        fields[oldFieldId].setCar(guiPlayers[playerId],false);

        //Moves the guiPlayer to the new position
        fields[newPosition].setCar(guiPlayers[playerId],true);
    }

    private void setupGuiPlayers(String[] playerNames){
        this.guiPlayers = new GUI_Player[playerNames.length];
        for (int i=0;i<playerNames.length;i++){
            //Static car
            //this.guiPlayers[i] = new GUI_Player(playerNames[i],30000,this.guiCars[i]);
            //Pick your own car
            guiPlayers[i] = manuallyCreateGuiPlayer(playerNames[i]);
            this.gui.addPlayer(this.guiPlayers[i]);

           this.fields[0].setCar(this.guiPlayers[i],true);
        }
    }


    public void updatePlayerBalances(int[] playerBalances) {
        //Updates the balances of all players on the board
        for (int i =0; i<playerBalances.length;i++){
            guiPlayers[i].setBalance(playerBalances[i]);
        }
    }

    public void updateOwnerships(Board board) {
        int ownerId;
        for (int i =0;i<fields.length;i++){

            switch (board.getFields()[i].getType()){
                case ("street"):
                    ownerId = ((Ownable)board.getFields()[i]).getOwnerId();
                    if (ownerId>=0) {
                        fieldSubtexts[i] = "<h2>" + readFile(setupMessagesPath, "owner") + " " + guiPlayers[ownerId].getName() + "<br>"
                                + readFile(setupMessagesPath, "price") + " " + ((Street) board.getFields()[i]).getPrice() + "<br>"
                                + readFile(setupMessagesPath, "housePrice") + " " + ((Street) board.getFields()[i]).getHousePrice() + "<br>"
                                + readFile(setupMessagesPath, "rent") + " " + ((Street) board.getFields()[i]).getRent() + "<br>"
                        ;
                    }
                    else{
                        fieldSubtexts[i] = "<h2>" + readFile(setupMessagesPath,"owner") + " " + readFile(setupMessagesPath,"none")  +"<br>"
                            + readFile(setupMessagesPath,"price") +" " + ((Street)board.getFields()[i]).getPrice() + "<br>"
                            + readFile(setupMessagesPath,"housePrice") +" " + ((Street)board.getFields()[i]).getHousePrice() + "<br>"
                            + readFile(setupMessagesPath,"rent") +" " + ((Street)board.getFields()[i]).getRent() + "<br>"
                        ;
                    }
                    fields[i].setDescription(fieldSubtexts[i]);
                    if (((Street)board.getFields()[i]).getHouseLevel() < 5){
                        ((GUI_Street)fields[i]).setHouses(((Street)board.getFields()[i]).getHouseLevel());
                        //((GUI_Street)fields[i]).setHotel(false);

                    } else{ //if house level is 5
                        ((GUI_Street)fields[i]).setHouses(0);
                        ((GUI_Street)fields[i]).setHotel(true);
                    }

                    //Adds "pawned" if it is pawned
                    if (((Ownable)board.getFields()[i]).isPledged()){
                        fields[i].setSubText(readFile(setupMessagesPath,"pawned"));
                    } else{
                        fields[i].setSubText(readFile(setupMessagesPath,""));
                    }
                    break;

                case ("brew"):
                    ownerId = ((Ownable)board.getFields()[i]).getOwnerId();
                    if (ownerId>=0) {
                        fieldSubtexts[i] = "<h2>" + readFile(setupMessagesPath, "owner") + " " + guiPlayers[ownerId].getName() + "<br>>"
                                + readFile(setupMessagesPath, "price") + " " + ((Brewery) board.getFields()[i]).getPrice() + "<br>"
                                + readFile(setupMessagesPath, "rent") + " " + ((Brewery) board.getFields()[i]).getRent() + "<br>";
                        fields[i].setDescription(fieldSubtexts[i]);
                    }
                    else{
                        fieldSubtexts[i] = "<h2>" +readFile(setupMessagesPath,"owner") + " " + readFile(setupMessagesPath,"none") +"<br>>"
                            + readFile(setupMessagesPath,"price") +" " + ((Brewery)board.getFields()[i]).getPrice() + "<br>"
                            + readFile(setupMessagesPath,"rent") +" " + ((Brewery)board.getFields()[i]).getRent() + "<br>";
                        fields[i].setDescription(fieldSubtexts[i]);
                    }
                    //Adds "pawned" if it is pawned
                    if (((Ownable)board.getFields()[i]).isPledged()){
                        fields[i].setSubText(readFile(setupMessagesPath,"pawned"));
                    } else{
                        fields[i].setSubText(readFile(setupMessagesPath,""));
                    }
                    break;

                case ("ferry"):
                    ownerId = ((Ownable)board.getFields()[i]).getOwnerId();
                    if (ownerId>=0)
                    fieldSubtexts[i] = "<h2>" + readFile(setupMessagesPath,"owner") + " " + guiPlayers[ownerId].getName() +"<br>"
                            + readFile(setupMessagesPath,"price") +" " + ((Ferry)board.getFields()[i]).getPrice() + "<br>"
                            + readFile(setupMessagesPath,"rent") +" " + ((Ferry)board.getFields()[i]).getRent() + "<br>"
                    ;
                    else{
                        fieldSubtexts[i] = "<h2>" + readFile(setupMessagesPath,"owner") + " " + readFile(setupMessagesPath,"none")  +"<br>"
                                + readFile(setupMessagesPath,"price") +" " + ((Ferry)board.getFields()[i]).getPrice() + "<br>"
                                + readFile(setupMessagesPath,"rent") +" " + ((Ferry)board.getFields()[i]).getRent() + "<br>"
                        ;

                    }
                    fields[i].setDescription(fieldSubtexts[i]);

                    //Adds "pawned" if it is pawned
                    if (((Ownable)board.getFields()[i]).isPledged()){
                        fields[i].setSubText(readFile(setupMessagesPath,"pawned"));
                    } else{
                        fields[i].setSubText(readFile(setupMessagesPath,""));
                    }
                    break;
            }
        }
    }

    public boolean buyFieldOrNot(int activePlayerId,int fieldId){
        String question =String.format(readFile(turnMessagesPath,"buyField?"),guiPlayers[activePlayerId].getName(),fields[fieldId].getTitle());
        String selection = gui.getUserButtonPressed(question,readFile(turnMessagesPath,"no"),readFile(turnMessagesPath,"yes"));
        if (selection.equals(readFile(turnMessagesPath,"no"))){
            return false;
        } else{
            return true;
        }

    }

    public boolean payIncomeTax(String message){
        String selection = gui.getUserSelection(message,readFile(turnMessagesPath,"pay4kTax"),readFile(turnMessagesPath,"pay10pct"));
        if(readFile(turnMessagesPath,"pay4kTax").equals(selection)){
            return true;
        }
        else {
            return false;
        }
    }
    public void showMessage(String message){
        gui.showMessage(message);
    }


    public void prisonMessage(){
//TODO slet
    }

    public boolean chooseToBuy(int activePlayerId){
        String message = String.format(readFile(turnMessagesPath,"buyBeforeTurn"),guiPlayers[activePlayerId].getName());
        String selection = gui.getUserButtonPressed(message,
                readFile(turnMessagesPath,"no"),readFile(turnMessagesPath,"yes"));
        if(selection.equals(readFile(turnMessagesPath,"yes"))){
            return true;
        } else {
            return false;
        }
    }

    public int getWantedNumberOfHouses(int fieldId, int activePlayerId){
        String message = String.format(readFile(turnMessagesPath,"howManyHouses"),guiPlayers[activePlayerId].getName(),fields[fieldId].getTitle());
        String selection = gui.getUserSelection(message,"0","1","2","3","4","5");
        int numberOfHouses = Integer.parseInt(selection);
        return numberOfHouses;
    }

    public void newTurnMessage(int activePlayerId){
        //Resets the text on the ChanceCardField
        gui.displayChanceCard(readFile(turnMessagesPath,"getLucky"));
        String newTurnMessage = String.format(readFile(turnMessagesPath,"newTurn"),guiPlayers[activePlayerId].getName());
        gui.showMessage(newTurnMessage);
    }

    public void showChanceCard(String cardText){
        gui.displayChanceCard(cardText);
    }

    public void looserMessage(int activePlayerId) {
        String msg = String.format( readFile( endMessagePath,"loser" ),guiPlayers[activePlayerId].getName());
        gui.showMessage(msg);
    }

    public void removeLoser(int playerId, int oldFieldId) {
        // When the counterForWinner = playerNames.length-1, so we know that there is one player on board
        counterForWinner++;
        fields[oldFieldId].setCar( guiPlayers[playerId], false );
        updateLooserOnBoard( playerId );
    }

    public void updateLooserOnBoard(int playerId) {
        guiPlayers[playerId].setName( guiPlayers[playerId].getName() + readFile( endMessagePath, "updateLooserOnBoard" ));
        guiPlayers[playerId].setBalance(0);
    }

   public void endGame(String winnerName){
       gui.showMessage(String.format( readFile( endMessagePath, "winner" ) ,   winnerName ));
       gui.close();
       System.exit( 0 );
   }

   public String getUserButtonPressed(String msg,String ... options){
        return gui.getUserButtonPressed(msg,options);
   }

    public String getUserSelection(String msg,String ... options){
        return gui.getUserSelection(msg,options);
    }
}