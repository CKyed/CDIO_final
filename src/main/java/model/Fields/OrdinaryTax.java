package model.Fields;

public class OrdinaryTax extends Field{
    private int group = 11;
    private String type = "tax";
    public OrdinaryTax(String state) {
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
