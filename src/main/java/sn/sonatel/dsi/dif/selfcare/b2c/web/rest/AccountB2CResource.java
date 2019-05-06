package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.sonatel.dsi.dif.selfcare.b2c.aop.logging.annotation.Auditable;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.service.AccountB2CService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AccountB2CDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.EmailExistDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.UserInfoOuvertureCompte;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.HeaderUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Message;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.PaginationUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;


/**
 * REST controller for managing AccountB2C.
 */
@RestController
@RequestMapping("/api/account-management")
public class AccountB2CResource {

    private final Logger log = LoggerFactory.getLogger(AccountB2CResource.class);

    private static final String ENTITY_NAME = "selfcareB2CAccountManagementAccountB2C";

    private final AccountB2CService accountB2CService;

    public AccountB2CResource(AccountB2CService accountB2CService) {

        this.accountB2CService = accountB2CService;
    }


    @Auditable(description = Message.Account.ADD)
    @PostMapping("/account-b-2-cs")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AccountB2C> createAccountB2C(@Valid @RequestBody AccountB2CDTO accountB2CDto) throws URISyntaxException {
        log.debug("REST request to save AccountB2C : {}", accountB2CDto);
        AccountB2C result = accountB2CService.createAccountB2C(accountB2CDto);

        return ResponseEntity.created(new URI("/api/account-b-2-cs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    //TO-DO IMPLEMENT CAPTCHA
    @Auditable(description = Message.Account.ADD)
    @PostMapping("/register")
    public ResponseEntity<AccountB2C> registerAccountB2C(@Valid @RequestBody ManagedUserVM managedUserVM) throws URISyntaxException {

        AccountB2C result = accountB2CService.registerAccountB2C(managedUserVM);

        return ResponseEntity.created(new URI("/api/account-b-2-cs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    @Auditable(description = Message.Account.UPDATE)
    @PutMapping("/account-b-2-cs")
    @PreAuthorize("#accountB2C.numero == authentication.name")
    public ResponseEntity<AccountB2C> updateAccountB2C(@Valid @RequestBody AccountB2CDTO accountB2C) throws URISyntaxException {
        log.debug("REST request to update AccountB2C : {}", accountB2C);
        AccountB2C result = accountB2CService.updateAccountB2C(accountB2C);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountB2C.getId().toString()))
            .body(result);
    }


    @Auditable(description = Message.Account.LIST)
    @GetMapping("/account-b-2-cs")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<AccountB2C>> getAllAccountB2CS(Pageable pageable) {
        log.debug("REST request to get a page of AccountB2CS");
        Page<AccountB2C> page = accountB2CService.getAllAccountB2CS(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/account-b-2-cs");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    @Auditable(description = Message.Account.LIST_BY_ID)
    @GetMapping("/account-b-2-cs/{id}")
    public ResponseEntity<AccountB2C> getAccountB2C(@PathVariable Long id) {
        log.debug("REST request to get AccountB2C : {}", id);
        Optional<AccountB2C> accountB2C = accountB2CService.getAccountB2C(id);
        return ResponseUtil.wrapOrNotFound(accountB2C);
    }



    @Auditable(description = Message.Account.CHECK_Numero)
    @GetMapping("/check_number/{msisdn}")
    public ResponseEntity checkNumber(@PathVariable String msisdn) {
        log.debug("REST request to check number AccountB2C : {}", msisdn);
        return accountB2CService.checkNumber(msisdn);
    }

    @Auditable(description = Message.Account.CHECK_Email)
    @PostMapping("/email-already-exist")
    @Timed
    public boolean emailExistingVerify(@Valid @RequestBody EmailExistDTO email) {
        log.debug("REST request to check email AccountB2C : {}", email);
        return accountB2CService.emailExistingVerify(email.getEmail());
    }


    @Auditable(description = Message.Account.Authent)
    @GetMapping("/account/{login}")
    @Timed
    @PreAuthorize("#login == authentication.name")
    public AccountB2C getAccount(@PathVariable String login) {
        log.debug("REST request to get AccountB2C : {}", login);
        return accountB2CService.getAccount(login);

    }

    @PostMapping("/mail/ouverture-compte")
    @PreAuthorize("#b2C.numero== authentication.name")
    public void sendmail(@Valid @RequestBody UserInfoOuvertureCompte b2C) {
        log.debug("REST request to register ouverture-compte : {}", b2C);
        accountB2CService.sendmail(b2C);

    }

    /**
     * GET / view-tutorial : change the status of user for view tutorial
     * @param msisdn
     * @return Response ok if
     */
    @GetMapping("/view-tutorial/{msisdn}")
    @Timed
    public ResponseEntity<String> tutorialView(@PathVariable String msisdn){
        log.debug("REST request to update field turorialView AccountB2C : {}", msisdn);
        accountB2CService.tutorialView(msisdn);

        return ResponseEntity.ok().build();
    }



}
