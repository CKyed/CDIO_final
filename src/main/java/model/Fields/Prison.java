package model.Fields;

public class Prison  extends Field{
    private String type = "prison";
    private int group = 9;
    public Prison(String state) {
        super(state);
    }
    private int bail = 1000;
    //TODO I have hardoded the bail, later this should be implemented from textdocument
    private int turnsInJail = 0;

   //method for counting turns in Jail

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
