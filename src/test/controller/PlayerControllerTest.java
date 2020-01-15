package controller;
import java.math.*;
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
    void safeTransferToBank(){
        //Test if the method withdraws for the player and set the account balance and owes to the correct number


    }

    @Test
    void safeTransferToPlayer() {
    }

    @Test
    void tryToPayDebt() {
    }
}