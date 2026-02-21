package services;

import model.Transaction;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileTransactionService {
    private static final String TRANSACTION_FILE = "transactions.txt";

    public void saveTransaction(Transaction transaction) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(TRANSACTION_FILE, true))) {
            out.println(transaction.toString());
            out.println("----------");
        }
    }

    public List<Transaction> getTransactionsForUpiId(String upiId) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
            String line;
            StringBuilder txnData = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.equals("----------")) {
                    String txnString = txnData.toString();
                    if (txnString.contains("From: " + upiId) || txnString.contains("To: " + upiId)) {
                        transactions.add(parseTransaction(txnString));
                    }
                    txnData = new StringBuilder();
                } else {
                    txnData.append(line).append("\n");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No transactions found yet.");
        }
        return transactions;
    }

    private Transaction parseTransaction(String txnString) {
        String[] lines = txnString.split("\n");
        String txnId = lines[0].split(": ")[1];
        String from = lines[1].split(": ")[1];
        String to = lines[2].split(": ")[1];
        double amount = Double.parseDouble(lines[3].split(": ")[1]);
        String remarks = lines[6].split(": ")[1];
        
        Transaction txn = new Transaction(txnId, from, to, amount, remarks);
        
        String status = lines[5].split(": ")[1];
        txn.setStatus(status);
        
        return txn;
    }
}
