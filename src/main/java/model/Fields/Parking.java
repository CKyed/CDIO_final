package model.Fields;

public class Parking  extends Field {
    private int group = 10;
    private String type = "parking";
    public Parking(String state) {
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
