package sn.sonatel.dsi.dif.selfcare.b2c.cucumber.stepdefs;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = SelfcareB2CApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
