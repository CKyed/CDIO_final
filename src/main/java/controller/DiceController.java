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
        this.faceValues = diceCup.getFaceValues();

        //Checks if all dice have same faceValues
        this.sameValue = true;
        int counter =0;
        while (sameValue==true){
            if(counter==diceCup.getNumberOfDice()-1){
                break;
            }

            if (faceValues[counter] != faceValues[counter+1]){
                this.sameValue=false;
            }

            counter++;
        }
    }

    public int[] getFaceValues(){
        return diceCup.getFaceValues();
    }

    public int getSum(){
        return this.diceCup.getDieSum();
    }

    public boolean isSameValue() {
        return sameValue;
    }
}
