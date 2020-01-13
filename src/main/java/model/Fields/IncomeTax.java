package model.Fields;

import static controller.TextController.getFieldData;

public class IncomeTax extends Field{
    private int group = 11;
    private String type = "incomeTax";
    private int incomeTax;
    private int percentTax;
    public IncomeTax(String state) {
        super(state);
        //the names of these attributes don't match their real value
        this.incomeTax = getFieldData(state,"price");
        this.percentTax = getFieldData(state,"houseprice");
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


