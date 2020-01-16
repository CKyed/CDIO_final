package model.Fields;

import static Utilities.FileReader.getFieldData;

public class IncomeTax extends Field{
    private int incomeTax;
    private int percentTax;
    public IncomeTax(String state) {
        super(state);
        //the names of these attributes don't match their real value
        this.incomeTax = getFieldData(state,"price");
        this.percentTax = getFieldData(state,"rent");
        group = 11;
        type = "incomeTax";
    }

    public int getIncomeTax() {
        return incomeTax;
    }


}


