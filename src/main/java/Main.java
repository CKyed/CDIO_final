import model.DiceCup;
import model.Die;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world");

        //tester terning
        Die terning = new Die(6);
        terning.roll();
        System.out.println("én terning har slået: "+terning.getFaceValue());

        //tester raflebæger
        DiceCup raflebæger = new DiceCup(10,6);
        raflebæger.rollDice();
        System.out.println(raflebæger.getNumberOfDice() +" terninger har slået: "+raflebæger.getDieSum());

    }
}

