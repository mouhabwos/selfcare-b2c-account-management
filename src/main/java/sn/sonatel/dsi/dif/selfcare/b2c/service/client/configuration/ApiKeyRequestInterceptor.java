package sn.sonatel.dsi.dif.selfcare.b2c.service.client.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sn.sonatel.dsi.dif.selfcare.b2c.service.TokenService;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 *
 */
@Component
public class ApiKeyRequestInterceptor implements RequestInterceptor {

    private final Logger log = LoggerFactory.getLogger(TokenService.class);


    @Autowired
    TokenService tokenService;

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";


    public ApiKeyRequestInterceptor() {
        //Default Constructor
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {

        log.debug("################## apiKeyRequestInterceptor ################## ");

        requestTemplate.header(AUTHORIZATION, BEARER+tokenService.getToken().getAccessToken());

    }

}
