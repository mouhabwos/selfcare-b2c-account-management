package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;


public class WelcomeBoosterStatus {

    private Status status=Status.FAILURE;
    private BoosterType type;
    private BoosterValue value;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BoosterType getType() {
        return type;
    }

    public void setType(BoosterType type) {
        this.type = type;
    }

    public BoosterValue getValue() {
        return value;
    }

    public void setValue(BoosterValue value) {
        this.value = value;
    }


    public static
    class BoosterValue {
        String amount;
        BoosterUnit unit;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public BoosterUnit getUnit() {
            return unit;
        }

        public void setUnit(BoosterUnit unit) {
            this.unit = unit;
        }
    }
   public enum BoosterUnit{
        CFA,MO,GO
    }
    public enum BoosterType{
        PASS,RECHARGE
    }
    public enum Status {
        SUCCESS, FAILURE, PENDING
    }
}
