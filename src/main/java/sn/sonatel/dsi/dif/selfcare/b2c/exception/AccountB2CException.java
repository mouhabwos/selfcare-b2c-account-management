package sn.sonatel.dsi.dif.selfcare.b2c.exception;

public class AccountB2CException extends Exception {

     final String message;



    public AccountB2CException(final String message) {
        super(message);
        this.message = message;
    }

    public AccountB2CException(final String message, final Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
