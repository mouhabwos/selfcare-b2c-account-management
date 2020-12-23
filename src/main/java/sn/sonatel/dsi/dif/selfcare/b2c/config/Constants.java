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

    public static final String LOGIN_REGEX_VALID_NUMBER = "^(00221|\\+221|221)? ?(77|78|76|70) ?([0-9]{3}) ?([0-9]{2}) ?([0-9]{2})$";

    public static final String FIX_REGEX_VALID_NUMBER = "^(00221|\\+221|221)? ?(33) ?([0-9]{3}) ?([0-9]{2}) ?([0-9]{2})$";

    public static final String NAME_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[\\w~@#$%^&*+=`|{}:;!.?\\\"()\\[\\]-]{8,}$";

    public static final String MOTDEPASSE_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[\\w~@#$%^&*+=`|{}:;!.?\\\"()\\[\\]-]{8,}$";

    public static final String PDF_FILE_EXTENSION_REGEX = "(pdf|PDF|doc|DOC|docx|DOCX)$";

    public static final String IMAGE_EXTENSION_REGEX = "(png|jpg|jpeg|PNG|JPG|JPEG)$";


    public static final String SELFCARE_B2C_SOAP_SERVICE = "selfcare-b2c-soap";

    public static final String SELFCARE_UAA_SERVICE = "selfcare-b2c-uaa";

    public static final String SELFCARE_FILE_MANAGER_SERVICE = "selfcare-file-manager";

    public static final String SELFCARE_BOOSTER_SERVICE = "selfcare-b2c-booster-management";

    public static final String SELFCARE_SERVICE_OTP = "selfcare-otp";

    public static final String SELFCARE_GATEWAY = "selfcare-gateway";
    public static final String API_MANAGEMENT = "http://api.rec.orange-sonatel.com";


    public static final String ENDPOINT_API_MANAGEMENT = "/api-accountmanagement";

    public static final String FILE_DOWNLOAD = "/api/download/";

    public static final String FILE_UPLOAD = "/api/upload";

    public static final String GET_ABONNE = "/api/soap/information-client";

    public static final String GET_SOUSCRIPTION_ABONNE = "/api/soap/souscription";

    public static final String URL_SEND_MESSAGE = "/api/message/send";

    public static final String REGISTER_ACCOUNT = "/api/register";
    public static final String UAA_UPDATE_USERS = "/api/extern/users";

    public static final String URL_CHECK_CODE_OTP = "/api/code-otp-infos/check";

    public static final String URL_CHECK_VALID_REQUEST_OTP = "/api/code-otp-infos/check-valid-request/{msisdn}";

    public static final String URL_GET_NUMERO_CLIENT = "/api/numero-client/{msisdn}";

    public static final String PARTY_MANAGEMENT = "api-partymanagement";


    public static final String VALIDE ="valid";

    public static final String EMAIL_PART1 = "selfcare-b2c-";

    public static final String EMAIL_PART2 = "@selfcare.com";

    public static final String EMAIL_SERVICE_CLIENT = "vieuxmamadou.kasse@orange-sonatel.com";

    public static final long MAX_DELAY_TO_TRY_CONNEXION = 300;

    public static final int getMaxAttempts = 4;

    public static final String ORANGE_ET_MOI ="OrangeetMoi";

    public static final String API_MANAGEMENT_CUSTOMER_OFFER_NAME="api-customer-offer";

    private Constants() {

        //Default constructor
    }
}
