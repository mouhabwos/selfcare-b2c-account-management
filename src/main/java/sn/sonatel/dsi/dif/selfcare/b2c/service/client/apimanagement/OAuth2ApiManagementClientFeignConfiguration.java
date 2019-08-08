package sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class OAuth2ApiManagementClientFeignConfiguration {

    @Bean(name = "apiManagementFeignClientInterceptor")
    public RequestInterceptor getUserFeignClientInterceptor(ApiManagementAccessTokenRetrieveService tokenRetrieveService) {
        return new ApiManagementFeignClientInterceptor (tokenRetrieveService);
    }
}
