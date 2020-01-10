package model;

public class Player {
    private String name;
    private Account account;
    private boolean inJail;
    private int positionOnBoard =0;

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

    public void setPositionOnBoard(int positionOnBoard) {
        this.positionOnBoard = positionOnBoard;
    }

    public int getPositionOnBoard() {
        return positionOnBoard;
    }

    public boolean isInJail() {
        return inJail;
    }

    public String getName() {
        return name;
    }
}

