package model.Fields.OwnableFile;

import model.Fields.Ownable;

public class Ferry extends Ownable {
    private String type = "ferry";
    private int group = 13;
    private int rentLevel;

    public Ferry(String state) {
        super(state);
    }

    @Override
    public void setRent() {

    }

    @Override
    public int getGroup() {
        return this.group;
    }

    @Override
    public String getType() {
        return this.type;
    }
}
