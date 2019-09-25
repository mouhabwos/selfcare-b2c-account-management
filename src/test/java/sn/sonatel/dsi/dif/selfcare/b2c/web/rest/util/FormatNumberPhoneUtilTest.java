package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FormatNumberPhoneUtilTest {

    private static final String NUMERO221 = "221 77 562 23 23";

    private static final String NUMERO00221 = "00221 77 562 23 23";

    private static final String NUMEROPLUS221 = "+221 77 562 23 23";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getNumberFormatStart221() {

        String msisdn = FormatNumberPhoneUtil.extractNumberWithoutSuffix( NUMERO221 );
        assertThat ( msisdn ).isEqualTo ( "775622323" );

    }

    @Test
    public void getNumberFormatStart00221() {

        String msisdn = FormatNumberPhoneUtil.extractNumberWithoutSuffix( NUMERO00221 );
        assertThat ( msisdn ).isEqualTo ( "775622323" );

    }

    @Test
    public void getNumberFormatStartPlus221() {

        String msisdn = FormatNumberPhoneUtil.extractNumberWithoutSuffix( NUMEROPLUS221 );
        assertThat ( msisdn ).isEqualTo ( "775622323" );

    }

    @Test(expected = IllegalArgumentException.class)
    public void getNumberFormatWithNull() {
        FormatNumberPhoneUtil.extractNumberWithoutSuffix( null );

    }
}
