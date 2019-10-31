package sn.sonatel.dsi.dif.selfcare.b2c.domain;

import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SponsorDTO;

public class SponsorUploadResponse extends SponsorDTO {

    private String errorMsg;
    private String typeResp;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getTypeResp() {
        return typeResp;
    }

    public void setTypeResp(String typeResp) {
        this.typeResp = typeResp;
    }


    @Override
    public String toString() {
        return "SponsorUploadResponse{" +
            "errorMsg='" + errorMsg + '\'' +
            ", typeResp='" + typeResp + '\'' +
            '}';
    }
}
