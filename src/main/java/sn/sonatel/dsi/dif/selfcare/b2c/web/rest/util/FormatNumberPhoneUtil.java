package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatNumberPhoneUtil {

    private static final Pattern EXTRACT_MOBILE_NUMBER = Pattern.compile(Constants.VALIDE_NUMBER_ORANGE_FIXE_MOBILE);



    private FormatNumberPhoneUtil() {
        // default constructor
    }

    public static String extractNumberWithoutSuffix(String msisdn) {

        if(msisdn != null){
            Matcher matcher = EXTRACT_MOBILE_NUMBER.matcher(msisdn);

            if (matcher.matches()) {
                StringBuilder numberbuilder = new StringBuilder();

                numberbuilder.append(matcher.group(2))

                    .append(matcher.group(3))

                    .append(matcher.group(4))

                    .append(matcher.group(5));

                return numberbuilder.toString();
            }
        }


        return "";

    }

}
