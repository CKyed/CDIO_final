package model.Fields.OwnableFile;

import model.Fields.Ownable;

public class Brewery extends Ownable {
    private int[] rentLevels;
    public Brewery(String state) {
        super(state);
        rentLevels = new int[3];
        rentLevels[0] = this.rent;
        rentLevels[1] = this.rent;
        rentLevels[2] = 2*this.rent;
        this.group =14;
        this.type = "brew";
    }

    public int[] getRentLevels() {
        return rentLevels;
    }

}
