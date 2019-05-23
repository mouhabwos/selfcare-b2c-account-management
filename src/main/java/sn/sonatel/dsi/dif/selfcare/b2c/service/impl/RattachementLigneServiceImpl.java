package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.RattachementLigneRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.CaptchaService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.RattachementLigneService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.RattachementLigneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice.SelfcareSoapService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.RattachementLigneResource;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.*;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.FormatNumberPhoneUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing users RattachementLigne.
 */
@Service
public class RattachementLigneServiceImpl implements RattachementLigneService {

    private final Logger log = LoggerFactory.getLogger(RattachementLigneResource.class);

    private final RattachementLigneRepository rattachementLigneRepository;

    private final AccountB2CRepository accountB2CRepository;

    private final SelfcareSoapService selfcareSoapService;

    private final CaptchaService captchaService;


    public RattachementLigneServiceImpl(RattachementLigneRepository rattachementLigneRepository, AccountB2CRepository accountB2CRepository, SelfcareSoapService selfcareSoapService, CaptchaService captchaService) {
        this.rattachementLigneRepository = rattachementLigneRepository;

        this.accountB2CRepository = accountB2CRepository;

        this.selfcareSoapService = selfcareSoapService;
        this.captchaService = captchaService;
    }

    @Override
    public RattachementLigne createRattachementLigne(RattachementLigneDTO rattachementLigne) {

        log.debug ( "Service to save RattachementLigne : {}", rattachementLigne );
        RattachementLigne ligne = new RattachementLigne();
        ligne.setTypeNumero(rattachementLigne.getTypeNumero());
        ligne.setAccountB2C(rattachementLigne.getAccountB2C());
        ligne.setNumero(rattachementLigne.getNumero());

        return rattachementLigneRepository.save(ligne);

    }

    @Override
    public RattachementLigne updateRattachementLigne(RattachementLigneDTO rattachementLigne) {

        log.debug ( "Servvice to update RattachementLigne : {}", rattachementLigne );
        RattachementLigne ligne = new RattachementLigne();
        ligne.setId(rattachementLigne.getId());
        ligne.setTypeNumero(rattachementLigne.getTypeNumero());
        ligne.setAccountB2C(rattachementLigne.getAccountB2C());
        ligne.setNumero(rattachementLigne.getNumero());

        return rattachementLigneRepository.save(ligne);

    }

    @Override
    public Page<RattachementLigne> getAllRattachementLignes(Pageable pageable) {
        log.debug("Service to get a page of RattachementLignes");
        return rattachementLigneRepository.findAll(pageable);
    }

    @Override
    public Optional<RattachementLigne> getRattachementLigne(Long id) {
        log.debug("Service to get RattachementLigne : {}", id);
        return rattachementLigneRepository.findById(id);

    }

    @Override
    public void deleteRattachementLigne(Long id) {
        log.debug("Service to delete RattachementLigne : {}", id);
        rattachementLigneRepository.deleteById(id);
    }

    @Override
    public RattachementLigne addRattachementLigne(RattachementLigneVM ligneVM) {

        log.debug ( "Service to save RattachementLigne : {}", ligneVM );
        ligneVM.setNumero(FormatNumberPhoneUtil.extractNumberWithoutSuffix(ligneVM.getNumero()));

        ligneVM.setLogin(FormatNumberPhoneUtil.extractNumberWithoutSuffix(ligneVM.getLogin()));
        RattachementLigne rattachement = new RattachementLigne();
        if (ligneVM.getNumero().matches(Constants.VALIDE_NUMBER_ORANGE_FIXE_MOBILE)) {

            checkNumberIfUsed(ligneVM.getNumero(), ligneVM.getLogin());

            Optional<AccountB2C> accountB2C = accountB2CRepository.findOneByNumero(ligneVM.getLogin());

            if (!accountB2C.isPresent()) {
                log.debug ( "Error login not found  : {}", ligneVM.getLogin() );
                throw new LigneNotFoundException();
            }

            rattachement.setNumero(ligneVM.getNumero());
            rattachement.setTypeNumero(ligneVM.getTypeNumero());

            rattachement.setAccountB2C(accountB2C.get());

            rattachement = rattachementLigneRepository.save(rattachement);

        }

        return rattachement;
    }

    @Override
    public List<InfoNumberVM> getRattachementLignes(String msisdn) {
        log.debug("Service to get all RattachementLigne : {}", msisdn);

        List<InfoNumberVM> infoNumberVMList = new ArrayList<>();

        msisdn = FormatNumberPhoneUtil.extractNumberWithoutSuffix(msisdn);

        Optional<AccountB2C> user = accountB2CRepository.findOneByNumero(msisdn);

        if (user.isPresent()) {
            List<RattachementLigne> list = rattachementLigneRepository.findAllByAccountB2C(user.get());

            for (RattachementLigne rattachementLigne : list) {

                InfoNumberVM infoNumberVMS = new InfoNumberVM();

                infoNumberVMS.setMsisdn(rattachementLigne.getNumero());

                SouscriptionDto dto = getSouscription(rattachementLigne.getNumero());
                if (dto != null) {

                    infoNumberVMS.setProfil(dto.getProfil());
                    infoNumberVMS.setFormule(dto.getNomOffre());

                } else {
                    infoNumberVMS.setProfil("");
                    infoNumberVMS.setFormule("");

                }

                infoNumberVMList.add(infoNumberVMS);
            }
        }
        return infoNumberVMList;
    }

