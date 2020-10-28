package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.ClientType;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.RattachementLigneRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.AbonneService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.RattachementLigneService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.SponseeService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.CustomerOfferApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.InfoClientWrapper;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.RattachementLigneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.RattachementLigneResource;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.*;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.FormatNumberPhoneUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service class for managing users RattachementLigne.
 */
@Service
public class RattachementLigneServiceImpl implements RattachementLigneService {

    private final Logger log = LoggerFactory.getLogger(RattachementLigneResource.class);

    private final RattachementLigneRepository rattachementLigneRepository;

    private final AccountB2CRepository accountB2CRepository;

    private final CustomerOfferApiClient customerOfferApiClient;

    private final SponseeService sponseeService;

    private final AbonneService abonneService;

    public RattachementLigneServiceImpl(RattachementLigneRepository rattachementLigneRepository, AccountB2CRepository accountB2CRepository, CustomerOfferApiClient customerOfferApiClient, SponseeService sponseeService, AbonneService abonneService) {

        this.rattachementLigneRepository = rattachementLigneRepository;
        this.accountB2CRepository = accountB2CRepository;
        this.customerOfferApiClient = customerOfferApiClient;
        this.sponseeService = sponseeService;
        this.abonneService = abonneService;
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

        log.debug ( "Service to save RattachementLigne register: {}", ligneVM );
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

            if(!isInContactNumbers(ligneVM.getNumero(),ligneVM.getLogin())){
                log.debug("Service Error forbiden: This number {} cannot be attached by user: {}", ligneVM.getNumero(), ligneVM.getLogin());
                throw new BadRequestAlertException("Vous ne pouvez pas rattacher ce numero","RattachementLigne","notMyNumber");
            }

            rattachement.setNumero(ligneVM.getNumero());
            rattachement.setTypeNumero(ligneVM.getTypeNumero());

            rattachement.setAccountB2C(accountB2C.get());
            rattachement = rattachementLigneRepository.save(rattachement);
            sponseeService.updateEffectiveInscriptionOfSponsee(rattachement.getNumero());
        }

        return rattachement;
    }

    @Override
    public List<InfoNumberVM> getRattachementLignes(String msisdn, boolean withCustomerOffer) {
        log.debug("Service to get all RattachementLigne : {}", msisdn);

        List<InfoNumberVM> infoNumberVMList = new ArrayList<>();

        msisdn = FormatNumberPhoneUtil.extractNumberWithoutSuffix(msisdn);

        Optional<AccountB2C> user = accountB2CRepository.findOneByNumero(msisdn);

        if (user.isPresent()) {
            List<RattachementLigne> list = rattachementLigneRepository.findAllByAccountB2C(user.get());

            for (RattachementLigne rattachementLigne : list) {
                InfoNumberVM infoNumberVMS = new InfoNumberVM();
                if(withCustomerOffer){
                     infoNumberVMS = getInfoNumber(rattachementLigne.getNumero());
                }

                infoNumberVMS.setMsisdn(rattachementLigne.getNumero());

                infoNumberVMList.add(infoNumberVMS);
            }
        }
        return infoNumberVMList;
    }



    private InfoNumberVM getInfoNumber(String msisdn){

        msisdn = FormatNumberPhoneUtil.extractNumberWithoutSuffix(msisdn);

        InfoNumberVM infoNumberVMS = new InfoNumberVM();

            CustomerOffer  customerOffer = getSouscription(msisdn);
            if (customerOffer != null) {

                infoNumberVMS.setProfil(customerOffer.getOfferType().toString());
                infoNumberVMS.setFormule(customerOffer.getOfferName());

            } else {
                infoNumberVMS.setProfil("");
                infoNumberVMS.setFormule("");

            }

        return infoNumberVMS;
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

    @Override
    public RattachementLigne addRattachementLigneFixe(RattachementLigneFixeVM ligneFixeVM){
        log.debug ( "Service to save RattachementLigne Fixe: {}", ligneFixeVM );
        ligneFixeVM.setNumero(FormatNumberPhoneUtil.extractNumberWithoutSuffix(ligneFixeVM.getNumero()));

        ligneFixeVM.setLogin(FormatNumberPhoneUtil.extractNumberWithoutSuffix(ligneFixeVM.getLogin()));
        checkNumberIfUsed(ligneFixeVM.getNumero(),ligneFixeVM.getLogin());
        if(checkIdClient(ligneFixeVM.getNumero(),ligneFixeVM.getIdClient())){
            isAnOrganizationNumber(ligneFixeVM.getNumero());
            Optional<AccountB2C> account = accountB2CRepository.findOneByNumero(ligneFixeVM.getLogin());
            if(account.isPresent()){
                RattachementLigne ligne = new RattachementLigne();
                ligne.setNumero(ligneFixeVM.getNumero());
                ligne.setAccountB2C(account.get());
                ligne.setTypeNumero(ligneFixeVM.getTypeNumero());
                ligne.setIdClient(ligneFixeVM.getIdClient());
                return rattachementLigneRepository.save(ligne);
            }

        }

        throw new BadRequestAlertException("L id Client n existe pas","","");

    }

    private void checkNumberIfUsed(String number, String login) {

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

    private CustomerOffer getSouscription(String msisdn) {

        try {

            ResponseEntity<CustomerOffer> response = customerOfferApiClient.getCustomerOffer(msisdn);
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


    private boolean isInContactNumbers(String msisdn, String login){

        InfoClientWrapper informations = abonneService.getInformations(login);

        if(informations.getClientType() == ClientType.INDIVIDUAL){
            Set<String> contactNumbers = informations.getInformation().getContactNumbers();

          return contactNumbers.contains(msisdn);
        }
        return false;
    }

    private boolean checkIdClient(String msisdn, String idClient){
        CustomerOffer customerOffer = getSouscription(msisdn);
        if(customerOffer != null){
            String clientCode = customerOffer.getClientCode();
            return clientCode.equals(idClient);
        }else throw new NotFoundNumberException("");
    }

    private void isAnOrganizationNumber(String msisdn){
        InfoClientWrapper informations = abonneService.getInformations(msisdn);
        if(informations.getClientType()!= ClientType.INDIVIDUAL && informations.getClientType()==ClientType.ORGANIZATION){
            throw new BadRequestAlertException("Vous ne pouvez pas rattacher un numero d entreprise","","");
        }
    }

}
