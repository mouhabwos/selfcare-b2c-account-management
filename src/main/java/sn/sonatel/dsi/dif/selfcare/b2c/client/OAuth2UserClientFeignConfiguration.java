package sn.sonatel.dsi.dif.selfcare.b2c.client;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import sn.sonatel.dsi.dif.selfcare.b2c.security.oauth2.AuthorizationHeaderUtil;

import java.io.IOException;

public class OAuth2UserClientFeignConfiguration {

    @Bean(name = "userFeignClientInterceptor")
    public RequestInterceptor getUserFeignClientInterceptor(AuthorizationHeaderUtil authorizationHeaderUtil) throws IOException {
        return new TokenRelayRequestInterceptor (authorizationHeaderUtil);
    }
}
