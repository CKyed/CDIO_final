import java.util.Scanner;
import controller.PathExpert.*;
import model.*;
import model.Fields.*;
import model.Fields.OwnableFile.Ferry;


import static java.lang.System.out;
import static controller.PathExpert.*;
import static controller.TextController.readFile;

public class Main {
    public static void main(String[] args) {
        out.println("Hello world");

        //MARTIN TESTER TEXTCONTROLLER
        String hej = readFile(namePath,"chance");
        out.println(hej);

        //Tester noget mere
        Field ferry = new Ferry("ferry01");
    }
}