    @Override
    public RattachementLignesDeleteMultipleVM deleteMultipleRattachementLigne( RattachementLignesDeleteMultipleVM deleteListe) {

        log.debug ( "Service to delete RattachementLigne : {}", deleteListe );
        for (String numero : deleteListe.getListMsisdn()) {
            Optional<RattachementLigne> ligne = rattachementLigneRepository
                .findByNumero(numero);
            if (ligne.isPresent()) {
                rattachementLigneRepository.delete(ligne.get());
                deleteListe.setDeleted(true);
            }
        }
        return deleteListe;

    }

    /**
     *
     * @since 23/05/2019
     * @author Bouya Kande
     *
     * @param numberRequest
     * @return statut 200 (ok)
     *
     * @throws ResponseEntity 400 (Bad Request) : if token is invalid
     * @throws NoValideNumberFixeException 400 (Bad Request) : if the number is not an orange number
     * @throws LigneAlreadyRattachedException 400 (Bad Request) : if the number is already attached to an account
     * @throws AccountAlreadyHaveNumberFixeException 400 (Bad Request) : if already has a fixed fix number
     *
     */
    @Override
    public ResponseEntity checkNumberFix(CheckNumberFixVM numberRequest) {

        log.debug("Check number of AccountB2C : {}", numberRequest);

        if(!captchaService.verifyCaptcha(numberRequest.getToken())){
            log.debug("Error invalid token  : {}", numberRequest);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(!numberRequest.getMsisdn().matches(Constants.FIX_REGEX_VALID_NUMBER)){

            log.debug("Error this number is not an valid number orange  : {}", numberRequest);
            throw  new  NoValideNumberFixeException();
        }

        String numFixe = FormatNumberPhoneUtil.extractNumberWithoutSuffix(numberRequest.getMsisdn());
        String login = FormatNumberPhoneUtil.extractNumberWithoutSuffix(numberRequest.getLogin());

        Optional<RattachementLigne> ligne = rattachementLigneRepository.findByNumero(numFixe);
        if(ligne.isPresent()){
            log.debug("Error number is already rattached  : {}", numberRequest);
            throw new LigneAlreadyRattachedException();
        }

        checkFixNumberAssociatedWithThisAccount(login);

        return ResponseEntity.ok().build();
    }


    public void checkNumberIfUsed(String number, String login) {

        log.debug ( "Service to check status number for rattached : {}", number );
        log.debug ( "Service to check status login : {}", login );
        number = FormatNumberPhoneUtil.extractNumberWithoutSuffix(number);
        Optional<RattachementLigne> ligne = rattachementLigneRepository
            .findByNumero(number);

        if (ligne.isPresent()) {
            log.debug ( "error ligne already rattached : {}", number );
            throw new LigneAlreadyRattachedException();
        }

        Optional<AccountB2C> account = accountB2CRepository.findOneByNumero(login);

        if (!account.isPresent()) {
            log.debug ( "error login not found : {}", login );
            throw new LigneNotFoundException();
        }

        Optional<AccountB2C> rattachementLigne = accountB2CRepository.findOneByNumero(number);
        if (rattachementLigne.isPresent()) {
            log.debug ( "error ligne already use in a other account : {}", number );
            throw new LoginAlreadyUsedException();
        }

    }


    public SouscriptionDto getSouscription(String msisdn) {

        try {

            ResponseEntity<SouscriptionDto> response = selfcareSoapService.getSouscription(msisdn);
            if (response.getBody()!= null) {
                return response.getBody();
            } else {
                return null;
            }

        } catch (Exception e) {

            log.debug("Exception get souscription abonne : {}", msisdn);
        }

        return null;
    }

    /**
     * @since 23/05/2019
     * @param login
     */

    public void checkFixNumberAssociatedWithThisAccount(String login){

        List<RattachementLigne> ligneList = rattachementLigneRepository.findByAccountB2C_Numero(login);
        if(!ligneList.isEmpty()){

            for (RattachementLigne ligne1: ligneList) {

                if(ligne1.getNumero().matches(Constants.FIX_REGEX_VALID_NUMBER)){
                    log.debug("Error this account have a number fixe rattached  : {}", login);
                    throw new AccountAlreadyHaveNumberFixeException();
                }

            }

        }

    }
}
