package sn.sonatel.dsi.dif.selfcare.b2c.notification.conf.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatNumberPhoneUtil {

    private static final Logger log = LoggerFactory.getLogger(FormatNumberPhoneUtil.class);

    private static final Integer PHONE_NUMBER_PREFIX_GROUP = 2;

    private static final Integer PHONE_NUMBER_START_GROUP = 3;

    private static final Integer PHONE_NUMBER_MIDDLE_GROUP = 4;

    private static final Integer PHONE_NUMBER_END_GROUP = 5;

    public static final String LOGIN_REGEX_VALID_NUMBER = "^(00221|\\+221|221)? ?(77|78|70|76) ?([0-9]{3}) ?([0-9]{2}) ?([0-9]{2})$";

    private static final Pattern EXTRACT_MOBILE_NUMBER = Pattern.compile(LOGIN_REGEX_VALID_NUMBER);



    private FormatNumberPhoneUtil() {
        // default constructor
    }

    public static String extractNumberWithoutSuffix(String msisdn) {

        log.debug("extract valid orange number for {} : ", msisdn);

        Assert.notNull(msisdn," Le numero ne doit pas etre null ");

            Matcher matcher = EXTRACT_MOBILE_NUMBER.matcher(msisdn.trim());

            if (matcher.matches()) {
                StringBuilder numberbuilder = new StringBuilder();

                numberbuilder.append(matcher.group(PHONE_NUMBER_PREFIX_GROUP))

                    .append(matcher.group(PHONE_NUMBER_START_GROUP))

                    .append(matcher.group(PHONE_NUMBER_MIDDLE_GROUP))

                    .append(matcher.group(PHONE_NUMBER_END_GROUP));

                return numberbuilder.toString();
            }

        return "";

    }

}
