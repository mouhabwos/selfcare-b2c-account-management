package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors;

public class SponsorException extends Exception {

    final String message;

    public SponsorException(final String message) {
        this.message = message;
    }

    public SponsorException(final String message, final Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
