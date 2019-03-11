package sn.sonatel.dsi.dif.selfcare.b2c.service.servicesCall;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;

public class ServiceSelfcareUAA {

    public static void regiserAccount(RestTemplate restTemplate, HttpEntity<ManagedUserVM> request){

            restTemplate
                .exchange(Constants.SELFCARE_UAA_SERVICE+""+Constants.REGISTER_ACCOUNT, HttpMethod.POST, request, ManagedUserVM.class);

    }
}
