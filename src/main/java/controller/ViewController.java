package controller;
import static controller.PathExpert.namePath;
import static controller.TextController.readFile;
import static controller.PathExpert.fieldAttributesPath;

import gui_fields.*;
import gui_fields.GUI_Field;
import gui_fields.GUI_Refuge;
import gui_fields.GUI_Start;
import gui_main.GUI;
import model.*;
import model.Fields.*;
import model.Fields.OwnableFile.*;

import java.awt.*;

public class ViewController {
    private GUI gui;
    private GUI_Player[] guiPlayers;


    public ViewController(Board board) {
        GUI_Field[] fields = createFields(board);

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
                    case ("tax"):
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

        setupGuiPlayers(playerNames);

        return playerNames;
    }

    public void rollDiceAndMove(int[] faceValues, int sum){

    }

    private void setupGuiPlayers(String[] playerNames){
        for (int i=0;i<playerNames.length;i++){
            this.guiPlayers[i] = new GUI_Player(playerNames[i]);
        }





    }


    public void updatePlayerBalances() {
    }

    public void updateOwnerships() {
    }
}
