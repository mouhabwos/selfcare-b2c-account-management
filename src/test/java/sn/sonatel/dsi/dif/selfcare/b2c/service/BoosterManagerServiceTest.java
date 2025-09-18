package sn.sonatel.dsi.dif.selfcare.b2c.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.BoosterTrigger;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.OfferTypeEnum;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement.CustomerOfferService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.booster.BoosterClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.booster.dto.BoosterPromo;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;

class BoosterManagerServiceTest {

    @Mock
    private BoosterClient boosterClient;

    @Mock
    private CustomerOfferService customerOfferService;

    private BoosterManagerService boosterManagerService;

    @BeforeEach
    void setUp() {
        initMocks(this);
        boosterManagerService = new BoosterManagerService(boosterClient, customerOfferService);
    }

    private List<BoosterPromo> getListPromoBooster() {
        BoosterPromo boosterPromo = new BoosterPromo();
        boosterPromo.setId(12L);
        BoosterPromo.Gift gift = new BoosterPromo.Gift();
        gift.setId(45L);
        gift.setValueType(BoosterPromo.Gift.ValueType.AMONT);
        gift.setValue(100.00);
        boosterPromo.setGift(gift);
        List<BoosterPromo> boosterPromos = new ArrayList<>();
        boosterPromos.add(boosterPromo);

        gift.setId(56L);
        gift.setValueType(BoosterPromo.Gift.ValueType.PERCENTAGE);
        gift.setValue(100.00);
        boosterPromo.setGift(gift);
        boosterPromo.setId(90L);

        boosterPromos.add(boosterPromo);
        return boosterPromos;
    }

    private CustomerOffer getCustomerOffer() {
        CustomerOffer customerOffer = new CustomerOffer();
        customerOffer.setClientCode("0012707812");
        customerOffer.setCreateDate("2011-05-26T15:51:10");
        customerOffer.setEndUserId("771326617");
        customerOffer.setOfferCode("9131");
        customerOffer.setOfferType(OfferTypeEnum.PREPAID);
        customerOffer.setOfferStatus("ACTIF");
        customerOffer.setOfferName("Jamono New Scool");

        return customerOffer;
    }

    @Test
    void getActiveWelcomeBoosterValue() {
        CustomerOffer customerOffer = getCustomerOffer();
        when(customerOfferService.getCustomerOffer(Mockito.anyString())).thenReturn(customerOffer);

        List<BoosterPromo> listPromoBooster = getListPromoBooster();
        when(boosterClient.getActiveWelcomeBoosterValue(Mockito.anyString(), anyString(), anyString()))
            .thenReturn(ResponseEntity.ok(listPromoBooster));

        List<BoosterPromo> boosterPromoList = boosterManagerService.getActiveWelcomeBoosterValue(
            "782363572",
            BoosterTrigger.FORM_INSCRIPTION.name()
        );

        Assert.assertEquals(listPromoBooster.size(), boosterPromoList.size());
    }

    @Test
    void getActiveWelcomeBoosterValueNewArrayList() {
        CustomerOffer customerOffer = getCustomerOffer();
        when(customerOfferService.getCustomerOffer(Mockito.anyString())).thenReturn(customerOffer);

        List<BoosterPromo> listPromoBooster = getListPromoBooster();
        when(boosterClient.getActiveWelcomeBoosterValue(Mockito.anyString(), anyString(), anyString()))
            .thenReturn(ResponseEntity.ok().build());

        List<BoosterPromo> boosterPromoList = boosterManagerService.getActiveWelcomeBoosterValue(
            "782363572",
            BoosterTrigger.FORM_INSCRIPTION.name()
        );

        Assert.assertEquals(0, boosterPromoList.size());
    }
}
