package model.Fields.OwnableFile;

import model.Fields.Ownable;

import static controller.TextController.*;

public class Street extends Ownable {
    private int serie;
    private int housePrice;
    private int houseLevel;
    private int[] rentLevels;


    public Street(String state) {
        super(state);
        this.serie = getFieldData(state,"serie");
        this.housePrice = getFieldData(state,"housePrice");
        this.houseLevel = 5;
        this.rentLevels = new int[houseLevel];
        for (int i = 1; i <rentLevels.length ; i++) {
            rentLevels[i] = getFieldData(state,"house"+i);
        }
    }

    @Override
    public void setRent() {
    }
}
