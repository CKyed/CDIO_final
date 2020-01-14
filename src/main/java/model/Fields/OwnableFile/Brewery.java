package model.Fields.OwnableFile;

import model.Fields.Ownable;

import static controller.TextController.*;

public class Brewery extends Ownable {
    private String type = "brew";
    private int group = 14;
    private int[] rentLevels;
    public Brewery(String state) {
        super(state);
        rentLevels = new int[2];
        rentLevels[0] = this.rent;
        rentLevels[1] = 2*this.rent;
    }


    @Override
    public int getGroup() {
        return this.group;
    }

    @Override
    public String getType() {
        return this.type;
    }


    public int[] getRentLevels() {
        return rentLevels;
    }

}
