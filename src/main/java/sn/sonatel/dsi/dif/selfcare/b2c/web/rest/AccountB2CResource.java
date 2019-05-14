package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.aop.logging.annotation.Auditable;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.RattachementLigneRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.AccountB2CService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.CaptchaService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.DowloadManager;
import sn.sonatel.dsi.dif.selfcare.b2c.service.MailService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AccountB2CDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.EmailExistDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.UserInfoOuvertureCompte;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServiceSelfcareUAA;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.*;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.FormatNumberPhoneUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.HeaderUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Message;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.PaginationUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.NumberRequest;

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

    private  final CaptchaService captchaService;

    private final AccountB2CRepository accountB2CRepository;

    private final RattachementLigneRepository rattachementLigneRepository;

    @Qualifier("loadBalancedRestTemplate")
    private final RestTemplate restTemplate;

    private final MailService mailService;

    private final DowloadManager dowloadManager;

    private final AccountB2CService accountB2CService;

    public AccountB2CResource(AccountB2CRepository accountB2CRepository,CaptchaService captchaService, RattachementLigneRepository rattachementLigneRepository, @Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate, MailService mailService, DowloadManager dowloadManager, AccountB2CService accountB2CService) {
        this.accountB2CRepository = accountB2CRepository;
        this.captchaService = captchaService;
        this.rattachementLigneRepository = rattachementLigneRepository;
        this.restTemplate = restTemplate;
        this.mailService = mailService;
        this.dowloadManager = dowloadManager;
        this.accountB2CService = accountB2CService;
    }


    @Auditable(description = Message.Account.ADD)
    @PostMapping("/account-b-2-cs")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AccountB2C> createAccountB2C(@Valid @RequestBody AccountB2CDTO accountB2C) throws URISyntaxException {
        log.debug("REST request to save AccountB2C : {}", accountB2C);
        if (accountB2C.getId() != null) {
            throw new BadRequestAlertException("A new accountB2C cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountB2C b2C = new AccountB2C();

        b2C.setNumero(accountB2C.getNumero());

        b2C.setEmail(accountB2C.getEmail());

        b2C.setFirstName(accountB2C.getFirstName());
        b2C.setLastName(accountB2C.getLastName());
        b2C.setImageProfil(accountB2C.getImageProfil());
        b2C.setAttempts(accountB2C.getAttempts());
        b2C.setDerniereConnnexionDate(accountB2C.getDerniereConnnexionDate());
        AccountB2C result = accountB2CRepository.save(b2C);

        return ResponseEntity.created(new URI("/api/account-b-2-cs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    //TO-DO IMPLEMENT CAPTCHA
    @Auditable(description = Message.Account.ADD)
    @PostMapping("/register")
    public ResponseEntity<AccountB2C> registerAccountB2C(@Valid @RequestBody ManagedUserVM managedUserVM) throws URISyntaxException {

        Optional<AccountB2C> accountB2C = accountB2CRepository.findOneByNumero(managedUserVM.getLogin());
        Optional<RattachementLigne> ligne = rattachementLigneRepository.findByNumero(managedUserVM.getLogin());

        if(accountB2C.isPresent()){
            throw new LoginAlreadyUsedException();
        }

        if(ligne.isPresent()){
            throw new LigneAlreadyRattachedException();

        }

        AccountB2C result =  new AccountB2C();
        try {

            HttpEntity<ManagedUserVM> request = new HttpEntity<>(managedUserVM);
            if(managedUserVM.getEmail() == null){

                managedUserVM.setEmail("selfcare-b2c-"+managedUserVM.getLogin()+"@selfcare.com");

            }
            ResponseEntity response = ServiceSelfcareUAA.regiserAccount(restTemplate, request);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                managedUserVM.setActivated(true);
                AccountB2C account =  new AccountB2C();
                account.setNumero(managedUserVM.getLogin());

                account.setFirstName(managedUserVM.getFirstName());
                account.setLastName(managedUserVM.getLastName());
                account.setImageProfil(managedUserVM.getImageprofil());
                account.setEmail(managedUserVM.getEmail());
                result = accountB2CRepository.save(account);
                mailService.sendActivationEmail(result);
            }
            else {
                throw new UserNoCreatedException();
            }

        }catch (Exception e){

            log.debug(" exception for creation Account : {}", e);
        }

        return ResponseEntity.created(new URI("/api/account-b-2-cs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    @Auditable(description = Message.Account.UPDATE)
    @PutMapping("/account-b-2-cs")
    @PreAuthorize("#accountB2C.numero == authentication.name")
    public ResponseEntity<AccountB2C> updateAccountB2C(@Valid @RequestBody AccountB2CDTO accountB2C) throws URISyntaxException {
        log.debug("REST request to update AccountB2C : {}", accountB2C);
        if (accountB2C.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AccountB2C b2C = new AccountB2C();
        b2C.setId(accountB2C.getId());
        b2C.setNumero(accountB2C.getNumero());
        b2C.setEmail(accountB2C.getEmail());
        b2C.setFirstName(accountB2C.getFirstName());
        b2C.setLastName(accountB2C.getLastName());
        b2C.setImageProfil(accountB2C.getImageProfil());
        b2C.setAttempts(accountB2C.getAttempts());
        b2C.setDerniereConnnexionDate(accountB2C.getDerniereConnnexionDate());
        AccountB2C result = accountB2CRepository.save(b2C);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountB2C.getId().toString()))
            .body(result);
    }


    @Auditable(description = Message.Account.LIST)
    @GetMapping("/account-b-2-cs")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<AccountB2C>> getAllAccountB2CS(Pageable pageable) {
        log.debug("REST request to get a page of AccountB2CS");
        Page<AccountB2C> page = accountB2CRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/account-b-2-cs");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


/*    @Auditable(description = Message.Account.LIST_BY_ID)
    @GetMapping("/account-b-2-cs/{id}")
    public ResponseEntity<AccountB2C> getAccountB2C(@PathVariable Long id) {
        log.debug("REST request to get AccountB2C : {}", id);
        Optional<AccountB2C> accountB2C = accountB2CRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(accountB2C);
    }

    @Auditable(description = Message.Account.DELETE)
    @DeleteMapping("/account-b-2-cs/{id}")
    public ResponseEntity<Void> deleteAccountB2C(@PathVariable Long id) {
        log.debug("REST request to delete AccountB2C : {}", id);
        accountB2CRepository.deleteById(id);

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }*/

    @Auditable(description = Message.Account.CHECK_Numero)
    @PostMapping("/check_number")
    public ResponseEntity checkNumber(@Valid @RequestBody NumberRequest numberRequest) {

        if(!captchaService.verifyCaptcha(numberRequest.getToken())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String msisdn = FormatNumberPhoneUtil.getNumberFormat(numberRequest.getMsisdn());

        Optional<AccountB2C> account = accountB2CRepository.findOneByNumero(msisdn);
        if(account.isPresent()){
            throw new LoginAlreadyUsedException();
        }

        Optional<RattachementLigne> ligne = rattachementLigneRepository.findByNumero(msisdn);
        if(ligne.isPresent()){
            throw new LigneAlreadyRattachedException();
        }
        return ResponseEntity.ok().build();
    }

    @Auditable(description = Message.Account.CHECK_Email)
    @PostMapping("/email-already-exist")
    @Timed
    public boolean emailExistingVerify(@Valid @RequestBody EmailExistDTO email) {

        if (email != null) {

            Optional<AccountB2C> user = accountB2CRepository.findOneByEmail(email.getEmail());

            if (user.isPresent()) {
                return true;
            }
        }
        return false;
    }


    @Auditable(description = Message.Account.Authent)
    @GetMapping("/account/{login}")
    @Timed
    @PreAuthorize("#login == authentication.name")
    public AccountB2C getAccount(@PathVariable String login) {

        if(login.matches(Constants.LOGIN_REGEX_VALID_NUMBER)){

            login = FormatNumberPhoneUtil.getNumberFormat(login);

            Optional<AccountB2C> account = accountB2CRepository.findOneByNumero(login);
            if(account.isPresent()){

                return account.get();

            }else {

                throw new LigneNotFoundException();
            }

        }else {

                throw new LigneNotFoundException();
            }


    }

    @PostMapping("/mail/ouverture-compte")
    @PreAuthorize("#b2C.numero== authentication.name")
    public void sendmail(@Valid @RequestBody UserInfoOuvertureCompte b2C) {

        b2C = dowloadManager.addResources(b2C);
          mailService.sendEmailToServiceClient(b2C);

    }

    /**
     * GET / view-tutorial : change the status of user for view tutorial
     * @param msisdn
     * @return Response ok if
     */
    @GetMapping("/view-tutorial/{msisdn}")
    @Timed
    public ResponseEntity<String> tutorialView(@PathVariable String msisdn){

        accountB2CService.tutorialView(msisdn);

        return ResponseEntity.ok().build();
    }



}
