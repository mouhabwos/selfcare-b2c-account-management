package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm;



public class TestVM  {

     private String status;

    public TestVM() {

        //Default Constructor
    }

    @Override
    public String toString() {
        return "TestVM{" +
            "status='" + status + '\'' +
            '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
