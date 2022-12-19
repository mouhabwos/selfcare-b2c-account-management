package sn.sonatel.dsi.dif.selfcare.b2c.client.customeroffer;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.OfferTypeEnum;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.CustomerOfferApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;

@ExtendWith(MockitoExtension.class)
class CustomerOfferRetrieveServiceTest {

    @Mock
    private CustomerOfferApiClient mockCustomerOfferApiClient;

    private CustomerOfferRetrieveService customerOfferRetrieveService;

    @BeforeEach
    void setUp() {
        customerOfferRetrieveService = new CustomerOfferRetrieveService(mockCustomerOfferApiClient);
    }

    @Test
    void testGetCustomerOffer() {
        CustomerOffer customerOffer = new CustomerOffer();
        customerOffer.setClientCode("0012707812");
        customerOffer.setCreateDate("2011-05-26T15:51:10");
        customerOffer.setEndUserId("771326617");
        customerOffer.setOfferCode("9131");
        customerOffer.setOfferType(OfferTypeEnum.PREPAID);
        customerOffer.setOfferStatus("ACTIF");
        customerOffer.setOfferName("Jamono New Scool");

        ResponseEntity<CustomerOffer> response = ResponseEntity.status(HttpStatus.OK).body(customerOffer);

        when(mockCustomerOfferApiClient.getCustomerOffer(anyString())).thenReturn(response);

        // Run the test
        final ResponseEntity<CustomerOffer> result = customerOfferRetrieveService.getCachedCustomerOffer(customerOffer.getEndUserId());

        // Verify the results
        Assertions.assertEquals(customerOffer, result.getBody());
    }

    @Test
    void testGetCustomerOfferWithBadRequest() {
        ResponseEntity<CustomerOffer> response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        when(mockCustomerOfferApiClient.getCustomerOffer(anyString())).thenReturn(response);

        // Run the test
        final ResponseEntity<CustomerOffer> result = customerOfferRetrieveService.getCachedCustomerOffer("test");

        // Verify the results
        Assertions.assertEquals(response.getStatusCode(), result.getStatusCode());
    }

    @Test
    void testGetCustomerOfferWithNotFound() {
        ResponseEntity<CustomerOffer> response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        when(mockCustomerOfferApiClient.getCustomerOffer(anyString())).thenReturn(response);

        // Run the test
        final ResponseEntity<CustomerOffer> result = customerOfferRetrieveService.getCachedCustomerOffer("770000000");

        // Verify the results
        Assertions.assertEquals(response.getStatusCode(), result.getStatusCode());
    }
}
