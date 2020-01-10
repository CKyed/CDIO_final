package model.Fields;

public class Chance  extends Field {
    private int group = 12;
    private String type = "chance";
    public Chance(String state) {
        super(state);
    }

    //on a chancefield, a card displayed and one of the following consequenses are possible
    //Pay money
    //Get money
    //Money between players
    //Move player
    //Go to prison
    //Receive prison-card
    //Prices raised








    @Override
    public int getGroup() {
        return this.group;
    }

    @Override
    public String getType() {
        return this.type;
    }
}
