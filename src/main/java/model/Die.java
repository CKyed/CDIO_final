package model;

import java.util.Random;


public class Die {

    private int faceValue = 1;
    private int sideNumber = 6;


    public Die(int i) {
        sideNumber = i;
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
