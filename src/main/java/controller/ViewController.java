package controller;

import gui_fields.GUI_Field;
import gui_fields.GUI_Start;
import gui_main.GUI;
import model.*;

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
            if (board.getFields()[i].getName().equals("Start")){
                guiFields[i] = new GUI_Start();
                guiFields[i].setBackGroundColor(Color.red);
                guiFields[i].setTitle("Start");
                guiFields[i].setSubText("Modtag 4000 kr.");
            }
        }
        return guiFields;
    }

}
