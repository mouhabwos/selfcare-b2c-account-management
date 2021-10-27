package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.TestVM;

/**
 * Controller for view and managing Log Level at runtime.
 */
@RestController
@RequestMapping("/test")
public class TestResource {

    @Autowired
    ApplicationProperties applicationProperties;

   @Autowired
   @Qualifier("loadBalancedRestTemplate")
   private RestTemplate loadBalancedRestTemplate;

    @Autowired
    @Qualifier("vanillaRestTemplate")
    private RestTemplate vanillaRestTemplate;



    @GetMapping("/loadBalancedRestTemplate")
    @Timed
    public String loadBalancedRestTemplate() {
        String url = applicationProperties.getAuthenticationAuthorisationUserServerHost() + "/test/uaa";

         ResponseEntity responseEntity=null ;

         responseEntity=loadBalancedRestTemplate.getForEntity(url, String.class);

        return (responseEntity!=null && responseEntity.getBody()!=null)? responseEntity.getBody().toString(): "failed";
    }


    @GetMapping("/vanillaRestTemplate")
    @Timed
    public String vanillaRestTemplate() {
        String url = applicationProperties.getAuthenticationAuthorisationUserServerHost() + "/test/uaa";

        ResponseEntity responseEntity = null;

        responseEntity=vanillaRestTemplate.getForEntity(url, String.class);

        return (responseEntity!=null && responseEntity.getBody()!=null)? responseEntity.getBody().toString(): "failed";
    }
}
