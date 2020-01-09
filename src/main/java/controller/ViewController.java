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


    String[] players = {"c", "a", "b", "e"};
    //Shuffels the chance card deck
    public  void shuffle(){
        for (int i=0; i<1000; i++ ){
            int a= (int) (Math.random()*players.length);
            int b= (int) (Math.random()*players.length);
            swap(a,b);
        }
    }

    public  void swap(int a, int b){
        String cardA = players[a];
        String cardB = players[b];
        players[a] = cardB ;
        players[b] = cardA;
    }

    /**
     * i take care of first element in array
     * j take care of second element in array
     * compareToIgnoreCase Example https://beginnersbook.com/2013/12/java-string-comparetoignorecase-method-example/
     */
    public  void alphabetSort() {
        for (int i = 0; i < players.length; i++) {
            for (int j = i + 1; j < players.length; j++) {
                if (players[i].compareToIgnoreCase( players[j] ) > 0) {
                    String temp = players[i];
                    players[i] = players[j];
                    players[j] = temp;

                }
            }
        }
    }

}
