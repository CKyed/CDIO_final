package model.Fields;
import static Utilities.FileReader.*;

public class OrdinaryTax extends Field{
    private int tax;

    public OrdinaryTax(String state) {
        super(state);
        this.tax = getFieldData(state,"price");
        group = 11;
        type = "ordinaryTax";

    }

    public int getTax() {
        return tax;
    }

}
