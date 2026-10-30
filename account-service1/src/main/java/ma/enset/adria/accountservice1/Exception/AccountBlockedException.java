package ma.enset.adria.accountservice1.Exception;


public class AccountBlockedException extends RuntimeException {

    public AccountBlockedException(String message) {
        super(message);
    }
}