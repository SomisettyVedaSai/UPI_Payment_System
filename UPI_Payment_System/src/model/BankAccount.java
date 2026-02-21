package model;

public class BankAccount extends Account {
    private String bankName;
    private String ifscCode;

    public BankAccount(String accountNumber, double balance, User user, String bankName, String ifscCode) {
        super(accountNumber, balance, user);
        this.bankName = bankName;
        this.ifscCode = ifscCode;
    }

    public String getAccountDetails() {
        return String.format("Bank Account: %s, Bank: %s, IFSC: %s, Balance: %.2f",
                getAccountNumber(), bankName, ifscCode, getBalance());
    }

    public String getBankName() {
        return bankName;
    }

    public String getIfscCode() {
        return ifscCode;
    }
}
