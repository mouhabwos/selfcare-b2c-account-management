package sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.OfferTypeEnum;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.CustomerOfferApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.NotFoundNumberException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ApiManagementServiceTest {

    @Mock
    private CustomerOfferApiClient mockCustomerOfferApiClient;

    private CustomerOfferService customerOfferServiceUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        customerOfferServiceUnderTest = new CustomerOfferService(mockCustomerOfferApiClient);
    }

    @Test
    public void testGetCustomerOffer() {

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
        final CustomerOffer result = customerOfferServiceUnderTest.getCustomerOffer(customerOffer.getEndUserId());

        // Verify the results
        assertEquals(customerOffer, result);
    }


    @Test(expected = NotFoundNumberException.class)
    public void testGetCustomerOfferNotFoundException() {

        ResponseEntity<CustomerOffer> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        when(mockCustomerOfferApiClient.getCustomerOffer(anyString())).thenReturn(response);

        customerOfferServiceUnderTest.getCustomerOffer("test");

    }
}
