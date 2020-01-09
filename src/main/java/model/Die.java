package model;
public class Die {

    private int minValue = 1;
    private int faceValue;
    private int maxValue = 6;

    //Roll dice and get random value
    public int roll(){
        faceValue = minValue + (int) (Math.random()*maxValue);
        return faceValue;
    }
}