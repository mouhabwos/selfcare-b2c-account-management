package sn.sonatel.dsi.dif.selfcare.b2c.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "fr";

    public static final String EMAIL_VALIDATION = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

    public static final String USERNAME_REGEX = "^((\"[\\w-\\s]+\")|([\\w-]+(?:\\.[\\w-]+)*)|(\"[\\w-\\s]+\")([\\w-]+(?:\\.[\\w-]+)*))(@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$)|(@\\[?((25[0-5]\\.|2[0-4][0-9]\\.|1[0-9]{2}\\.|[0-9]{1,2}\\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\]?$)|(^(00221|\\+221|221)? ?(77|78) ?([0-9]{3}) ?([0-9]{2}) ?([0-9]{2})$)";

    public static final String LOGIN_REGEX_VALID_NUMBER = "^(00221|\\+221|221)? ?(77|78) ?([0-9]{3}) ?([0-9]{2}) ?([0-9]{2})$";

    public static final String FIX_REGEX_VALID_NUMBER = "^(00221|\\+221|221)? ?(33) ?([0-9]{3}) ?([0-9]{2}) ?([0-9]{2})$";

    public static final String NAME_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[\\w~@#$%^&*+=`|{}:;!.?\\\"()\\[\\]-]{8,}$";

    public static final String MOTDEPASSE_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{8,}$";

    public static final String SELFCARE_B2C_SOAP_SERVICE = "http://selfcare-b2c-soap";

    public static final String SELFCARE_UAA_SERVICE = "http://selfcare-uaa";

    public static final String SELFCARE_FILE_MANAGER_SERVICE = "http://selfcare-file-manager";

    public static final String FILE_DOWNLOAD = "/api/download/";

    public static final String GET_ABONNE = "/api/soap/information-client";

    public static final String GET_SOUSCRIPTION_ABONNE = "/api/soap/souscription";

    public static final String REGISTER_ACCOUNT = "/api/register";

    public static final String GET_FORMULE_BY_MSISDN = "/api/soap/achats/formules/";

    public static final String EMAIL_SERVICE_CLIENT = "bouyakandee@gmail.com";

    private Constants() {

        //Default constructor
    }
}
