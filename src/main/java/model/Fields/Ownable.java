package model.Fields;

import model.Player;

import static controller.PathExpert.fieldAttributesPath;
import static controller.TextController.readFile;

public abstract class Ownable extends Field {
    private int price;
    private boolean isPledged;
    private Player owner;

    public Ownable(String state) {
        super(state);





    }
}

