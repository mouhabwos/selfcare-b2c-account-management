package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

/**
 * Created by centonni on 30/11/18.
 */
public class SouscriptionDto {

	protected String msisdn;

	protected String imsi;

	protected String profil;

	protected String nomOffre;

	protected String codeOffre;

	protected String code;

	protected String message;

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getProfil() {
		return profil;
	}

	public void setProfil(String profil) {
		this.profil = profil;
	}

	public String getNomOffre() {
		return nomOffre;
	}

	public void setNomOffre(String nomOffre) {
		this.nomOffre = nomOffre;
	}

	public String getCodeOffre() {
		return codeOffre;
	}

	public void setCodeOffre(String codeOffre) {
		this.codeOffre = codeOffre;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "SouscriptionDto{" + "msisdn='" + msisdn + '\'' + ", imsi='" + imsi + '\''
				+ ", profil='" + profil + '\'' + ", nomOffre='" + nomOffre + '\''
				+ ", codeOffre='" + codeOffre + '\'' + ", code='" + code + '\''
				+ ", message='" + message + '\'' + '}';
	}

}
