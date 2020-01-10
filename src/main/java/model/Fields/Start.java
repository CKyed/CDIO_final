package model.Fields;

public class Start  extends Field {
    private String type = "start";
    private int group = 8;
    public Start(String state) {
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
