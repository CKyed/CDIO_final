package model;

public class Account {
    private int balance;
    private int owesAmount = 0;
    private int creditorId;

    public Account(int balance){
        this.balance=balance;
    }

    public void withdraw(int amount){
        this.balance-=amount;
    }

    public void deposit(int amount){
        balance+=amount;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getOwesAmount() {
        return owesAmount;
    }

    public void setOwesAmount(int owesAmount){
        this.owesAmount = owesAmount;
    }

    public int getCreditorId() {
        return creditorId;
    }

    public void setCreditorId(int creditorId) {
        this.creditorId = creditorId;
    }
}
