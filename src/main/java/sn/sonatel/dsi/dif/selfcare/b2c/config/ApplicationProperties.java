package sn.sonatel.dsi.dif.selfcare.b2c.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

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

    private String grantType;
    private String clientId;
    private String clientSecret;
    private String keyAccessTokenUri;


    private final SelfcareMail selfcareMail = new SelfcareMail();

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private final UrgenceDepannage urgenceDepannage = new UrgenceDepannage();

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

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getKeyAccessTokenUri() {
        return keyAccessTokenUri;
    }

    public void setKeyAccessTokenUri(String keyAccessTokenUri) {
        this.keyAccessTokenUri = keyAccessTokenUri;
    }

    /**
     * @author BOUYA KANDE
     * @since 1.1.4
     */
    public static class SelfcareMail {

        private String senderAddress;

        private String senderName;

        private String mailSubject;

        public String getMailSubject() {
            return mailSubject;
        }

        public void setMailSubject(String mailSubject) {
            this.mailSubject = mailSubject;
        }

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

    /**
     * @author BOUYA KANDE
     * @since 1.1.4
     */
    public static class UrgenceDepannage{

        @Setter(AccessLevel.PUBLIC)
        @Getter(AccessLevel.PUBLIC)
        private List<Operation> operation = new ArrayList<>();

        public static class Operation{

            @Setter(AccessLevel.PUBLIC)
            @Getter(AccessLevel.PUBLIC)
            private String code;

            @Setter(AccessLevel.PUBLIC)
            @Getter(AccessLevel.PUBLIC)
            private String title;

        }

    }
}
