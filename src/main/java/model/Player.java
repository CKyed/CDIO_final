package model;

public class Player {
    private String name;
    private Account account;
    private boolean inJail;


    public Player(String name, int startKapital){
        this.account.setBalance(startKapital);
        this.name = name;
    }
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void deposit(int amount){
        this.account.deposit(amount);
    }

    public void withdraw(int amount){
        this.account.withdraw(amount);
    }

    public int getAccountBalance(){
        return this.account.getBalance();
    }
}

