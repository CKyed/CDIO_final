package model.Fields;

import static Utilities.FileReader.getFieldData;

public class Prison  extends Field{
    private int bail;

    public Prison(String state) {
        super(state);
        this.bail = getFieldData(state,"rent");
        type = "prison";
        group = 9;
    }

    public int getBail() {
        return bail;
    }

}
