package controller;

import model.DiceCup;

public class DiceController {
    private DiceCup diceCup;
    private int[] faceValues;

    private boolean sameValue =false;

    /**
     * Dice constructor, this is where you can choose number of dice and number of sides
     */
    public DiceController(){
        this.diceCup = new DiceCup(2,6);
    }

    /**
     * Rolls the dice and checks for two equal rolls, if the facevalues are the same, then the counter becomes one higher
     *
     */

    public void roll() {
        this.diceCup.rollDice();
        this.faceValues = new int[diceCup.getNumberOfDice()];
        for (int i = 0; i<diceCup.getNumberOfDice(); i++){
            this.faceValues[i] = diceCup.getDice()[i].getFaceValue();
        }
        this.sameValue = true;
        int counter =0;
        while (sameValue==true){
            if(counter==diceCup.getNumberOfDice()-1){
                break;
            }

            if (faceValues[counter] != faceValues[counter+1]){
                this.sameValue=false;
            }
            //TODO: maybe there is a problem with this counter, it counts even if the faceValues are not the same
            counter++;
        }

        for (int i = 0; i<diceCup.getNumberOfDice(); i++){

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
