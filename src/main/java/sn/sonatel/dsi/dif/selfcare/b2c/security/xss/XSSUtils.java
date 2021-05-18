package sn.sonatel.dsi.dif.selfcare.b2c.security.xss;

import java.util.Objects;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.owasp.esapi.ESAPI;

public class XSSUtils {

    static String stripXSS(String value) {
        if (Objects.isNull(value)) {
            return null;
        }
        value = ESAPI.encoder()
            .canonicalize(value)
            .replaceAll("\0", "");
        return Jsoup.clean(value, Whitelist.none());
    }

    private XSSUtils() {
    }
}
