package model.Fields;

public class Prison  extends Field{
    private String type = "prison";
    private int group = 9;
    public Prison(String state) {
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
