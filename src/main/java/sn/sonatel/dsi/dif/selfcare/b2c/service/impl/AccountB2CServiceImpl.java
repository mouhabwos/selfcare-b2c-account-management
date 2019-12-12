package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.RattachementLigneRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.*;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AccountB2CDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.UserInfoOuvertureCompte;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice.SelfcareUAAService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.*;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.FormatNumberPhoneUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.CheckNumberRequest;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.NumberRequest;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Service class for managing users accountB2C.
 */
@Service
public class AccountB2CServiceImpl implements AccountB2CService {

    private final Logger log = LoggerFactory.getLogger(AccountB2CServiceImpl.class);


    private final AccountB2CRepository accountB2CRepository;

    private final RattachementLigneRepository rattachementLigneRepository;

    private final SelfcareUAAService selfcareUAAService;

    private final MailService mailService;

    private final DowloadManager dowloadManager;

    private final SponseeService sponseeService;

    private final BoosterManager boosterManager;

    private final ValidationHmacService validationHmacService;


    public AccountB2CServiceImpl(AccountB2CRepository accountB2CRepository, RattachementLigneRepository rattachementLigneRepository, SelfcareUAAService selfcareUAAService, MailService mailService, DowloadManager dowloadManager, SponseeService sponseeService, BoosterManager boosterManager, ValidationHmacService validationHmacService) {
        this.accountB2CRepository = accountB2CRepository;
        this.rattachementLigneRepository = rattachementLigneRepository;
        this.selfcareUAAService = selfcareUAAService;
        this.mailService = mailService;
        this.dowloadManager = dowloadManager;
        this.sponseeService = sponseeService;
        this.boosterManager=boosterManager;
        this.validationHmacService = validationHmacService;
    }

    @Override
    public AccountB2C createAccountB2C(AccountB2CDTO accountB2C){
        log.debug("Service for save AccountB2C : {}", accountB2C);

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

        log.debug("Service for register AccountB2C : {}", managedUserVM);
        return register(managedUserVM);
    }

    @Override
    public AccountB2C registerAccountB2CV2(ManagedUserVM managedUserVM){

        managedUserVM.setLogin(FormatNumberPhoneUtil.extractNumberWithoutSuffix(managedUserVM.getLogin()));


        log.debug("Service for register AccountB2C : {}", managedUserVM);
        boolean validateHmac = validationHmacService.validateHmac(managedUserVM.getHmac(), managedUserVM.getLogin(), managedUserVM.getUuid());

        if(!validateHmac){
            throw new BadRequestAlertException("Hmac non valide","","InvalidHmac");
        }

        return register(managedUserVM);
    }


