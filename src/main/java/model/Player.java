package model;

public class Player {
    private String name;
    private Account account;
    private boolean inJail;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
