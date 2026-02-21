package services;

import interfaces.PaymentService;
import interfaces.NotificationService;
import model.*;
import exceptions.*;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class UPIPaymentServiceImpl implements PaymentService {
    private UPIRegistry upiRegistry;
    private NotificationService notificationService;
    private FileTransactionService fileService;

    public UPIPaymentServiceImpl(NotificationService notificationService, FileTransactionService fileService) {
        this.upiRegistry = new UPIRegistry();
        this.notificationService = notificationService;
        this.fileService = fileService;
    }

    public void registerUPIAccount(UPIAccount upiAccount) throws UPIException {
        if (upiRegistry.contains(upiAccount.getUpiId())) {
            throw new UPIException("UPI ID already exists");
        }
        upiRegistry.register(upiAccount);
    }

    @Override
    public Transaction makePayment(PaymentRequest request) throws UPIException {
        UPIAccount senderAccount = upiRegistry.getAccount(request.getFromUpiId());
        if (senderAccount == null) {
            throw new UPIException("Sender UPI account not found");
        }

        UPIAccount receiverAccount = upiRegistry.getAccount(request.getToUpiId());
        if (receiverAccount == null) {
            throw new UPIException("Receiver UPI account not found");
        }

        if (!senderAccount.validatePin(request.getUpiPin())) {
            throw new InvalidUPIPinException("Invalid UPI PIN");
        }

        BankAccount senderBankAccount = senderAccount.getLinkedBankAccount();
        if (senderBankAccount.getBalance() < request.getAmount()) {
            throw new InsufficientBalanceException("Insufficient balance in account");
        }

        String transactionId = "TXN" + System.currentTimeMillis() + new Random().nextInt(1000);
        Transaction transaction = new Transaction(transactionId, request.getFromUpiId(), 
                request.getToUpiId(), request.getAmount(), request.getRemarks());

        try {
            BankAccount receiverBankAccount = receiverAccount.getLinkedBankAccount();
            
            senderBankAccount.setBalance(senderBankAccount.getBalance() - request.getAmount());
            receiverBankAccount.setBalance(receiverBankAccount.getBalance() + request.getAmount());
            
            transaction.setStatus("COMPLETED");
            fileService.saveTransaction(transaction);
            
            notificationService.sendNotification(senderAccount.getLinkedBankAccount().getUser(), 
                    String.format("Debit of ₹%.2f to %s. Txn ID: %s", 
                            request.getAmount(), request.getToUpiId(), transactionId));
            
            notificationService.sendNotification(receiverAccount.getLinkedBankAccount().getUser(), 
                    String.format("Credit of ₹%.2f from %s. Txn ID: %s", 
                            request.getAmount(), request.getFromUpiId(), transactionId));
            
            return transaction;
        } catch (Exception e) {
            transaction.setStatus("FAILED");
            throw new UPIException("Payment failed: " + e.getMessage());
        }
    }

    @Override
    public void requestMoney(PaymentRequest request) throws UPIException {
        UPIAccount receiverAccount = upiRegistry.getAccount(request.getToUpiId());
        if (receiverAccount == null) {
            throw new UPIException("Receiver UPI account not found");
        }

        if (!receiverAccount.validatePin(request.getUpiPin())) {
            throw new InvalidUPIPinException("Invalid UPI PIN");
        }

        notificationService.sendNotification(receiverAccount.getLinkedBankAccount().getUser(), 
                String.format("Money request of ₹%.2f from %s", 
                        request.getAmount(), request.getFromUpiId()));
    }

    @Override
    public double checkBalance(String upiId) throws UPIException {
        UPIAccount account = upiRegistry.getAccount(upiId);
        if (account == null) {
            throw new UPIException("UPI account not found");
        }
        return account.getLinkedBankAccount().getBalance();
    }

    @Override
    public Transaction payElectricityBill(ElectricityBillRequest request) throws UPIException {
        return processBillPayment(request, "Electricity Bill - " + request.getElectricityBoard());
    }

    @Override
    public Transaction rechargeMobile(MobileRechargeRequest request) throws UPIException {
        return processBillPayment(request, "Mobile Recharge - " + request.getOperator());
    }

    @Override
    public Transaction rechargeFastag(FastagRechargeRequest request) throws UPIException {
        return processBillPayment(request, "Fastag Recharge - " + request.getFastagProvider());
    }

    private Transaction processBillPayment(PaymentRequest request, String description) throws UPIException {
        UPIAccount account = upiRegistry.getAccount(request.getFromUpiId());
        if (account == null) {
            throw new UPIException("UPI account not found");
        }

        if (!account.validatePin(request.getUpiPin())) {
            throw new InvalidUPIPinException("Invalid UPI PIN");
        }

        BankAccount bankAccount = account.getLinkedBankAccount();
        if (bankAccount.getBalance() < request.getAmount()) {
            throw new InsufficientBalanceException("Insufficient balance in account");
        }

        String txnId = "TXN" + System.currentTimeMillis() + new Random().nextInt(1000);
        Transaction txn = new Transaction(txnId, request.getFromUpiId(), 
                request.getToUpiId(), request.getAmount(), description);

        try {
            bankAccount.setBalance(bankAccount.getBalance() - request.getAmount());
            txn.setStatus("COMPLETED");
            fileService.saveTransaction(txn);
            
            notificationService.sendNotification(bankAccount.getUser(),
                String.format("Bill payment of ₹%.2f for %s. Txn ID: %s",
                    request.getAmount(), description, txnId));
            
            return txn;
        } catch (Exception e) {
            txn.setStatus("FAILED");
            throw new UPIException("Payment failed: " + e.getMessage());
        }
    }

    @Override
    public Transaction internationalTransfer(InternationalTransferRequest request) throws UPIException {
        UPIAccount senderAccount = upiRegistry.getAccount(request.getFromUpiId());
        if (senderAccount == null) {
            throw new UPIException("Sender UPI account not found");
        }

        if (!senderAccount.validatePin(request.getUpiPin())) {
            throw new InvalidUPIPinException("Invalid UPI PIN");
        }

        BankAccount senderBankAccount = senderAccount.getLinkedBankAccount();
        if (senderBankAccount.getBalance() < request.getAmount()) {
            throw new InsufficientBalanceException("Insufficient balance in account");
        }

        double forexFee = request.getAmount() * 0.02;
        double totalDebit = request.getAmount() + forexFee;

        String txnId = "INT" + System.currentTimeMillis() + new Random().nextInt(1000);
        Transaction txn = new Transaction(txnId, request.getFromUpiId(), 
                request.getToUpiId(), request.getAmount(), 
                "International Transfer: " + request.getRemarks());

        try {
            senderBankAccount.setBalance(senderBankAccount.getBalance() - totalDebit);
            txn.setStatus("PROCESSING");
            fileService.saveTransaction(txn);
            
            notificationService.sendNotification(senderBankAccount.getUser(),
                String.format("International transfer of %s%.2f to %s. Txn ID: %s\n" +
                             "Forex Fee: %s%.2f",
                    request.getCurrency(), request.getAmount(), 
                    request.getBeneficiaryBank(), txnId,
                    request.getCurrency(), forexFee));
            
            return txn;
        } catch (Exception e) {
            txn.setStatus("FAILED");
            throw new UPIException("Transfer failed: " + e.getMessage());
        }
    }

    @Override
    public List<Transaction> getTransactionHistory(String upiId) throws UPIException {
        if (!upiRegistry.contains(upiId)) {
            throw new UPIException("UPI account not found");
        }
        
        try {
            return fileService.getTransactionsForUpiId(upiId);
        } catch (IOException e) {
            throw new UPIException("Could not retrieve transaction history: " + e.getMessage());
        }
    }

    public int getRegisteredAccountsCount() {
        return upiRegistry.size();
    }
    
}
