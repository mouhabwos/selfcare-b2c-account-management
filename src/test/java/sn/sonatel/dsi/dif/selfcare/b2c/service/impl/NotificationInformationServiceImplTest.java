package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import sn.sonatel.dsi.dif.selfcare.b2c.IntegrationTest;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.NotificationInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.OfferTypeEnum;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.NotificationInformationRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.CustomerOfferApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement.CustomerOfferService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;
import sn.sonatel.dsi.dif.selfcare.b2c.service.mapper.NotificationInformationMapper;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@IntegrationTest
public class NotificationInformationServiceImplTest {

    @Autowired
    private NotificationInformationRepository notificationInformationRepository;
    @Autowired
    private NotificationInformationMapper mockNotificationInformationMapper;
    @Autowired
    private AccountB2CRepository accountB2CRepository;
    @Mock
    private CustomerOfferService mockCustomerOfferService;

    @Mock
    private CustomerOfferApiClient customerOfferApiClient;

    @Mock
    private CustomerOfferService customerOfferService;

    private NotificationInformationServiceImpl notificationInformationServiceImplUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        notificationInformationServiceImplUnderTest = new NotificationInformationServiceImpl(notificationInformationRepository, mockNotificationInformationMapper, accountB2CRepository, customerOfferApiClient, customerOfferService);
    }



    @Test
    public void testAddCodeFormuleCustomerOffer() {
        // Setup

        //save account
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setLastName("hello");
        accountB2C.setFirstName("hello");
        accountB2C.setNumero("770010195");
        accountB2C.setEmail("test785015@gmail.com");
        accountB2C = accountB2CRepository.save(accountB2C);

        // Configure CustomerOfferService.getCustomerOffer(...).
         CustomerOffer customerOffer = new CustomerOffer();
        customerOffer.setClientCode("clientCode");
        customerOffer.setCreateDate("createDate");
        customerOffer.setOfferType(OfferTypeEnum.HYBRIDE);
        customerOffer.setOfferId("9331");
        ResponseEntity<CustomerOffer> response = ResponseEntity.status(HttpStatus.OK).body(customerOffer);
        when(customerOfferApiClient.getCustomerOffer(Mockito.anyString())).thenReturn(response);

        // Run the test
        notificationInformationServiceImplUnderTest.addCodeFormuleCustomerOffer(accountB2C.getNumero());

        //verify that the add method was called with argument 'some string'
        Mockito.verify(customerOfferApiClient).getCustomerOffer(accountB2C.getNumero());
    }



    @Test
    public void testUpdateCodeFormuleCustomerOffer() {
        // Setup

        //save account
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setLastName("hello");
        accountB2C.setFirstName("hello");
        accountB2C.setNumero("770010185");
        accountB2C.setEmail("test785065@gmail.com");
        accountB2C = accountB2CRepository.save(accountB2C);

        // Configure CustomerOfferService.getCustomerOffer(...).
        CustomerOffer customerOffer = new CustomerOffer();
        customerOffer.setClientCode("clientCode");
        customerOffer.setCreateDate("createDate");
        customerOffer.setOfferType(OfferTypeEnum.PREPAID);
        customerOffer.setOfferId("9331");
        ResponseEntity<CustomerOffer> response = ResponseEntity.status(HttpStatus.OK).body(customerOffer);
        when(customerOfferApiClient.getCustomerOffer(Mockito.anyString())).thenReturn(response);

        // Run the test
        notificationInformationServiceImplUnderTest.updateNotificationInformationFormulCode(accountB2C.getNumero());

        //verify that the add method was called with argument 'some string'
        Mockito.verify(customerOfferApiClient).getCustomerOffer(accountB2C.getNumero());
    }

    @Test
    public void testAddCodeFormuleCustomerOfferNotFound() {
        // Setup

        //save account
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setLastName("hello");
        accountB2C.setFirstName("hello");
        accountB2C.setNumero("770020195");
        accountB2C.setEmail("test785025@gmail.com");
        accountB2C = accountB2CRepository.save(accountB2C);

        // Configure CustomerOfferService.getCustomerOffer(...).
        ResponseEntity<CustomerOffer> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        when(customerOfferApiClient.getCustomerOffer(Mockito.anyString())).thenReturn(response);

        // Run the test
        notificationInformationServiceImplUnderTest.addCodeFormuleCustomerOffer(accountB2C.getNumero());

        //verify that the add method was called with argument 'some string'
        Mockito.verify(customerOfferApiClient).getCustomerOffer(accountB2C.getNumero());
    }


    @Test
    public void testUpdateCodeFormuleCustomer() {
        // Setup

        //save account
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setLastName("hello");
        accountB2C.setFirstName("hello");
        accountB2C.setNumero("770010185");
        accountB2C.setEmail("test785065@gmail.com");
        accountB2C = accountB2CRepository.save(accountB2C);

        // Configure CustomerOfferService.getCustomerOffer(...).
        CustomerOffer customerOffer = new CustomerOffer();
        customerOffer.setClientCode("clientCode");
        customerOffer.setCreateDate("createDate");
        customerOffer.setOfferType(OfferTypeEnum.PREPAID);
        customerOffer.setOfferId("9339");
        ResponseEntity<CustomerOffer> response = ResponseEntity.status(HttpStatus.OK).body(customerOffer);
        when(customerOfferApiClient.getCustomerOffer(Mockito.anyString())).thenReturn(response);

        //Save Notification information

        NotificationInformation information = new NotificationInformation();
        information.setCodeFormule("7878");
        information.setFirebaseId("000001");
        information.setAccountB2C(accountB2C);

        notificationInformationRepository.save(information);

        // Run the test
        notificationInformationServiceImplUnderTest.updateNotificationInformationFormulCode(accountB2C.getNumero());

        //verify that the add method was called with argument 'some string'
        Mockito.verify(customerOfferApiClient).getCustomerOffer(accountB2C.getNumero());
    }
}
