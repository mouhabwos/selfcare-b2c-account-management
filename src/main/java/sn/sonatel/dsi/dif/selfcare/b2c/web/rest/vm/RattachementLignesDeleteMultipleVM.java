package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class RattachementLignesDeleteMultipleVM {

    @NotEmpty
    private List<String> listMsisdn;

    private boolean deleted = false;

    public List<String> getListMsisdn() {
        return listMsisdn;
    }

    public void setListMsisdn(List<String> listMsisdn) {
        this.listMsisdn = listMsisdn;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
