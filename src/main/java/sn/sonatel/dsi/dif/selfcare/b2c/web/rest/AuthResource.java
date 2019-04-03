package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.sonatel.dsi.dif.selfcare.b2c.exception.AccountB2CException;
import sn.sonatel.dsi.dif.selfcare.b2c.service.LoginAttemptService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.SelfcareUAAService;

@RestController
@RequestMapping("/auth")
public class AuthResource {

    private final Logger log = LoggerFactory.getLogger(AuthResource.class);

    @Qualifier(value = "LoginAttemptServiceImpl")
    @Autowired
    private LoginAttemptService loginAttemptService;

    private final SelfcareUAAService selfcareUAAService;



    public AuthResource(SelfcareUAAService selfcareUAAService) {

        this.selfcareUAAService = selfcareUAAService;
    }


    @GetMapping(value = "/login-succeeded/{username}")
    @Timed
    public void loginSucceeded(@PathVariable String username) throws AccountB2CException {
        log.info("logging loginSucceeded {}", username);
        loginAttemptService.loginSucceeded(username);
    }

    
    @GetMapping(value = "/login-failed/{username}")
    @Timed
    public void loginFailed(@PathVariable String username) throws AccountB2CException {
        log.info("logging loginFailed {}", username);
        loginAttemptService.loginFailed(username);
    }

    @GetMapping(value = "/disable/{username}")
    @Timed
    public void test(@PathVariable String username) throws AccountB2CException {
        log.info("logging loginFailed {}", username);
        selfcareUAAService.disableUser(username);
    }

}
