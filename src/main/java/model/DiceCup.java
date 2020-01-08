package model;

public class DiceCup {
    private int numberOfDice;
    private int dieSum;
    private Die[] dice;

    public DiceCup(int numberOfDice, int numberOfSides) {
        this.numberOfDice = numberOfDice;
        dice = new Die[numberOfDice];
        for (int i = 0; i < numberOfDice; i++) {
            dice[i] = new Die(numberOfSides);
        }
    }

    public void rollDice() {
        int sum = 0;
        for (int i = 0; i < numberOfDice; i++) {
            dice[i].roll();
        }
    }

    public int getNumberOfDice() {
        return numberOfDice;
    }

    public Die[] getDice() {
        return dice;
    }

    public int getDieSum(){
        int dieSum = 0;
        for(int i =0; i<numberOfDice; i++){
          dieSum += this.dice[i].getFaceValue();
        }
        return dieSum;
    }

}
