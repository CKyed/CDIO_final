package model;

public class DiceCup {
    private int numberOfDice;
    private int dieSum;
    private Die[] dice;

    /**
     *Creates an array with a number of dice, they all have same number of sides, defined by the two variables
     * @see Die
     * @param numberOfDice
     * @param numberOfSides
     */



    public DiceCup(int numberOfDice, int numberOfSides) {
        this.numberOfDice = numberOfDice;
        dice = new Die[numberOfDice];
        for (int i = 0; i < numberOfDice; i++) {
            dice[i] = new Die(numberOfSides);
        }
    }



    //Rolls all the dice in the array
    public void rollDice() {
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
        this.dieSum = 0;
        for(int i =0; i<numberOfDice; i++){
          this.dieSum += this.dice[i].getFaceValue();
        }
        return this.dieSum;
    }

}
