package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.InvalidOrangeNumberException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatNumberPhoneUtil {

    private static final Logger log = LoggerFactory.getLogger(FormatNumberPhoneUtil.class);

    private static final Integer PHONE_NUMBER_PREFIX_GROUP = 2;

    private static final Integer PHONE_NUMBER_START_GROUP = 3;

    private static final Integer PHONE_NUMBER_MIDDLE_GROUP = 4;

    private static final Integer PHONE_NUMBER_END_GROUP = 5;

    private static final Pattern EXTRACT_MOBILE_NUMBER = Pattern.compile(Constants.VALIDE_NUMBER_ORANGE_FIXE_MOBILE);



    private FormatNumberPhoneUtil() {
        // default constructor
    }

    public static String extractNumberWithoutSuffix(String msisdn) {

        log.debug("extract valid orange number for {} : ", msisdn);

        Assert.notNull(msisdn," Le numero ne doit pas etre null ");

            Matcher matcher = EXTRACT_MOBILE_NUMBER.matcher(msisdn);

            if (matcher.matches()) {
                StringBuilder numberbuilder = new StringBuilder();

                numberbuilder.append(matcher.group(PHONE_NUMBER_PREFIX_GROUP))

                    .append(matcher.group(PHONE_NUMBER_START_GROUP))

                    .append(matcher.group(PHONE_NUMBER_MIDDLE_GROUP))

                    .append(matcher.group(PHONE_NUMBER_END_GROUP));

                return numberbuilder.toString();
            }

        throw new InvalidOrangeNumberException();

    }

}
