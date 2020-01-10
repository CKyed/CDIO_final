package controller;
import static controller.PathExpert.namePath;
import static controller.TextController.readFile;
import static controller.PathExpert.fieldAttributesPath;

import gui_fields.*;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
import gui_fields.GUI_Refuge;
import gui_fields.GUI_Start;
import gui_main.GUI;
import model.*;
import model.Fields.*;
import model.Fields.OwnableFile.*;

import java.awt.*;

public class ViewController {
    private GUI gui;
    private GUI_Field[] fields;
    private GUI_Player[] guiPlayers;
    private GUI_Car[] guiCars;

    public ViewController(Board board) {
        GUI_Field[] fields = createFields(board);
        this.fields = fields;
        this.gui = new GUI(fields);



    }

    public GUI_Field[] createFields(Board board){
        int numberOfFields = board.getFields().length;
        GUI_Field[] guiFields = new GUI_Field[numberOfFields];
        //typer bliver sat op for at sammenligne med model attributter.
        int[] fieldColorIDs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
        Color[] guiFieldColors = {
                Color.BLUE, Color.RED, Color.CYAN,
                Color.YELLOW, Color.WHITE, Color.BLACK,
                Color.MAGENTA, Color.GRAY, Color.GREEN,
                Color.PINK, Color.ORANGE, Color.LIGHT_GRAY,
                Color.DARK_GRAY, Color.darkGray,Color.darkGray
        };
        for (int i = 0; i < numberOfFields; i++) {

            for (int j = 0; j < fieldColorIDs.length; j++) {
                switch (board.getFields()[i].getType()){
                    case ("start"):
                        guiFields[i] = new GUI_Start();
                        break;
                    case ("street"):
                        guiFields[i] = new GUI_Street();
                        break;
                    case ("incomeTax"):
                        guiFields[i] = new GUI_Tax();
                        break;
                    case("ordinaryTax"):
                        guiFields[i] = new GUI_Tax();
                        break;
                    case ("chance"):
                        guiFields[i] = new GUI_Chance();
                        break;
                    case ("brew"):
                        guiFields[i] = new GUI_Brewery();
                        break;
                    case ("prison"):
                        guiFields[i] = new GUI_Jail();
                        break;
                    case ("ferry"):
                        guiFields[i] = new GUI_Shipping();
                        break;
                    case ("parking"):
                        guiFields[i] = new GUI_Refuge();
                        break;
                }
            }
            for (int j = 0; j < fieldColorIDs.length; j++) {
                if (fieldColorIDs[j] == board.getFields()[i].getGroup()) {
                    guiFields[i].setBackGroundColor(guiFieldColors[j]);
                }
            }
            guiFields[i].setTitle(board.getFields()[i].getName());
            guiFields[i].setDescription("");
            guiFields[i].setSubText(null);
        }
        return guiFields;
    }

    public String[] setupPlayers(){
        int numberOfPlayers = Integer.parseInt(gui.getUserSelection("","3","4","5","6"));

        String[] playerNames = new String[numberOfPlayers];

        this.guiCars = new GUI_Car[numberOfPlayers];

        for (int i =0;i<numberOfPlayers;i++) {
            playerNames[i] = gui.getUserString("");
/*
            while (playerNames[i].equals("")||playerNames[i].equals(" ")){
                System.out.println("Indtast nyt navn"); //TODO GUI-meddelese
                playerNames[i] = gui.getUserString("");
            }
*/
            while(playerNames[i].isEmpty()){
                System.out.println("prøv igen ");
                playerNames[i] = gui.getUserString("");
            }

            for (int j = 0; j < numberOfPlayers; j++) {

                if (playerNames[i].equals(playerNames[j]) && i != j)
                playerNames[i] = gui.getUserString("");

            }




            int numberOfLetters = playerNames[i].length();
            /*
            for(int j = 0; j<numberOfLetters;j++) {
               char c = playerNames[i].charAt(j);
               while
            }

         */
            //Spilleren vælger deres bil
            //this.guiCars[i] = new GUI_Car(Color.BLUE,Color.BLUE,GUI_Car.Type.CAR,GUI_Car.Pattern.FILL);

        }

        //tjek om navne er tomme :P

        //tjek om navne er ens

        this.guiCars = new GUI_Car[]{new GUI_Car(Color.GREEN, Color.GREEN, GUI_Car.Type.CAR, GUI_Car.Pattern.FILL),
                new GUI_Car(Color.RED, Color.RED, GUI_Car.Type.CAR, GUI_Car.Pattern.FILL), new GUI_Car(Color.YELLOW, Color.YELLOW, GUI_Car.Type.CAR, GUI_Car.Pattern.FILL), new GUI_Car(Color.WHITE, Color.WHITE, GUI_Car.Type.CAR, GUI_Car.Pattern.FILL), new GUI_Car(Color.BLUE, Color.BLUE, GUI_Car.Type.CAR, GUI_Car.Pattern.FILL), new GUI_Car(Color.BLUE, Color.BLUE, GUI_Car.Type.CAR, GUI_Car.Pattern.FILL)};


        setupGuiPlayers(playerNames);

        return playerNames;
    }
    
    public void rollDiceAndMove(int[] faceValues, int sum,int activePlayerId, int oldFieldId){
        gui.setDice(faceValues[0],faceValues[1]);
        gui.showMessage("");

        for (int i =0;i<sum;i++){
            teleportPlayerCar(activePlayerId,1,(oldFieldId+i)% fields.length);
            try
            {
                Thread.sleep(200);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
        }
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
            this.guiPlayers[i] = new GUI_Player(playerNames[i],30000,this.guiCars[i]);
            this.gui.addPlayer(guiPlayers[i]);

           this.fields[0].setCar(this.guiPlayers[i],true);
        }





    }


    public void updatePlayerBalances(int[] playerBalances) {
        //Updates the balances of all players on the board
        for (int i =0; i<playerBalances.length;i++){
            guiPlayers[i].setBalance(playerBalances[i]);
        }
    }

    public void updateOwnerships() {

    }

    public boolean buyFieldOrNot(int activePlayerId){
        String selection = gui.getUserSelection("","0","1");
        int selectionInt = Integer.parseInt(selection);
        if (selectionInt==0){
            return false;
        } else{
            return true;
        }

    }

    public boolean payIncomeTax(String message){
        String selection = gui.getUserSelection(message,"Betal 4000 i skat","Betal 10% i skat");
        if(selection.equals("Betal 4000 i skat")){
            return true;
        }
        else {
            return false;
        }
    }


}
