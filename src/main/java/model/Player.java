package model;

public class Player {
    private String name;
    private Account account;
    private boolean inJail;
    private int positionOnBoard =0;
    private boolean prisonCard = false;
    private boolean hasPlayerLost = false;

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
        return this.inJail;
    }

    public String getName() {
        return name;
    }

    public void setInJail(boolean inJail) {
        this.inJail = inJail;
    }

    public boolean isPrisonCard() {
        return prisonCard;
    }

    public void setPrisonCard(boolean prisonCard) {
        this.prisonCard = prisonCard;
    }

    public boolean isHasPlayerLost() {
        return hasPlayerLost;
    }

    public void setHasPlayerLost(boolean hasPlayerLost) {
        this.hasPlayerLost = hasPlayerLost;
    }

    public int getOwesAmount() {
        return this.account.getOwesAmount();
    }

    public void setOwesAmount(int owesAmount) {
        this.getAccount().setOwesAmount(owesAmount);
    }
}

