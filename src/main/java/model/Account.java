package model;

public class Account {
    private int balance;


    public Account(){
        this.balance = 30000;
    }

    public void withdraw(int amount){
        this.balance-= amount;
    }

    public void deposit(int amount){
        this.balance+= amount;
    }


}
