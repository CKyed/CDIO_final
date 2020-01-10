package model.Fields;
import static controller.TextController.*;

public class OrdinaryTax extends Field{
    private int group = 11;
    private String type = "ordinaryTax";
    private int tax;
    //TODO Her har vi hardcoded beløbet, så senere kan de importeres fra textfil


    public OrdinaryTax(String state) {
        super(state);
        this.tax = getFieldData(state,"rent");

    }

    public int getTax() {
        return tax;
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
