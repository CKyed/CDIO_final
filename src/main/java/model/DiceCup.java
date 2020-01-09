package model;

public class DiceCup {
    private Die dieOne = new Die();
    private Die dieTwo = new Die();
    private int[] diceValue;

    public int[] throwDice(){
        diceValue = new int[]{dieOne.roll(), dieTwo.roll()};
        return diceValue;
    }
}
