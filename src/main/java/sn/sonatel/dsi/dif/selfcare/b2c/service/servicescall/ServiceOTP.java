package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.service.vm.MessageVM;

@Service
public class ServiceOTP {

    @Qualifier("loadBalancedRestTemplate")
    private final RestTemplate restTemplate;

    private final ApplicationProperties properties;

    public ServiceOTP(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate, ApplicationProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public void generateMessage(MessageVM messageVM){

        HttpEntity<MessageVM> entity = new HttpEntity<> ( messageVM );

        restTemplate.exchange(properties.getUrlOtp()+""+Constants.URL_SEND_MESSAGE, HttpMethod.POST, entity, String.class);

    }

}
