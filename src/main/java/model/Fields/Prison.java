package model.Fields;

import static controller.TextController.getFieldData;

public class Prison  extends Field{
    private String type = "prison";
    private int group = 9;
    private int bail;
    private int turnsInJail = 0;

    public Prison(String state) {
        super(state);
        this.bail = getFieldData(state,"rent");
    }

   //method for counting turns in Jail if needed

    @Override
    public int getGroup() {
        return this.group;
    }

    @Override
    public String getType() {
        return this.type;
    }

    public int getBail() {
        return bail;
    }

    public int getTurnsInJail() {
        return turnsInJail;
    }


}
