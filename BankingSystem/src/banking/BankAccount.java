package banking; 
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class BankAccount {

    private String accountNumber;
    private String accountHolderName;
    private double balance;

   
    public BankAccount(String accountNumber, String accountHolderName, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("The opening balance cannot be negative.");
        }
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
    }

   
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    
    protected void setBalance(double balance) {
        this.balance = balance;
    }

    
    public abstract void deposit(double amount) throws Exception;
    public abstract void withdraw(double amount) throws Exception;
    public abstract void calculateInterest() throws Exception;
    

    protected void logTransaction(String type, double amount, String status) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = LocalDateTime.now().format(dtf);
        String record = String.format("[%s] Acc: %s | %s: M%.2f | Bal: M%.2f | %s\n",
                timestamp, accountNumber, type, amount, balance, status);

        try (FileWriter writer = new FileWriter("transactions.txt", true)) {
            writer.write(record);
        } catch (IOException e) {
            System.err.println("Audit trail file system error: " + e.getMessage());
        }
    }

    
    public void displayAccountInfo() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder: " + accountHolderName);
        System.out.printf("Current Balance: M%.2f\n", balance);
    }
}

