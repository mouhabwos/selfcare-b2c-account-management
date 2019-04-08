package sn.sonatel.dsi.dif.selfcare.b2c.exception;

public class Message {

    public static  final String EMAIL_EXIST  = "emailexist";
    public static  final String ID_EXISTS  = "idexists";
    public static  final String ID_NULL  = "idnull";
    public static  final String VALIDATION_ERROR  = "validationerror";
    public static  final String ADD_USER_ERROR  = "addusererror";

    String msg;
    int error;

    public Message(String msg) {
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
