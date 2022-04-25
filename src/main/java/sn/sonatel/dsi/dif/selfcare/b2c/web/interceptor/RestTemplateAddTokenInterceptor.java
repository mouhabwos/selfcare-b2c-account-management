package sn.sonatel.dsi.dif.selfcare.b2c.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import sn.sonatel.dsi.dif.selfcare.b2c.security.oauth2.OAuthIdpTokenResponseDTO;

import java.io.IOException;

public class RestTemplateAddTokenInterceptor implements ClientHttpRequestInterceptor {
    private static final String BEARER_TOKEN_TYPE = "Bearer";
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public ClientHttpResponse intercept( HttpRequest request,  byte[] body, ClientHttpRequestExecution execution) throws IOException {

    	String authorizationHeader = "";
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if(securityContext != null) {
            Authentication authentication = securityContext.getAuthentication();
             if(authentication != null  ) {
                try{
                    OAuthIdpTokenResponseDTO details = (OAuthIdpTokenResponseDTO) authentication.getDetails();
                    authorizationHeader = String.format("%s %s", BEARER_TOKEN_TYPE, details.getAccessToken());
                    request.getHeaders().add("Authorization", authorizationHeader);
                }catch(ClassCastException e){
                    log.error(e.getMessage());
                }
             }
        }

        return execution.execute(request, body);
    }
}
