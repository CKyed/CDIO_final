package model.Fields;

public class IncomeTax extends Field{
    private int group = 11;
    private String type = "tax";
    public IncomeTax(String state) {
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
