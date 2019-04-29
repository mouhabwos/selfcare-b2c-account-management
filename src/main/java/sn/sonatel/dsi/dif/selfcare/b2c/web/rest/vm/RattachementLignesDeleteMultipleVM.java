package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class RattachementLignesDeleteMultipleVM {

    @NotEmpty
    private List<String> listMsisdn;

    @NotNull
    private String login;

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
