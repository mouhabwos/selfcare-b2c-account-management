package sn.sonatel.dsi.dif.selfcare.b2c.service;

import static com.google.inject.matcher.Matchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.booster.BoosterClient;

@RunWith(SpringRunner.class)
class BoosterManagerTest {

    BoosterManager boosterManager;

    @Mock
    BoosterClient boosterClient;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        boosterManager = new BoosterManager(boosterClient);
    }

    @Test
    void applyWelcomeBooster() {
        boosterManager.applyWelcomeBooster("777777777");
        Mockito.verify(boosterClient).applyBooster(eq("777777777"), Mockito.any(), eq("FORM_INSCRIPTION"), Mockito.any());
    }
}
