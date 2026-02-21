package model;

public class InternationalTransferRequest extends PaymentRequest {
    private String beneficiaryBank;
    private String beneficiaryAccount;
    private String swiftCode;
    private String currency;

    public InternationalTransferRequest(String upiId, String toAccount, double amount, 
                                     String pin, String beneficiaryBank, 
                                     String beneficiaryAccount, String swiftCode, 
                                     String currency, String remarks) {
        super(upiId, toAccount, amount, remarks, pin);
        this.beneficiaryBank = beneficiaryBank;
        this.beneficiaryAccount = beneficiaryAccount;
        this.swiftCode = swiftCode;
        this.currency = currency;
    }

    public String getBeneficiaryBank() {
        return beneficiaryBank;
    }

    public String getBeneficiaryAccount() {
        return beneficiaryAccount;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return super.toString() + 
               String.format("\nBeneficiary Bank: %s\nAccount: %s\nSWIFT: %s\nCurrency: %s",
               beneficiaryBank, beneficiaryAccount, swiftCode, currency);
    }
}
