package model.Fields;

public class Chance  extends Field {
    private int group = 12;
    private String type = "chance";
    public Chance(String state) {
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
