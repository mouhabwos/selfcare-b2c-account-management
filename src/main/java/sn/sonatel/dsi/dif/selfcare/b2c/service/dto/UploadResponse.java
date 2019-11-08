package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

import sn.sonatel.dsi.dif.selfcare.b2c.domain.SponsorUploadResponse;

import java.util.List;

public class UploadResponse {

    private int nbreSponsorAdded;
    private List<SponsorUploadResponse> errorList;

    public int getNbreSponsorAdded() {
        return nbreSponsorAdded;
    }

    public void setNbreSponsorAdded(int nbreSponsorAdded) {
        this.nbreSponsorAdded = nbreSponsorAdded;
    }

    public List<SponsorUploadResponse> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<SponsorUploadResponse> errorList) {
        this.errorList = errorList;
    }
}
