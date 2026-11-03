package banking;

public class SavingsAccount extends BankAccount implements Loanable {

    private static final double INTEREST_RATE = 0.05; // 5% annual
    private static final double MIN_BALANCE = 100.00; // must remain after withdrawal

    public SavingsAccount(String accountNumber, String accountHolderName, double initialBalance) {
        super(accountNumber, accountHolderName, initialBalance);
    }

    @Override
    public void deposit(double amount) throws IllegalArgumentException {
        if (amount <= 0) {
            logTransaction("Deposit", amount, "FAILED: Deposit must be greater than zero.");
            throw new IllegalArgumentException("Deposit must be greater than zero.");
        }
        setBalance(getBalance() + amount);
        logTransaction("Deposit", amount, "SUCCESS");
    }

    @Override
    public void withdraw(double amount) throws IllegalArgumentException {
        if (amount <= 0) {
            logTransaction("Withdrawal", amount, "FAILED: Withdrawal must be greater than zero.");
            throw new IllegalArgumentException("Withdrawal must be greater than zero.");
        }
        if (getBalance() - amount < MIN_BALANCE) {
            logTransaction("Withdrawal", amount, "FAILED: Minimum balance of M100 must remain.");
            throw new IllegalArgumentException("Insufficient funds. Must keep M100 minimum balance.");
        }
        setBalance(getBalance() - amount);
        logTransaction("Withdrawal", amount, "SUCCESS");
    }

    @Override
    public void calculateInterest() {
        double interest = getBalance() * INTEREST_RATE;
        setBalance(getBalance() + interest);
        logTransaction("Interest", interest, "SUCCESS");
    }

    @Override
    public void requestLoan(double amount) throws IllegalArgumentException {
        if (amount <= 0) {
            logTransaction("Loan Request", amount, "FAILED: Loan must be positive.");
            throw new IllegalArgumentException("Loan amount must be positive.");
        }
        // Example loan rule: Savings accounts can request up to M5000
        if (amount > 5000) {
            logTransaction("Loan Request", amount, "FAILED: Exceeds limit of M5000.");
            throw new IllegalArgumentException("Loan request exceeds permitted limit (M5000).");
        }
        logTransaction("Loan Request", amount, "SUCCESS: Loan approved.");
    }
}
