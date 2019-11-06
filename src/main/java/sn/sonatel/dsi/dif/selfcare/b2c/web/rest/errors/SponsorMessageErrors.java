package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors;

public class SponsorMessageErrors {

    public static final String INCORRECT_MSISDN = "incorrectmsisdn";
    public static final String MSISDN_ALREADY_USED = "Ce numéro est déja utilisé";
    public static final String FILE_ERROR = "ilisablefile";
    public static final String FORMAT_INVALID = "formatinvalid";

    String msg;
    int error;

    public SponsorMessageErrors(String msg) {
        super();
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

}
