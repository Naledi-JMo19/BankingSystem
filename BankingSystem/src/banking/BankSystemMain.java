
package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class BankSystemMain 
{

    private static final List<BankAccount> accounts = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    //Main class
    public static void main(String[] args) 
    {

        //Instances
        accounts.add(new SavingsAccount("90101", "Itumeleng Mohai", 500));
        accounts.add(new CurrentAccount("90102", "Malefetsane Machesetsa", 1000));
        accounts.add(new CurrentAccount("90103", "Naledi Motoko", 2000));
        accounts.add(new CurrentAccount("90104", "Lebajoa Pitso", 1500));
        accounts.add(new CurrentAccount("90105", "Lieketseng Leemisa", 1700));        

        boolean running = true;

        while (running) 
            {
            printMenu();

            try 
            {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) 
                {
                    case 1 -> displayAllAccounts();
                    case 2 -> depositMoney();
                    case 3 -> withdrawMoney();
                    case 4 -> transferMoney();
                    case 5 -> calculateInterest();
                    case 6 -> requestLoan();
                    case 7 -> displayAccountDetails();
                    case 8 -> running = false;
                    default -> System.out.println(
                            "Invalid choice. Please choose 1 to 8.");
                }

            } catch (NumberFormatException e) 
            {
                System.out.println("Invalid input. Please enter a number.");
            }

            if (running) 
            {
                System.out.println();
            }
        }

        System.out.println("Exiting safely. Goodbye!");
        scanner.close();
    }

    // Display the main menu

    private static void printMenu() {
        System.out.println("\n--- Banking Menu ---");
        System.out.println("1. Display All Accounts");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Transfer Money");
        System.out.println("5. Calculate Interest");
        System.out.println("6. Request Bank Loan");
        System.out.println("7. Display Account Details");
        System.out.println("8. Exit");
        System.out.print("Choose An Option: ");
    }

    // Find an account using its account number
    private static BankAccount findAccountByNumber(String accNum) {
        for (BankAccount acc : accounts) {
            if (acc.getAccountNumber().equalsIgnoreCase(accNum.trim())) {
                return acc;
            }
        }
        return null;
    }

    // Read and validate a monetary amount
    private static double readAmount(String message) {
        System.out.print(message);

        double amount = Double.parseDouble(scanner.nextLine().trim());

        if (!Double.isFinite(amount)) {
            throw new IllegalArgumentException(
                    "Please enter a valid monetary amount.");
        }

        return amount;
    }

    // Ask for an account and return it if found
    private static BankAccount askForAccount() {
        System.out.print("Enter account number: ");
        String accNum = scanner.nextLine().trim();

        BankAccount acc = findAccountByNumber(accNum);

        if (acc == null) {
            System.out.println("Account not found.");
        }

        return acc;
    }

    // Display all bank accounts
    private static void displayAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts are available.");
            return;
        }

        System.out.println("\n--- All Bank Accounts ---");

        for (BankAccount acc : accounts) {
            acc.displayAccountInfo();
            System.out.println("--------------------");
        }
    }

    // Deposit money into an account
    private static void depositMoney() {
        BankAccount acc = askForAccount();

        if (acc == null) {
            return;
        }

        try {
            double amount = readAmount("Enter deposit amount: M");
            acc.deposit(amount);
            System.out.println("Deposit completed successfully.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");

        } catch (Exception e) {
            System.out.println("Deposit failed: " + e.getMessage());
        }
    }

    // Withdraw money from an account
    private static void withdrawMoney() {
        BankAccount acc = askForAccount();

        if (acc == null) {
            return;
        }

        try {
            double amount = readAmount("Enter withdrawal amount: M");
            acc.withdraw(amount);
            System.out.println("Withdrawal completed successfully.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");

        } catch (Exception e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        }
    }

    // Transfer money between two accounts
    private static void transferMoney() {
        System.out.println("\n--- Transfer Money ---");

        System.out.print("Enter sender account number: ");
        String senderNumber = scanner.nextLine().trim();

        BankAccount sender = findAccountByNumber(senderNumber);

        if (sender == null) {
            System.out.println("Sender account not found.");
            return;
        }

        System.out.print("Enter recipient account number: ");
        String recipientNumber = scanner.nextLine().trim();

        BankAccount recipient = findAccountByNumber(recipientNumber);

        if (recipient == null) {
            System.out.println("Recipient account not found.");
            return;
        }

        if (sender == recipient) {
            System.out.println(
                    "Transfer failed: You cannot transfer to the same account.");
            sender.logTransaction(
                    "Transfer", 0, "FAILED: Same sender and recipient account.");
            return;
        }

        try {
            double amount = readAmount("Enter transfer amount: M");

            if (amount <= 0) {
                sender.logTransaction(
                        "Transfer Out", amount,
                        "FAILED: Transfer amount must be positive.");

                throw new IllegalArgumentException(
                        "Transfer amount must be greater than zero.");
            }

            // Withdraw first so the sender's account rules are enforced.
            sender.withdraw(amount);

            // Deposit into the recipient account.
            recipient.deposit(amount);

            // Record the transfer itself in both accounts' audit trails.
            sender.logTransaction(
                    "Transfer Out", amount,
                    "SUCCESS: Transferred to " + recipient.getAccountNumber());

            recipient.logTransaction(
                    "Transfer In", amount,
                    "SUCCESS: Received from " + sender.getAccountNumber());

            System.out.println("Transfer completed successfully.");
            System.out.printf("Sender's new balance: M%.2f%n",
                    sender.getBalance());
            System.out.printf("Recipient's new balance: M%.2f%n",
                    recipient.getBalance());

        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");

        } catch (Exception e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }

    // Calculate interest for a selected account
    private static void calculateInterest() {
        BankAccount acc = askForAccount();

        if (acc == null) {
            return;
        }

        try {
            double previousBalance = acc.getBalance();

            acc.calculateInterest();

            double difference = acc.getBalance() - previousBalance;

            if (difference > 0) {
                System.out.printf(
                        "Interest added: M%.2f%n", difference);
            } else {
                System.out.println(
                        "No interest is earned on this account.");
            }

            System.out.printf(
                    "Current balance: M%.2f%n", acc.getBalance());

        } catch (Exception e) {
            System.out.println(
                    "Interest calculation failed: " + e.getMessage());
        }
    }

    // Request a loan from the bank
    private static void requestLoan() {
        BankAccount acc = askForAccount();

        if (acc == null) {
            return;
        }

        if (!(acc instanceof Loanable)) {
            System.out.println(
                    "This account does not support loan requests.");
            return;
        }

        try {
            double amount = readAmount("Enter requested loan amount: M");

            Loanable loanAccount = (Loanable) acc;
            loanAccount.requestLoan(amount);

            System.out.println("Loan request approved.");
            System.out.println(
                    "Note: Approval is logged; the loan amount has not "
                    + "been added to the account balance.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");

        } catch (Exception e) {
            System.out.println("Loan request failed: " + e.getMessage());
        }
    }

    // Display information about one account
    private static void displayAccountDetails() {
        BankAccount acc = askForAccount();

        if (acc == null) {
            return;
        }

        System.out.println("\n--- Account Details ---");
        acc.displayAccountInfo();

        if (acc instanceof SavingsAccount) {
            System.out.println("Account Type: Savings Account");
            System.out.println("Annual Interest Rate: 5%");
            System.out.println("Minimum Required Balance: M100");

        } else if (acc instanceof CurrentAccount) {
            System.out.println("Account Type: Current Account");
            System.out.println("Interest: Not applicable");
            System.out.println("Overdraft Limit: M2000");
        }

        System.out.println("Loan Support: "
                + (acc instanceof Loanable ? "Available" : "Unavailable"));
    }
}
