package banking;

public interface Loanable {
    void requestLoan(double amount) throws IllegalArgumentException;
}
