package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final String USERNAME_REGEX = "^((\"[\\w-\\s]+\")|([\\w-]+(?:\\.[\\w-]+)*)|(\"[\\w-\\s]+\")([\\w-]+(?:\\.[\\w-]+)*))(@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$)|(@\\[?((25[0-5]\\.|2[0-4][0-9]\\.|1[0-9]{2}\\.|[0-9]{1,2}\\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\]?$)|(^(00221|\\+221|221)? ?(77|78) ?([0-9]{3}) ?([0-9]{2}) ?([0-9]{2})$)";

    public static final String LOGIN_REGEX_VALID_NUMBER = "^(00221|\\+221|221)? ?(77|78) ?([0-9]{3}) ?([0-9]{2}) ?([0-9]{2})$";

    public static final String FIX_REGEX_VALID_NUMBER = "^(00221|\\+221|221)? ?(33) ?([0-9]{3}) ?([0-9]{2}) ?([0-9]{2})$";

    public static final String NAME_REGEX = "^[^0-9_!¡?÷?¿/\\\\+=,.@#$%ˆ&*(){}|~<>;:\\[\\]-]{1,}$";

    public static final String MOTDEPASSE_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{8,}$";

    public static final String SYSTEM_ACCOUNT = "system";

    public static final String ANONYMOUS_USER = "anonymoususer";

    public static final String DEFAULT_LANGUAGE = "fr";

    public static final String IPLOCAL = "127.0.0.1";

    public static final String MONGO_DB_ENVIRONMENT = "MONGO DB ENVIRONMENT URIs .... {}";

    public static final String USERS_BY_LOGIN_CACHE = "usersByLogin";

    public static final String USERS_BY_EMAIL_CACHE = "usersByEmail";

    public static final long MAX_DELAY_TO_TRY = 120;

    public static final String QUERY = "&query=";

    private Constants() {

    }

}
