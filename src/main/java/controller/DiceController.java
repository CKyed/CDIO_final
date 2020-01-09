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
}
