package model;

public class Player {
    private String name;
    private Account account;
    private boolean inJail;
    private int currentFieldId=0;
// TODO declare variable to inform about the players debt


    public Player(String name, int startKapital){
        this.account = new Account(startKapital);
        this.name = name;
        this.inJail = false;
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

    public void setCurrentFieldId(int currentFieldId) {
        this.currentFieldId = currentFieldId;
    }

    public int getCurrentFieldId() {
        return currentFieldId;
    }

    public boolean isInJail() {
        return inJail;
    }

    public void setInJail(boolean inJail) {
        this.inJail = inJail;
    }
}

