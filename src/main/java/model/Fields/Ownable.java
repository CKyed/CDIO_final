package model.Fields;

import model.Player;

import static controller.PathExpert.fieldAttributesPath;
import static controller.TextController.getFieldData;
import static controller.TextController.readFile;

public abstract class Ownable extends Field {
    protected int price;
    protected int rent;
    protected boolean isPledged;
    protected int ownerId = -1;

    public Ownable(String state) {
        super(state);
        this.price = getFieldData(state,"price");
        this.rent = getFieldData(state,"rent");
    }
    public abstract void setRent();


    public int getOwnerId() {
        return ownerId;
    }

    public int getPrice() {
        return price;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getRent() {
        return rent;
    }

    public boolean isPledged() {
        return isPledged;
    }
}