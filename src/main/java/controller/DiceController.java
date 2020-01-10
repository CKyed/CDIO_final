package controller;

import model.DiceCup;

public class DiceController {
    private DiceCup diceCup;
    int[] eyes;

    //sends dice values for gui
    public int[] diceValues(){
        eyes = diceCup.throwDice();
        return eyes;
    }

    public int sum(){
        int sum = 0;
        for (int i = 0; i < eyes.length; i++) {
            sum += eyes[i];
        }
        return sum;
    }

    public boolean equals(){
        boolean equals = false;
        for (int i = 0; i < eyes.length; i++) {
            if (eyes[i] == eyes[(i+1)%eyes.length])
                equals = true;
        }
        return equals;
    }
}