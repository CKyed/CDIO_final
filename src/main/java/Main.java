import controller.SystemController;
import model.DiceCup;
import model.Die;
import java.util.Scanner;
import controller.PathExpert.*;
import model.*;
import model.Fields.*;
//import model.Fields.OwnableFile.Ferry;


import static java.lang.System.out;
import static controller.PathExpert.*;
import static controller.TextController.readFile;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world");


        SystemController systemController = new SystemController();



        //tester terning
        Die terning = new Die(6);
        terning.roll();
        System.out.println("én terning har slået: "+terning.getFaceValue());

        //tester raflebæger
        DiceCup raflebaeger = new DiceCup(10,6);
        raflebaeger.rollDice();
        System.out.println(raflebaeger.getNumberOfDice() +" terninger har slået: "+raflebaeger.getDieSum());

        //MARTIN TESTER TEXTCONTROLLER
        String hej = readFile(namePath,"chance");
        out.println(hej);

        //Tester noget mere
       // Field ferry = new Ferry("ferry01");

    }
}

