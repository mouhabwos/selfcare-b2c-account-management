package sn.sonatel.dsi.dif.selfcare.b2c.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.booster.BoosterClient;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SelfcareB2CApp.class})
public class AsyncServiceTest {

    @Mock
    private BoosterClient boosterClient;

    @Mock
    private NotificationInformationService notificationInformationService;

    @Mock
    private AbonneService abonneService;

    @Mock
    private AccountB2CRepository accountB2CRepository;

    private AsyncService asyncService;


    @Before
    public void setUp() {
        initMocks(this);
        asyncService = new AsyncService(boosterClient, notificationInformationService, abonneService, accountB2CRepository);
    }

    @Test
    public void applyWelcomeBooster() {
        asyncService.applyWelcomeBooster("782363572");
        Mockito.verify(boosterClient).applyBooster("782363572", null, "FORM_INSCRIPTION", null);
    }

    @Test
    public void addCodeFormuleInformationNotification(){

        asyncService.addCodeFormuleInformationNotification("782363572");
        Mockito.verify(notificationInformationService).addCodeFormuleCustomerOffer("782363572");
    }

    @Test
    public void updateFirstnameAndLasname(){

        AccountB2C b2C = new AccountB2C();
        b2C.setNumero("782363572");
        b2C.setFirstName("");
        b2C.setLastName("");
        AbonneDTO abonneDTO = new AbonneDTO();
        abonneDTO.setNomAbonne("nom");
        abonneDTO.setPrenomAbonne("");
        Mockito.when(abonneService.getInformationAbonne(b2C.getNumero())).thenReturn(abonneDTO);

        asyncService.updateFirstnameAndLasname(b2C);

        Mockito.verify(abonneService).getInformationAbonne(b2C.getNumero());
    }
}
