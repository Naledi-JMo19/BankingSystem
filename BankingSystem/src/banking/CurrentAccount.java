package banking;

public class CurrentAccount extends BankAccount implements Loanable {

    private static final double OVERDRAFT_LIMIT = -2000.00; // can go negative up to -2000

    public CurrentAccount(String accountNumber, String accountHolderName, double initialBalance) {
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
        if (getBalance() - amount < OVERDRAFT_LIMIT) {
            logTransaction("Withdrawal", amount, "FAILED: Overdraft limit exceeded.");
            throw new IllegalArgumentException("Insufficient funds. Overdraft limit is M2000.");
        }
        setBalance(getBalance() - amount);
        logTransaction("Withdrawal", amount, "SUCCESS");
    }

    @Override
    public void calculateInterest() {
        
        logTransaction("Interest", 0, "NO INTEREST");
    }

    @Override
    public void requestLoan(double amount) throws IllegalArgumentException {
        if (amount <= 0) {
            logTransaction("Loan Request", amount, "FAILED: Loan must be positive.");
            throw new IllegalArgumentException("Loan amount must be positive.");
        }
        //loan rule: Current accounts can request up to M10000
        if (amount > 10000) {
            logTransaction("Loan Request", amount, "FAILED: Exceeds limit of M10000.");
            throw new IllegalArgumentException("Loan request exceeds permitted limit (M10000).");
        }
        logTransaction("Loan Request", amount, "SUCCESS: Loan approved.");
    }
}

