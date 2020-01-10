package model.Fields.OwnableFile;

import model.Fields.Ownable;

import static controller.TextController.*;

public class Street extends Ownable {
    private String type = "street";
    private int group;
    private int housePrice;
    private int houseLevel;
    private int[] rentLevels;


    public Street(String state) {
        super(state);
        this.group = getFieldData(state,"series");
        this.housePrice = getFieldData(state,"housePrice");
        this.houseLevel = 0;
        this.rentLevels = new int[6];
        rentLevels[0]=getFieldData(state,"rent");
        for (int i = 1; i <rentLevels.length ; i++) {
            rentLevels[i] = getFieldData(state,"house"+i);
        }
        this.rent = rentLevels[0];
    }

    public int getHousePrice() {
        return housePrice;
    }

    public int getHouseLevel() {
        return houseLevel;
    }

    public int[] getRentLevels() {
        return rentLevels;
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
