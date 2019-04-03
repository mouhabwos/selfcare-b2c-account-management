package sn.sonatel.dsi.dif.selfcare.b2c.aop.logging.audit;

class SelfcareLogException {

    private String type;
    private String cause;
    private String nomMethode;
    private String message;

    public SelfcareLogException(String type, String cause, String nomMethode, String message) {
        this.type = type;
        this.cause = cause;
        this.nomMethode = nomMethode;
        this.message = message;
    }

    public SelfcareLogException() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getNomMethode() {
        return nomMethode;
    }

    public void setNomMethode(String nomMethode) {
        this.nomMethode = nomMethode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SelfcareLogException{" +
            "type='" + type + '\'' +
            ", cause='" + cause + '\'' +
            ", nomMethode='" + nomMethode + '\'' +
            ", message='" + message + '\'' +
            '}';
    }
}
