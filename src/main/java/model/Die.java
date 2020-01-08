package model;

import java.util.Random;

public class Die {

    private int faceValue;
    private int sideNumber;

    public Die(int numberOfSides) {
        sideNumber = numberOfSides;
    }

    public void roll(){
        Random Ran = new Random();
        faceValue = Ran.nextInt(sideNumber) + 1;
    }

    public int getFaceValue(){
        return faceValue;
    }

    public void setFaceValue(int y){
        faceValue = y;
    }
}
