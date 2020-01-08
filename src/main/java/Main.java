import java.util.Scanner;
import controller.PathExpert.*;

import static java.lang.System.out;
import static controller.PathExpert.*;
import static controller.TextController.readFile;

public class Main {
    public static void main(String[] args) {
        out.println("Hello world");

        //MARTIN TESTER TEXTCONTROLLER
        String hej = readFile(namePath,"chance");
        out.println(hej);
    }
}
