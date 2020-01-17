package controller;
import java.math.*;

import model.Board;
import model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerControllerTest {

    @Test
    void addMoneyToPlayer() {
        //Test if the method add's the correct amount to the players account balance
        String[] tT = {"TorbenTest"};
        PlayerController pc = new PlayerController(tT);
        for (int i = 0; i < 1000 ; i++) {
            int amount=(int)(Math.random()*100);
            int accountBalanceBefore = pc.getPlayers()[0].getAccountBalance();
            pc.addMoneyToPlayer(0,amount);
            assertTrue(accountBalanceBefore+amount==pc.getPlayers()[0].getAccountBalance());
        }
    }

    @Test
    void takeMoneyFromPlayer(){
        //Test if the method withdraws the correct amount from the players account balance
        String[] tT = {"TorbenTest"};
        PlayerController pc = new PlayerController(tT);
        for (int i = 0; i < 1000 ; i++) {
            int amount=(int)(Math.random()*100);
            int accountBalanceBefore = pc.getPlayers()[0].getAccountBalance();
            pc.takeMoneyFromPlayer(0,amount);
            assertTrue(accountBalanceBefore-amount==pc.getPlayers()[0].getAccountBalance());
        }
    }
    @Test
    void safePaymentToBank(){
        //Test if the safePaymentToBank method withdraw the correct amount from the player, but never set its to less than 0,
        // and at the same set the OwesAmount to the remaining. One thing to note is that the game handles if a player suddenly owes anything,
        // and the player can't continue to play before his OwesAmount==0
        String[] tT = {"TorbenTest"};
        PlayerController pc = new PlayerController(tT);

        for (int i = 0; i <1000 ; i++) {
            int amount=(int)(Math.random()*1000);
            int accountBalanceBefore = pc.getPlayers()[0].getAccountBalance();
            pc.safeTransferToBank(0,amount);
            if(accountBalanceBefore-amount>=0){
                assertTrue(pc.getPlayers()[0].getAccountBalance()==accountBalanceBefore-amount);
            }
            if(accountBalanceBefore-amount<0){
                assertTrue(pc.getPlayers()[0].getAccountBalance()==0 && pc.getPlayers()[0].getOwesAmount()==amount-accountBalanceBefore);
            }
        }
    }

   @Test
    void safePaymentToPlayer(){
        String[] tT = {"TorbenTest","TinaTest"};
        PlayerController pc = new PlayerController(tT);

        for (int i = 0; i <10000; i++) {
            int fromPlayerId = ((int)(Math.random()*2));
            int toPlayerId = 0;
            if(fromPlayerId==0)
                toPlayerId =1;
            int amount=(int)(Math.random()*30000);
            int balanceBeforeFromPlayer = pc.getPlayers()[fromPlayerId].getAccountBalance();
            int balanceBeforeToPlayer = pc.getPlayers()[toPlayerId].getAccountBalance();
            pc.safeTransferToPlayer(fromPlayerId,amount,toPlayerId);

            if(balanceBeforeFromPlayer-amount>=0) {
                assertTrue(pc.getPlayers()[fromPlayerId].getAccountBalance() == balanceBeforeFromPlayer - amount);
                assertTrue(pc.getPlayers()[toPlayerId].getAccountBalance() == balanceBeforeToPlayer + amount);
            }
            if(balanceBeforeFromPlayer-amount<0){
                assertTrue(pc.getPlayers()[fromPlayerId].getAccountBalance()==0);
                assertTrue(pc.getPlayers()[fromPlayerId].getOwesAmount()==amount-balanceBeforeFromPlayer);
                assertTrue(pc.getPlayers()[toPlayerId].getAccountBalance() == balanceBeforeToPlayer+(balanceBeforeFromPlayer));
            }
        }

    }

}