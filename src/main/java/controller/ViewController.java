package controller;
import static controller.PathExpert.namePath;
import static controller.TextController.readFile;
import static controller.PathExpert.fieldAttributesPath;

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


    public ViewController(Board board) {
        GUI_Field[] fields = createFields(board);
        this.fields = fields;
        this.gui = new GUI(fields);

    }

    public GUI_Field[] createFields(Board board){
        int numberOfFields = board.getFields().length;
        GUI_Field[] guiFields = new GUI_Field[numberOfFields];


        // Her bliver det simpelt
        for (int i=0;i<numberOfFields;i++){
            if (board.getFields()[i].getName().equals(readFile(namePath,"start")) ){
                guiFields[i] = new GUI_Start();
                guiFields[i].setBackGroundColor(Color.red);
                guiFields[i].setTitle("Start");
            }

            if (board.getFields()[i].getName().equals(readFile(namePath,"carpark")) ){
                guiFields[i] = new GUI_Refuge();
                guiFields[i].setBackGroundColor(Color.blue);
                guiFields[i].setTitle(readFile(namePath,"carpark"));
            }


            if (board.getFields()[i].getName().equals(readFile(namePath,"start")) ){
                guiFields[i] = new GUI_Start();
                guiFields[i].setBackGroundColor(Color.red);
                guiFields[i].setTitle("Start");
            }




        }
        return guiFields;
    }

    public String[] setupPlayers(){
        int numberOfPlayers = Integer.parseInt(gui.getUserSelection("","3","4","5","6"));

        String[] playerNames = new String[numberOfPlayers];

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
        }

        //tjek om navne er tomme :P

        //tjek om navne er ens

        //Setup GUI-players

        return playerNames;
    }
    
    public void rollDiceAndMove(int[] faceValues, int sum,int activePlayerId, int[] oldFieldIds, int numberOfPlayers){
        gui.setDice(faceValues[0],faceValues[1]);

        for (int i =0;i<sum;i++){
            teleportPlayerCar(activePlayerId,1,oldFieldIds);
            try
            {
                Thread.sleep(100);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void teleportPlayerCar(int playerId, int dieRoll, int[] oldFieldIds){
        //Moves the player dieRoll fields forward
        //If fromJail, the player won't get his bonus when passing start

        int activePlayerOldFieldId = oldFieldIds[playerId];
        int newPosition = (activePlayerOldFieldId+1)%40;


        //counts how many players are on that field
        int numberOfPlayersOnField =0;
        for (int i=0;i<oldFieldIds.length;i++){
            if (oldFieldIds[i]==activePlayerOldFieldId){
                numberOfPlayersOnField++;
            }
        }


        //Makes array of playerID's of players on oldField
        int[] playersOnOldFieldIds = new int[numberOfPlayersOnField];

        //Fills the array
        for (int i=0;i<oldFieldIds.length;i++){
            int j=0;
            if (oldFieldIds[i]==activePlayerOldFieldId){
                playersOnOldFieldIds[j] = oldFieldIds[i];
                j++;
            }
        }

        //Removes all players from old field
        this.fields[activePlayerOldFieldId].removeAllCars();

        //Puts the remaining cars back again
        for (int i=0;i<playersOnOldFieldIds.length;i++){
            if (playerId!=playersOnOldFieldIds[i]){
                this.fields[activePlayerOldFieldId].setCar(guiPlayers[i],true);
            }
        }

        //Moves the guiPlayer to the new position
        fields[newPosition].setCar(guiPlayers[playerId],true);
    }


    public void updatePlayerBalances() {
    }

    public void updateOwnerships() {
    }
}
