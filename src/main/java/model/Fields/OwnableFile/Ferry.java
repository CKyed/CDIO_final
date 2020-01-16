package model.Fields.OwnableFile;

import model.Fields.Ownable;

public class Ferry extends Ownable {
       private int[] rentLevels = new int[5];

    public Ferry(String state) {
        super(state);
        rentLevels[0] = this.getRent();
        rentLevels[1] = this.getRent();
        rentLevels[2] = this.getRent()*2;
        rentLevels[3] = this.getRent()*4;
        rentLevels[4] = this.getRent()*8;
        type = "ferry";
        group = 13;
    }

    public int[] getRentLevels() {
        return rentLevels;
    }

}
