package model;

public class DiceCup {
    private int diceNumber;
    private int dieSum = 0;
    private Die[] dice;

    public DiceCup(int numberOfDice, int numberOfSides) {
        this.diceNumber = numberOfDice;
        dice = new Die[numberOfDice];
        for (int i = 0; i < numberOfDice; i++) {
            dice[i] = new Die(numberOfSides);
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
