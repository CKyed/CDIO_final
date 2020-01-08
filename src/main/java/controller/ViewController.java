package controller;
import static controller.PathExpert.namePath;
import static controller.TextController.readFile;
import static controller.PathExpert.fieldAttributesPath;

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


    public ViewController(Board board) {
        GUI_Field[] fields = createFields(board);
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

        for (int i =0;i<numberOfPlayers;i++){
            playerNames[i] = gui.getUserString("");

        }

        return playerNames;
    }



}
