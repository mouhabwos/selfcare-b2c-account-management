package sn.sonatel.dsi.dif.selfcare.b2c.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Selfcare B 2 C.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String authenticationAuthorisationUserServerHost;
    private String baseUrlAdmin;
    private String emailAdmin;
    private Integer maxAttempts;
    private String urlOtp;
    private String messageBlockUser;
    private String serviceClientOrange;
    private String emailServiceClientOrange;
    private String lienIbou;


    private final SelfcareMail selfcareMail = new SelfcareMail();

    public SelfcareMail getSelfcareMail() {
        return selfcareMail;
    }





    public String getAuthenticationAuthorisationUserServerHost() {
        return authenticationAuthorisationUserServerHost;
    }

    public void setAuthenticationAuthorisationUserServerHost(String authenticationAuthorisationUserServerHost) {
        this.authenticationAuthorisationUserServerHost = authenticationAuthorisationUserServerHost;
    }

    public Integer getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(Integer maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public String getBaseUrlAdmin() {
        return baseUrlAdmin;
    }

    public void setBaseUrlAdmin(String baseUrlAdmin) {
        this.baseUrlAdmin = baseUrlAdmin;
    }

    public String getEmailAdmin() {
        return emailAdmin;
    }

    public void setEmailAdmin(String emailAdmin) {
        this.emailAdmin = emailAdmin;
    }

    public String getUrlOtp() {
        return urlOtp;
    }

    public void setUrlOtp(String urlOtp) {
        this.urlOtp = urlOtp;
    }

    public String getMessageBlockUser() {
        return messageBlockUser;
    }

    public void setMessageBlockUser(String messageBlockUser) {
        this.messageBlockUser = messageBlockUser;
    }

    public String getServiceClientOrange() {
        return serviceClientOrange;
    }

    public void setServiceClientOrange(String serviceClientOrange) {
        this.serviceClientOrange = serviceClientOrange;
    }

    public String getLienIbou() {
        return lienIbou;
    }

    public void setLienIbou(String lienIbou) {
        this.lienIbou = lienIbou;
    }

    public String getEmailServiceClientOrange() {
        return emailServiceClientOrange;
    }

    public void setEmailServiceClientOrange(String emailServiceClientOrange) {
        this.emailServiceClientOrange = emailServiceClientOrange;
    }


    /**
     * @author BOUYA KANDE
     * @since 1.1.4
     */
    public static class SelfcareMail {

        private String senderAddress;

        private String senderName;

        public String getSenderAddress() {
            return senderAddress;
        }

        public void setSenderAddress(String senderAddress) {
            this.senderAddress = senderAddress;
        }

        public String getSenderName() {
            return senderName;
        }

        public void setSenderName(String senderName) {
            this.senderName = senderName;
        }

    }
}
