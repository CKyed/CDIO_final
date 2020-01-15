package model.Fields.OwnableFile;

import model.Fields.Ownable;

public class Ferry extends Ownable {
    private String type = "ferry";
    private int group = 13;
    private int[] rentLevels = new int[5];

    public Ferry(String state) {
        super(state);
        rentLevels[0] = this.getRent();
        rentLevels[1] = this.getRent();
        rentLevels[2] = this.getRent()*2;
        rentLevels[3] = this.getRent()*4;
        rentLevels[4] = this.getRent()*8;
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
