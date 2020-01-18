package model.Fields;

import static Utilities.FileReader.getFieldData;

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
    public void setRent(int rent){
        this.rent = rent;
    };


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
        return this.rent;
    }

    public boolean isPledged() {
        return isPledged;
    }

    public void setPledged(boolean pledged){
        this.isPledged = pledged;
    }
}