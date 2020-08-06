package sn.sonatel.dsi.dif.selfcare.b2c.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String hmacSecret;
    private String tmpPath;

    private Map<String,String> requestTitleMap = new HashMap<>();
    private Map<String,String> requestDescriptionMap = new HashMap<>();
    private Map<String,String> order = new HashMap<>();
    private Map<String,String> historic = new HashMap<>();

    private final SelfcareMail selfcareMail = new SelfcareMail();

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private final UrgenceDepannage urgenceDepannage = new UrgenceDepannage();

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private final ApiManagement apiManagement = new ApiManagement();

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private final SendSms sendSms = new SendSms();

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private final Scheduler scheduler = new Scheduler();

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private final ServeurFtp serveurFtp = new ServeurFtp();

    public String getHmacSecret() {
        return hmacSecret;
    }

    public void setHmacSecret(String hmacSecret) {
        this.hmacSecret = hmacSecret;
    }

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

    public String getTmpPath() {
        return tmpPath;
    }

    public void setTmpPath(String tmpPath) {
        this.tmpPath = tmpPath;
    }

    public Map<String, String> getRequestTitleMap() {
        return requestTitleMap;
    }

    public void setRequestTitleMap(Map<String, String> requestTitleMap) {
        this.requestTitleMap = requestTitleMap;
    }

    public Map<String, String> getRequestDescriptionMap() {
        return requestDescriptionMap;
    }

    public void setRequestDescriptionMap(Map<String, String> requestDescriptionMap) {
        this.requestDescriptionMap = requestDescriptionMap;
    }


    public Map<String, String> getOrder() {
        return order;
    }

    public void setOrder(Map<String, String> order) {
        this.order = order;
    }

    public Map<String, String> getHistoric() {
        return historic;
    }

    public void setHistoric(Map<String, String> historic) {
        this.historic = historic;
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


    /**
     * @author BOUYA KANDE
     * @since 1.1.4
     */
    public static class ApiManagement{

        @Getter(AccessLevel.PUBLIC)
        @Setter(AccessLevel.PUBLIC)
        private final Auth auth = new Auth();

        @Getter(AccessLevel.PUBLIC)
        @Setter(AccessLevel.PUBLIC)
        private final UrlApi urlApi = new UrlApi();

        @Getter(AccessLevel.PUBLIC)
        @Setter(AccessLevel.PUBLIC)
        private final PartyManagement partyManagement = new PartyManagement();

        public static class Auth{

            @Setter(AccessLevel.PUBLIC)
            @Getter(AccessLevel.PUBLIC)
            private String grantType;

            @Setter(AccessLevel.PUBLIC)
            @Getter(AccessLevel.PUBLIC)
            private String clientId;

            @Setter(AccessLevel.PUBLIC)
            @Getter(AccessLevel.PUBLIC)
            private String clientSecret;

            @Setter(AccessLevel.PUBLIC)
            @Getter(AccessLevel.PUBLIC)
            private String keyAccessTokenUri;

        }

        public static class UrlApi{

            @Setter(AccessLevel.PUBLIC)
            @Getter(AccessLevel.PUBLIC)
            private String baseUrl;

            @Setter(AccessLevel.PUBLIC)
            @Getter(AccessLevel.PUBLIC)
            private String urlAccountManagement;

            @Setter(AccessLevel.PUBLIC)
            @Getter(AccessLevel.PUBLIC)
            private String baseName;

        }

        public static class PartyManagement{

            @Getter(AccessLevel.PUBLIC)
            @Setter(AccessLevel.PUBLIC)
            private String name;

            @Getter(AccessLevel.PUBLIC)
            @Setter(AccessLevel.PUBLIC)
            private String urlIndividualInformation;

            @Getter(AccessLevel.PUBLIC)
            @Setter(AccessLevel.PUBLIC)
            private String urlOrganizationInformation;

        }


    }

    /**
     * @author BOUYA KANDE
     * @since 1.4.0
     */

    public static class SendSms{

        @Setter(AccessLevel.PUBLIC)
        @Getter(AccessLevel.PUBLIC)
        private Sponsorship sponsorship;

        public static class Sponsorship{

            @Setter(AccessLevel.PUBLIC)
            @Getter(AccessLevel.PUBLIC)
            private String smsSponsee;

            @Setter(AccessLevel.PUBLIC)
            @Getter(AccessLevel.PUBLIC)
            private String smsSponsor;

            @Setter(AccessLevel.PUBLIC)
            @Getter(AccessLevel.PUBLIC)
            private String smsPromo;

        }
    }

    /**
     * @author BOUYA KANDE
     * @since 1.4.0
     */
    public static class Scheduler{

        @Setter(AccessLevel.PUBLIC)
        @Getter(AccessLevel.PUBLIC)
        private String cronDisabledSponsee;
    }

    public static class ServeurFtp{

        @Setter(AccessLevel.PUBLIC)
        @Getter(AccessLevel.PUBLIC)
        private String ipServeur;

        @Setter(AccessLevel.PUBLIC)
        @Getter(AccessLevel.PUBLIC)
        private String password;

        @Setter(AccessLevel.PUBLIC)
        @Getter(AccessLevel.PUBLIC)
        private String user;

        @Setter(AccessLevel.PUBLIC)
        @Getter(AccessLevel.PUBLIC)
        private int port;

        @Setter(AccessLevel.PUBLIC)
        @Getter(AccessLevel.PUBLIC)
        private String directory;
    }
}
