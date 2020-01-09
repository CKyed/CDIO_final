package model.Fields;

public class IncomeTax extends Field{
    private int group = 11;
    private String type = "tax";
    private int incomeTax = 4000;
    public IncomeTax(String state) {
        super(state);
    }

    public int getIncomeTax() {
        return incomeTax;
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


