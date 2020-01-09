package model.Fields;

import model.Account;
import model.Player;

public class Taxes extends Field{
    private int tax;

    public Taxes(String state){
        super(state);
    }


    public void tax(Player player){
        player.getAccount().withdraw( 2000 );
    }

    public void taxWithPercent(Player player, int taxOrPercent){
            if (taxOrPercent == 0){
                player.getAccount().withdraw( 4000 );
            }else {
                int tenOfPlayerAccount = player.getAccount().getBalance() *(10/100);
                // TODO mangle Ownable. Skal redigeres når gamecontroller til at lave.
             }
    }
}
