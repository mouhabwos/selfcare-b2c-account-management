package sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.CustomerOfferApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.NotFoundNumberException;

@Service
public class CustomerOfferService {

    private final Logger log = LoggerFactory.getLogger ( CustomerOfferService.class );


    private final CustomerOfferApiClient customerOfferApiClient;

    public CustomerOfferService(CustomerOfferApiClient customerOfferApiClient) {
        this.customerOfferApiClient = customerOfferApiClient;
    }


    public CustomerOffer getCustomerOffer(String msisdn){
        log.debug ( "Service for get Customer Offer for client {}", msisdn );
        ResponseEntity<CustomerOffer> responseEntity = customerOfferApiClient.getCustomerOffer(msisdn);

        if(responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null){
           return responseEntity.getBody();
        } else throw new NotFoundNumberException(msisdn);
    }
}
