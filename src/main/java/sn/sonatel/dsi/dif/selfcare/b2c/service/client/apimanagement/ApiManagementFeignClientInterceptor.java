package sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class ApiManagementFeignClientInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String BEARER_TOKEN_TYPE = "Bearer";

    private final ApiManagementAccessTokenRetrieveService tokenRetrieveService;

    ApiManagementFeignClientInterceptor(ApiManagementAccessTokenRetrieveService tokenRetrieveService) {
        this.tokenRetrieveService = tokenRetrieveService;
    }

    @Override
    public void apply(RequestTemplate template) {
        template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, tokenRetrieveService.retrieveToken()));
    }
}
