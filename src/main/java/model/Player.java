package model;

public class Player {
    /**
     * Player has a name, account and inJail(boolean) as attributes
     */
    private String name;
    private Account account;
    private boolean inJail;
    private int currentFieldId=0;

    public Player(String name, int startKapital){
        this.account = new Account(startKapital);
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

    public void setCurrentFieldId(int currentFieldId) {
        this.currentFieldId = currentFieldId;
    }

    public int getCurrentFieldId() {
        return currentFieldId;
    }

}