    @Override
    public AccountB2C updateAccountB2C(@Valid @RequestBody AccountB2CDTO accountB2C){
        log.debug("Service to update AccountB2C : {}", accountB2C);

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
    public Page<AccountB2C> getAllAccountB2C(Pageable pageable) {
        log.debug("Service to get a page of AccountB2CS");
        return accountB2CRepository.findAll(pageable);

    }

    @Override
    public Optional<AccountB2C> getAccountB2C(Long id) {
        log.debug("Service to get AccountB2C : {}", id);
        return accountB2CRepository.findById(id);

    }

    @Override
    public ResponseEntity checkNumber(NumberRequest numberRequest) {

        log.debug("Service to check number of AccountB2C : {}", numberRequest);
        numberRequest.setMsisdn(FormatNumberPhoneUtil.extractNumberWithoutSuffix(numberRequest.getMsisdn()));


        checkExistingAccountForNumber(numberRequest.getMsisdn());

        return ResponseEntity.ok().build();
    }


    @Override
    public boolean emailExistingVerify(String email) {

        log.debug("Service to verify the status of email : {}", email);
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

        log.debug("Service to get AccountB2C by MSISDN : {}", login);
        if(login.matches(Constants.LOGIN_REGEX_VALID_NUMBER)){

            login = FormatNumberPhoneUtil.extractNumberWithoutSuffix(login);

            Optional<AccountB2C> account = accountB2CRepository.findOneByNumero(login);
            if(account.isPresent()){

                return account.get();

            }else {

                log.debug("Error number is already used  : {}", login);
                throw new LigneNotFoundException();
            }

        }else {

            log.debug("Error number is not valid   : {}", login);
            throw new LigneNotFoundException();
        }

    }

    @Override
    public void sendmail(UserInfoOuvertureCompte b2C) {

        log.debug("Service to get create an account : {}", b2C);
        b2C = dowloadManager.addResources(b2C);
        mailService.sendEmailToServiceClient(b2C);

    }

    @Override
    public void checkNumberV2(CheckNumberRequest checkNumberRequest) {

        log.debug("Service for check number version 2 for : {}", checkNumberRequest);
        checkNumberRequest.setMsisdn(FormatNumberPhoneUtil.extractNumberWithoutSuffix(checkNumberRequest.getMsisdn()));

        boolean validateHmac = validationHmacService.validateHmac(checkNumberRequest.getHmac(),checkNumberRequest.getMsisdn(),checkNumberRequest.getUuid());

        if(!validateHmac){
            throw new BadRequestAlertException("Hmac non valide","","InvalidHmac");
        }
        checkExistingAccountForNumber(checkNumberRequest.getMsisdn());
    }


    @Override
    public void updateTutorialView(String msisdn) {

        log.debug("Service for update tutorialView in AccountB2C by : {}", msisdn);
        Optional<AccountB2C> accountB2C = accountB2CRepository.findOneByNumero(msisdn);

        if(accountB2C.isPresent()){
            accountB2C.get().setTutoViewed(true);
            accountB2CRepository.save(accountB2C.get());
        }else {
            log.debug("Error number is not found in AccountB2C  : {}", msisdn);
            throw  new LigneNotFoundException();
        }

    }


    private void checkExistingAccountForNumber(String msisdn){

         msisdn = FormatNumberPhoneUtil.extractNumberWithoutSuffix(msisdn);

        Optional<AccountB2C> accountB2C = accountB2CRepository.findOneByNumero(msisdn);

        Optional<RattachementLigne> ligne = rattachementLigneRepository.findByNumero(msisdn);

        if(accountB2C.isPresent()){
            log.debug ( "Error this login is already used  : {}", msisdn);
            throw new LoginAlreadyUsedException();
        }

        if(ligne.isPresent()){
            log.debug ( "Error this login is already rattached  : {}", msisdn );
            throw new LigneAlreadyRattachedException();
        }
    }

    private AccountB2C register(ManagedUserVM managedUserVM){

        checkExistingAccountForNumber(managedUserVM.getLogin());


        if(managedUserVM.getEmail() == null){

            managedUserVM.setEmail(Constants.EMAIL_PART1+managedUserVM.getLogin()+Constants.EMAIL_PART2);
        }

        try {
            ResponseEntity response = selfcareUAAService.regiserAccount(managedUserVM);

            if (response.getStatusCode() == HttpStatus.CREATED) {

                managedUserVM.setActivated(true);

                AccountB2C result =  new AccountB2C();

                result.setNumero(managedUserVM.getLogin());
                result.setFirstName(managedUserVM.getFirstName());
                result.setLastName(managedUserVM.getLastName());
                result.setImageProfil(managedUserVM.getImageprofil());
                result.setEmail(managedUserVM.getEmail());
                result = accountB2CRepository.save(result);
                mailService.sendActivationEmail(result);

                this.boosterManager.applyWelcomeBooster(result.getNumero());
                sponseeService.updateEffectiveInscriptionOfSponsee(result.getNumero());

                return result;
            }

        }catch (HttpClientErrorException e){

            log.debug(" exception for creation Account : {}", e);
        }

        throw new UserNoCreatedException();
    }

}
