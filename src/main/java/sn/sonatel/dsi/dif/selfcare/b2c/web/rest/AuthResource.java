package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.sonatel.dsi.dif.selfcare.b2c.aop.logging.annotation.Auditable;
import sn.sonatel.dsi.dif.selfcare.b2c.exception.AccountB2CException;
import sn.sonatel.dsi.dif.selfcare.b2c.service.impl.LoginAttemptServiceImpl;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Message;


@RestController
@RequestMapping("/api/auth")
public class AuthResource {

    private final Logger log = LoggerFactory.getLogger(AuthResource.class);

    private final LoginAttemptServiceImpl loginAttemptService;

    public AuthResource(LoginAttemptServiceImpl loginAttemptService) {

        this.loginAttemptService = loginAttemptService;

    }

    /**
     *  GET : to init the number of attempts login
     * @param username
     * @throws AccountB2CException
     */
    @Auditable(description = Message.Authentification.CONN_SUCC)
    @GetMapping(value = "/login-succeeded/{username}")
    @Timed
    public ResponseEntity<String> loginSuccess(@PathVariable String username) throws AccountB2CException {
        log.info("logging loginSucceeded {}", username);
        loginAttemptService.loginSucceeded(username);
        return ResponseEntity.ok().build();
    }


    /**
     * GET : to increment the number of attempts login
     * @param username
     * @throws AccountB2CException
     */
    @Auditable(description = Message.Authentification.CONN_ERR)
    @GetMapping(value = "/login-failed/{username}")
    @Timed
    public ResponseEntity loginFailed(@PathVariable String username) throws AccountB2CException {
        log.info("logging loginFailed {}", username);
        int attemps = loginAttemptService.loginFailed(username);
        return ResponseEntity.ok().body(attemps+"");
    }

}
