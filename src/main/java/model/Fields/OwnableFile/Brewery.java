package model.Fields.OwnableFile;

import model.Fields.Ownable;

import static controller.TextController.*;

public class Brewery extends Ownable {
    private String type = "brew";
    private int group = 14;
    public Brewery(String state) {
        super(state);
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
