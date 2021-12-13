package sn.sonatel.dsi.dif.selfcare.b2c.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "en";

    public static final String LOGIN_REGEX_VALID_NUMBER = "^(00221|\\+221|221)? ?(77|78|76|70) ?([0-9]{3}) ?([0-9]{2}) ?([0-9]{2})$";


    public static final String PDF_FILE_EXTENSION_REGEX = "(pdf|PDF|doc|DOC|docx|DOCX)$";

    public static final String IMAGE_EXTENSION_REGEX = "(png|jpg|jpeg|PNG|JPG|JPEG)$";


    public static final String URL_SEND_MESSAGE = "/api/message/send";
    public static final String REGISTER_ACCOUNT = "/api/register";
    public static final String UAA_UPDATE_USERS = "/api/extern/users";
    public static final String UAA_RESET_PASSWORD = "/api/account/b2c/reset-password";
    public static final String URL_CHECK_CODE_OTP = "/api/code-otp-infos/check";
    public static final String URL_CHECK_VALID_REQUEST_OTP = "/api/code-otp-infos/check-valid-request/{msisdn}";



    public static final String VALIDE ="valid";
    public static final String EMAIL_PART1 = "selfcare-b2c-";
    public static final String EMAIL_PART2 = "@selfcare.com";
    public static final String EMAIL_SERVICE_CLIENT = "vieuxmamadou.kasse@orange-sonatel.com";
    public static final long MAX_DELAY_TO_TRY_CONNEXION = 300;
    public static final String ORANGE_ET_MOI ="OrangeetMoi";
    public static final String API_MANAGEMENT_CUSTOMER_OFFER_NAME="api-customer-offer";

    private Constants() {}
}
