package sn.sonatel.dsi.dif.selfcare.b2c.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import sn.sonatel.dsi.dif.selfcare.b2c.client.booster.BoosterClient;

@RunWith(SpringRunner.class)
public class BoosterManagerTest {

    BoosterManager boosterManager;

    @Mock
    BoosterClient boosterClient;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        boosterManager = new BoosterManager(boosterClient);
    }

    @Test
    public void applyWelcomeBooster(){
        boosterManager.applyWelcomeBooster("777777777");
    }

}
