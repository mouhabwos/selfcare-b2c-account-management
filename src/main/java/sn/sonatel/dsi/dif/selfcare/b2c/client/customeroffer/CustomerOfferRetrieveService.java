package sn.sonatel.dsi.dif.selfcare.b2c.client.customeroffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.CustomerOfferApiClient;


@Service
public class CustomerOfferRetrieveService {

    private final Logger log = LoggerFactory.getLogger ( CustomerOfferRetrieveService.class );

    private final CustomerOfferApiClient customerOfferApiClient;


    public CustomerOfferRetrieveService(CustomerOfferApiClient customerOfferApiClient) {
        this.customerOfferApiClient = customerOfferApiClient;
    }


    //@Cacheable(value = API_MANAGEMENT_CUSTOMER_OFFER_NAME, key = "#msisdn", unless="#result.getStatusCode()==200")
    public ResponseEntity getCachedCustomerOffer(String msisdn) {

        log.debug("Retrieving cached api management customer offer for user {} ", msisdn);

        ResponseEntity result = customerOfferApiClient.getCustomerOffer(msisdn);

        if (result!= null && result.getBody()!=null){

            log.debug("Successfully get customer offer from api management");
            return result;

        }else {
            log.debug("Failed to get customer offer  form api management with response {}  ",result);
        }

        return ResponseEntity.notFound().build();
    }

    //@CachePut(value = API_MANAGEMENT_CUSTOMER_OFFER_NAME, key = "#msisdn", unless="#result.getStatusCode()==200")
    public ResponseEntity updateCachedCustomerOffer(String msisdn) {

        log.debug(" Retrieving cached api management customer offer for user {} ", msisdn);

        ResponseEntity result = customerOfferApiClient.getCustomerOffer(msisdn);

        if (result!= null && result.getBody()!=null){

            log.debug("Successfully get customer offer from api management");
            return result;

        }else {
            log.info("  Failed to get customer offer  form api management with response {}  ",result);
        }

        return ResponseEntity.notFound().build();
    }

}
