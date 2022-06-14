package sn.sonatel.dsi.dif.selfcare.b2c.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.booster.BoosterClient;

@RunWith(SpringRunner.class)
public class BoosterManagerTest {

    BoosterManager boosterManager;

    @Mock
    BoosterClient boosterClient;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        boosterManager = new BoosterManager(boosterClient);
    }

    @Test
    public void applyWelcomeBooster(){
        boosterManager.applyWelcomeBooster("777777777");
    }

}
