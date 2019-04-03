package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util;

public class FormatNumberPhoneUtil {

    private FormatNumberPhoneUtil() {
        // default constructor
    }

    public static String getNumberFormat(String msisdn) {

        if (msisdn == null) {
            return "";
        } else {
            msisdn = msisdn.trim ();

            if (msisdn.startsWith ( "+221" )) {

                msisdn = msisdn.substring ( 4 );

            } else if (msisdn.startsWith ( "221" )) {

                msisdn = msisdn.substring ( 3 );

            } else if (msisdn.startsWith ( "00221" )) {

                msisdn = msisdn.substring ( 5 );

            }

            msisdn = msisdn.replaceAll ( " ", "" );

            return msisdn;
        }

    }

}
