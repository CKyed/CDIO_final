package controller;

import model.DiceCup;

public class DiceController {
    private DiceCup diceCup;
    private int[] faceValues;
    private boolean sameValue =false;


    public DiceController(){
        this.diceCup = new DiceCup(2,6);
    }

    public void roll() {
        //Rolls and updates the diceNumber and the sameValue variables
        this.diceCup.rollDice();
        this.faceValues = new int[diceCup.getDiceNumber()];
        for (int i = 0; i<diceCup.getDiceNumber();i++){
            this.faceValues[i] = diceCup.getDice()[i].getFaceValue();
        }
        this.sameValue = true;
        int counter =0;
        while (sameValue==true){
            if(counter==diceCup.getDiceNumber()-1){
                break;
            }

            if (faceValues[counter] != faceValues[counter+1]){
                this.sameValue=false;
            }

            counter++;
        }


        for (int i =0; i<diceCup.getDiceNumber();i++){



        }

    }




}
