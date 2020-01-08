package model;

public class DiceCup {
    private int diceNumber = 1;
    private int dieSum = 0;
    private Die[] dice;

    public DiceCup(int diceNumber, int diceSideNumber) {
        this.diceNumber = diceNumber;
        dice = new Die[diceNumber];
        for (int i = 0; i < diceNumber; i++) {
            dice[i] = new Die(diceSideNumber);
        }
    }

    public void rollDice() {
        int sum = 0;
        for (int i = 0; i < diceNumber; i++) {
            dice[i].roll();
        }
    }

    public int getDiceNumber() {
        return diceNumber;
    }

    public Die[] getDice() {
        return dice;
    }
}
