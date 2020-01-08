package model;

public class Account {

    int balance;

    public int withdraw(int amount){
        return balance-amount;
    }

    public void deposit(int amount){
        balance+=amount;
    }

}
