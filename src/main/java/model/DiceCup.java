package model;

public class DiceCup {
    private int numberOfDice;
    private int dieSum;
    private Die[] dice;
    private int[] faceValues;

    //Creates an array with a number of dice and a number of sides, defined by the two variables
    public DiceCup(int numberOfDice, int numberOfSides) {
        this.numberOfDice = numberOfDice;
        this.faceValues= new int[numberOfDice];
        dice = new Die[numberOfDice];
        for (int i = 0; i < numberOfDice; i++) {
            dice[i] = new Die(numberOfSides);
            faceValues[i] =0;
        }
    }

    //Rolls all the dice in the array
    public void rollDice() {
        int sum = 0;
        for (int i = 0; i < numberOfDice; i++) {
            dice[i].roll();
            faceValues[i] = dice[i].getFaceValue();
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
//        return 4;//Todo
    }

    public int[] getFaceValues() {
        return faceValues;
    }

}
