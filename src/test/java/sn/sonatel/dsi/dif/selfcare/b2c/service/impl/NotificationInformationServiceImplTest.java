package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.config.SecurityBeanOverrideConfiguration;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.NotificationInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.OfferTypeEnum;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.NotificationInformationRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement.CustomerOfferService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.NotificationInformationDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.mapper.NotificationInformationMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SelfcareB2CApp.class})
public class NotificationInformationServiceImplTest {

    @Autowired
    private NotificationInformationRepository mockNotificationInformationRepository;
    @Autowired
    private NotificationInformationMapper mockNotificationInformationMapper;
    @Autowired
    private AccountB2CRepository accountB2CRepository;
    @Mock
    private CustomerOfferService mockCustomerOfferService;

    private NotificationInformationServiceImpl notificationInformationServiceImplUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        notificationInformationServiceImplUnderTest = new NotificationInformationServiceImpl(mockNotificationInformationRepository, mockNotificationInformationMapper, accountB2CRepository, mockCustomerOfferService);
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
        when(mockCustomerOfferService.getCustomerOffer(anyString())).thenReturn(customerOffer);

        // Run the test
        notificationInformationServiceImplUnderTest.addCodeFormuleCustomerOffer(accountB2C.getNumero());

        //verify that the add method was called with argument 'some string'
        Mockito.verify(mockCustomerOfferService).getCustomerOffer(accountB2C.getNumero());
    }
}
