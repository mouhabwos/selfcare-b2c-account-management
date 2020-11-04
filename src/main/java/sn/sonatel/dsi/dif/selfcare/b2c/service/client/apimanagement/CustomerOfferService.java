package sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.client.customeroffer.CustomerOfferRetrieveService;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.OfferTypeEnum;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.ProfilType;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.NotFoundNumberException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.ServiceUnavailableException;

@Service
public class CustomerOfferService {

    private final Logger log = LoggerFactory.getLogger ( CustomerOfferService.class );


    private final CustomerOfferRetrieveService customerOfferRetrieveService;

    public CustomerOfferService(CustomerOfferRetrieveService customerOfferRetrieveService) {
        this.customerOfferRetrieveService = customerOfferRetrieveService;
    }


    public CustomerOffer getCustomerOffer(String msisdn){
        log.debug ( "Service for get Customer Offer for client {}", msisdn );
        ResponseEntity<CustomerOffer> responseEntity = customerOfferRetrieveService.getCachedCustomerOffer(msisdn);

        if(responseEntity != null && responseEntity.getBody()!=null){
            if(responseEntity.getStatusCode() == HttpStatus.OK){
                return responseEntity.getBody();
            } else if(responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST){
                throw new NotFoundNumberException(msisdn);
            }
        }
        throw new ServiceUnavailableException("");
    }

    public boolean isPostpaid(String msisdn){

        log.debug ( "Service for check if number is postpaid for client {}", msisdn );
        CustomerOffer customerOffer = getCustomerOffer(msisdn);
        OfferTypeEnum offerType = customerOffer.getOfferType();

        String profile = offerType.toString();

        return profile.equals(ProfilType.POSTPAID.name());
    }
}
