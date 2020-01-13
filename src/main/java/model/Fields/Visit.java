package model.Fields;

public class Visit  extends Field {
    private String type = "visit";
    private int group = 9;
    public Visit(String state) {
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
