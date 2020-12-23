package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.service.AccountB2CService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AccountB2CDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AccountDTOExploitant;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.EmailExistDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice.SelfcareOTPService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.HeaderUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.PaginationUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.CheckNumberRequest;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;

import javax.validation.Valid;
import java.util.List;


/**
 * REST controller for managing AccountB2C.
 */
@RestController
@RequestMapping("/api/account-management")
public class AccountB2CResource {

    private final Logger log = LoggerFactory.getLogger(AccountB2CResource.class);

    private static final String ENTITY_NAME = "selfcareB2CAccountManagementAccountB2C";


    private final AccountB2CService accountB2CService;

    private final SelfcareOTPService otpService;

    private static final String IDNULL = "idnull";

    public AccountB2CResource(AccountB2CService accountB2CService, SelfcareOTPService otpService) {

        this.accountB2CService = accountB2CService;
        this.otpService = otpService;

    }


    //@Auditable(description = Message.Account.ADD)
    @PostMapping("/account-b-2-cs")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AccountB2C> createAccountB2C(@Valid @RequestBody AccountB2CDTO accountB2CDto)  {
        log.debug("REST request to save AccountB2C : {}", accountB2CDto);
        if (accountB2CDto.getId() != null) {
            throw new BadRequestAlertException("A new accountB2C cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountB2C result = accountB2CService.createAccountB2C(accountB2CDto);

        return ResponseEntity.ok(result);
    }


    //@Auditable(description = Message.Account.ADD)
    @PostMapping("/register")
    public ResponseEntity<AccountB2C> registerAccountB2C(@Valid @RequestBody ManagedUserVM managedUserVM) {
        log.debug("REST request to save AccountB2C : {}", managedUserVM);

        if (!otpService.checkRegisterValidity(managedUserVM.getLogin())){
            return ResponseEntity.badRequest().build();
        }
        if (managedUserVM.getId() != null) {
            throw new BadRequestAlertException("Id is not null", ENTITY_NAME, IDNULL);
        }

        AccountB2C result = accountB2CService.registerAccountB2C(managedUserVM);

        return ResponseEntity.ok(result);

    }

    //@Auditable(description = Message.Account.ADD)
    @PostMapping("/v2/register")
    public ResponseEntity<AccountB2C> registerAccountB2CV2(@RequestHeader("X-UUID") String uuid, @Valid @RequestBody ManagedUserVM managedUserVM) {
        log.debug("REST request to save AccountB2C version 2 : {}", managedUserVM);

        if (managedUserVM.getId() != null) {
            throw new BadRequestAlertException("Id is not null", ENTITY_NAME, IDNULL);
        }
        managedUserVM.setUuid(uuid);
        AccountB2C result = accountB2CService.registerAccountB2CV2(managedUserVM);

        return ResponseEntity.ok(result);
    }


    //@Auditable(description = Message.Account.UPDATE)
    @PutMapping("/account-b-2-cs")
    @PreAuthorize("#accountB2C.numero == authentication.name")
    public ResponseEntity<AccountB2C> updateAccountB2C(@Valid @RequestBody AccountB2CDTO accountB2C) {
        log.debug("REST request to update AccountB2C : {}", accountB2C);
        if (accountB2C.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, IDNULL);
        }
        AccountB2C result = accountB2CService.updateAccountB2C(accountB2C);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountB2C.getId().toString()))
            .body(result);
    }


    //@Auditable(description = Message.Account.UPDATE_EXPLOITANT)
    @PutMapping("/account-b-2-cs/{msisdn}")
    @PreAuthorize("hasRole('ROLE_EXPLOITANT')")
    public ResponseEntity updateAccountB2CBySI(@PathVariable("msisdn") String msisdn, @Valid @RequestBody AccountDTOExploitant accountB2C) {
        log.debug("REST request to update AccountB2C by exploitant for msisdn {} : {}",msisdn, accountB2C);

        this.accountB2CService.updateAccountForExploitation(msisdn,accountB2C);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, msisdn))
            .build();
    }


    //@Auditable(description = Message.Account.LIST)
    @GetMapping("/account-b-2-cs")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<AccountB2C>> getAllAccountB2CS(Pageable pageable) {
        log.debug("REST request to get a page of AccountB2CS");
        Page<AccountB2C> page = accountB2CService.getAllAccountB2C(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/account-b-2-cs");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    //@Auditable(description = Message.Account.CHECK_Numero)
    @PostMapping("/v2/check_number")
    public ResponseEntity checkNumberV2(@RequestHeader("X-UUID") String uuid, @Valid @RequestBody CheckNumberRequest checkNumberRequest) {
        checkNumberRequest.setUuid(uuid);
        accountB2CService.checkNumberV2(checkNumberRequest);
        return ResponseEntity.ok().build();
    }

    //@Auditable(description = Message.Account.CHECK_Email)
    @PostMapping("/email-already-exist")
    @Timed
    public boolean emailExistingVerify(@Valid @RequestBody EmailExistDTO email) {
        log.debug("REST request to check email AccountB2C : {}", email);
        return accountB2CService.emailExistingVerify(email.getEmail());
    }


    //@Auditable(description = Message.Account.GET_ACCOUNT)
    @GetMapping("/account/{login}")
    @Timed
    @PreAuthorize("#login == authentication.name")
    public AccountB2C getAccount(@PathVariable String login) {
        log.debug("REST request to get AccountB2C : {}", login);
        return accountB2CService.getAccount(login);

    }


    /**
     * GET / view-tutorial : change the status of user for view tutorial
     * @param msisdn
     * @return Response ok if
     */
    //@Auditable(description = Message.Account.UPDATE_TUTORIAL_VIEW)
    @GetMapping("/view-tutorial/{msisdn}")
    @Timed
    public ResponseEntity<String> tutorialView(@PathVariable String msisdn){
        log.debug("REST request to update field turorialView AccountB2C : {}", msisdn);
        accountB2CService.updateTutorialView(msisdn);
        return ResponseEntity.ok().build();
    }


    /**
     * GET / view-tutorial : check the status of user for view tutorial
     * @param msisdn
     * @return Response the view status
     * @Throws BadRequestException when account not found
     */
    //@Auditable(description = Message.Account.TUTORIAL_VIEW_STATUS)
    @GetMapping("/view-tutorial/status/{msisdn}")
    @Timed
    public ResponseEntity<Boolean> checkTutorialViewStatus(@PathVariable String msisdn){
        log.debug("REST request to check field turorialView  status for AccountB2C : {}", msisdn);
        AccountB2C account = accountB2CService.getAccount(msisdn);
        return ResponseEntity.ok(account.isTutoViewed());
    }


    //@Auditable(description = Message.Account.CHECK_Numero)
    @GetMapping("/v2/check_number/{msisdn}")
    public Boolean checkNumberV2(@PathVariable  String  msisdn) {
        log.debug("REST request to check number : {}", msisdn);
        return accountB2CService.checkNumberV2(msisdn);
    }


}
