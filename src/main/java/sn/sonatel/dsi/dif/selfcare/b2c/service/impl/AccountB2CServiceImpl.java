package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.RattachementLigneRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.AccountB2CService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.DowloadManager;
import sn.sonatel.dsi.dif.selfcare.b2c.service.MailService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AccountB2CDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.UserInfoOuvertureCompte;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServiceSelfcareUAA;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.*;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.FormatNumberPhoneUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Service class for managing users accountB2C.
 */
@Service
public class AccountB2CServiceImpl implements AccountB2CService {

    private final Logger log = LoggerFactory.getLogger(AccountB2CServiceImpl.class);

    private static final String ENTITY_NAME = "selfcareB2CAccountManagementAccountB2CServiceImpl";

    private final AccountB2CRepository accountB2CRepository;

    private final RattachementLigneRepository rattachementLigneRepository;


    private final RestTemplate restTemplate;

    private final MailService mailService;

    private final DowloadManager dowloadManager;

    public AccountB2CServiceImpl(AccountB2CRepository accountB2CRepository, RattachementLigneRepository rattachementLigneRepository,@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate, MailService mailService, DowloadManager dowloadManager) {
        this.accountB2CRepository = accountB2CRepository;
        this.rattachementLigneRepository = rattachementLigneRepository;
        this.restTemplate = restTemplate;
        this.mailService = mailService;
        this.dowloadManager = dowloadManager;
    }

    @Override
    public AccountB2C createAccountB2C(AccountB2CDTO accountB2C){
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
        return accountB2CRepository.save(b2C);

    }

    @Override
    public AccountB2C registerAccountB2C(ManagedUserVM managedUserVM){

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

        return result;
    }


    @Override
    public AccountB2C updateAccountB2C(@Valid @RequestBody AccountB2CDTO accountB2C){
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
        return accountB2CRepository.save(b2C);
    }

    @Override
    public Page<AccountB2C> getAllAccountB2CS(Pageable pageable) {
        log.debug("REST request to get a page of AccountB2CS");
        return accountB2CRepository.findAll(pageable);

    }

    @Override
    public Optional<AccountB2C> getAccountB2C(Long id) {
        log.debug("REST request to get AccountB2C : {}", id);
        return accountB2CRepository.findById(id);

    }

    @Override
    public ResponseEntity checkNumber(String msisdn) {
        msisdn = FormatNumberPhoneUtil.getNumberFormat(msisdn);

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


    @Override
    public boolean emailExistingVerify(String email) {

        if (email != null) {

            Optional<AccountB2C> user = accountB2CRepository.findOneByEmail(email);

            if (user.isPresent()) {
                return true;
            }
        }
        return false;
    }


    @Override
    public AccountB2C getAccount(String login) {

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

    @Override
    public void sendmail(UserInfoOuvertureCompte b2C) {

        b2C = dowloadManager.addResources(b2C);
        mailService.sendEmailToServiceClient(b2C);

    }

















    @Override
    public void tutorialView(String msisdn) {

        Optional<AccountB2C> accountB2C = accountB2CRepository.findOneByNumero(msisdn);

        if(accountB2C.isPresent()){
            accountB2C.get().setTutoViewed(true);
            accountB2CRepository.save(accountB2C.get());
        }else {
            throw  new LigneNotFoundException();
        }

    }
}
