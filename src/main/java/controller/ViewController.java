package controller;

import gui_fields.*;
import gui_main.GUI;
import model.*;

import java.awt.*;

public class ViewController {
    private GUI gui;


    public ViewController(Board board) {
        GUI_Field[] fields = createFields(board);
        this.gui = new GUI(fields);


    }

    public GUI_Field[] createFields(Board board) {
        int numberOfFields = board.getFields().length;
        GUI_Field[] guiFields = new GUI_Field[numberOfFields];

        //typer bliver sat op for at sammenligne med model attributter.
        String[] fieldTypes = {"start", "street", "tax", "chance", "brew", "prison", "ferry", "parking"};
        GUI_Field[] guiFieldTypes = {
                new GUI_Start(),
                new GUI_Street(),
                new GUI_Tax(),
                new GUI_Chance(),
                new GUI_Brewery(),
                new GUI_Jail(),
                new GUI_Shipping(),
                new GUI_Refuge()
        };
        int[] fieldColorIDs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
        Color[] guiFieldColors = {
                Color.BLUE, Color.RED, Color.CYAN,
                Color.YELLOW, Color.WHITE, Color.BLACK,
                Color.MAGENTA, Color.GRAY, Color.GREEN,
                Color.PINK, Color.ORANGE, Color.LIGHT_GRAY,
                Color.DARK_GRAY, Color.darkGray
        };

        for (int i = 0; i < numberOfFields; i++) {

            for (int j = 0; j < fieldTypes.length; j++) {
                if (fieldTypes[j].equals(board.getFields()[i].getType()))
                    guiFields[i] = guiFieldTypes[j];
            }
            for (int j = 0; j < fieldColorIDs.length; j++) {
                if (fieldColorIDs[j] == board.getFields()[i].getGroup())
                    guiFields[i].setBackGroundColor(guiFieldColors[j]);
            }
            guiFields[i].setTitle(board.getFields()[i].getName());
            guiFields[i].setDescription("");
            guiFields[i].setSubText(null);
            guiFields[i].setForeGroundColor(Color.black);
        }
        return guiFields;
    }
}