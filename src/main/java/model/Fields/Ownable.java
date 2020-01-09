package model.Fields;

import model.Player;

import static controller.PathExpert.fieldAttributesPath;
import static controller.TextController.readFile;

public abstract class Ownable extends Field {
    protected int price;
    protected boolean isPledged;
    protected Player owner;

    public Ownable(String state) {
        super(state);





    }
}

