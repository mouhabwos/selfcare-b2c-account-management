package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.sonatel.dsi.dif.selfcare.b2c.aop.logging.annotation.Auditable;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.RattachementLigneRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.RattachementLigneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.SelfcareSoapService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.LigneAlreadyRattachedException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.LigneNotFoundException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.LoginAlreadyUsedException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.FormatNumberPhoneUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.HeaderUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Message;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.PaginationUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.InfoNumberVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.RattachementLigneVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.RattachementLignesDeleteMultipleVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.SOAPRequest;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * REST controller for managing RattachementLigne.
 */
@RestController
@RequestMapping("/api")
public class RattachementLigneResource {

    private final Logger log = LoggerFactory.getLogger ( RattachementLigneResource.class );

    private static final String ENTITY_NAME = "selfcareB2CAccountManagementRattachementLigne";

    private final RattachementLigneRepository rattachementLigneRepository;

    private final AccountB2CRepository accountB2CRepository;

    private final SelfcareSoapService selfcareSoapService;


    public RattachementLigneResource(RattachementLigneRepository rattachementLigneRepository, AccountB2CRepository accountB2CRepository, SelfcareSoapService selfcareSoapService) {
        this.rattachementLigneRepository = rattachementLigneRepository;

        this.accountB2CRepository = accountB2CRepository;

        this.selfcareSoapService = selfcareSoapService;
    }

    @Auditable(description = Message.Rattachement.ADD)
    @PostMapping("/rattachement-lignes")
    @PreAuthorize("#rattachementLigne.login == authentication.name")
    public ResponseEntity<RattachementLigne> createRattachementLigne(@Valid @RequestBody RattachementLigneDTO rattachementLigne) throws URISyntaxException {
        log.debug ( "REST request to save RattachementLigne : {}", rattachementLigne );
        if (rattachementLigne.getId () != null) {
            throw new BadRequestAlertException ( "A new rattachementLigne cannot already have an ID", ENTITY_NAME, "idexists" );
        }
        RattachementLigne ligne = new RattachementLigne ();
        ligne.setTypeNumero ( rattachementLigne.getTypeNumero () );
        ligne.setAccountB2C ( rattachementLigne.getAccountB2C () );
        ligne.setNumero ( rattachementLigne.getNumero () );
        ligne.setStatut ( rattachementLigne.getStatut () );
        ligne.setCodeVerification ( rattachementLigne.getCodeVerification () );
        ligne.setTypeVerification ( rattachementLigne.getTypeVerification () );

        RattachementLigne result = rattachementLigneRepository.save ( ligne );

        return ResponseEntity.created ( new URI ( "/api/rattachement-lignes/" + result.getId () ) )
            .headers ( HeaderUtil.createEntityCreationAlert ( ENTITY_NAME, result.getId ().toString () ) )
            .body ( result );
    }

    @Auditable(description = Message.Rattachement.UPDATE)
    @PutMapping("/rattachement-lignes")
    @PreAuthorize("#rattachementLigne.login == authentication.name")
    public ResponseEntity<RattachementLigne> updateRattachementLigne(@Valid @RequestBody RattachementLigneDTO rattachementLigne) throws URISyntaxException {
        log.debug ( "REST request to update RattachementLigne : {}", rattachementLigne );
        if (rattachementLigne.getId () == null) {
            throw new BadRequestAlertException ( "Invalid id", ENTITY_NAME, "idnull" );
        }
        RattachementLigne ligne = new RattachementLigne ();
        ligne.setId ( rattachementLigne.getId () );
        ligne.setTypeNumero ( rattachementLigne.getTypeNumero () );
        ligne.setAccountB2C ( rattachementLigne.getAccountB2C () );
        ligne.setNumero ( rattachementLigne.getNumero () );
        ligne.setStatut ( rattachementLigne.getStatut () );
        ligne.setCodeVerification ( rattachementLigne.getCodeVerification () );
        ligne.setTypeVerification ( rattachementLigne.getTypeVerification () );

        RattachementLigne result = rattachementLigneRepository.save ( ligne );

        return ResponseEntity.ok ()
            .headers ( HeaderUtil.createEntityUpdateAlert ( ENTITY_NAME, rattachementLigne.getId ().toString () ) )
            .body ( result );
    }

    @Auditable(description = Message.Rattachement.LIST)
    @GetMapping("/rattachement-lignes")
    @PreAuthorize("#rattachementLigne.login == authentication.name")
    public ResponseEntity<List<RattachementLigne>> getAllRattachementLignes(Pageable pageable) {
        log.debug ( "REST request to get a page of RattachementLignes" );
        Page<RattachementLigne> page = rattachementLigneRepository.findAll ( pageable );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders ( page, "/api/rattachement-lignes" );
        return ResponseEntity.ok ().headers ( headers ).body ( page.getContent () );
    }


    @Auditable(description = Message.Rattachement.LIST_BY_ID)
    @GetMapping("/rattachement-lignes/{id}")
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<RattachementLigne> getRattachementLigne(@PathVariable Long id) {
        log.debug ( "REST request to get RattachementLigne : {}", id );
        Optional<RattachementLigne> rattachementLigne = rattachementLigneRepository.findById ( id );
        return ResponseUtil.wrapOrNotFound ( rattachementLigne );
    }

    @Auditable(description = Message.Rattachement.DELETE)
    @DeleteMapping("/rattachement-lignes/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteRattachementLigne(@PathVariable Long id) {
        log.debug ( "REST request to delete RattachementLigne : {}", id );
        rattachementLigneRepository.deleteById ( id );

        return ResponseEntity.ok ().headers ( HeaderUtil.createEntityDeletionAlert ( ENTITY_NAME, id.toString () ) ).build ();
    }

    @Auditable(description = Message.Rattachement.SAVE)
    @PostMapping("/rattachement-lignes/register")
    @PreAuthorize("#ligneVM.login==authentication.name")
    public ResponseEntity<RattachementLigne> addRattachementLigne(
        @Valid @RequestBody RattachementLigneVM ligneVM) throws URISyntaxException {

        ligneVM.setNumero ( FormatNumberPhoneUtil.getNumberFormat ( ligneVM.getNumero () ) );

        ligneVM.setLogin ( FormatNumberPhoneUtil.getNumberFormat ( ligneVM.getLogin () ) );
        RattachementLigne rattachement = new RattachementLigne ();
        if (ligneVM.getNumero ().matches ( Constants.LOGIN_REGEX_VALID_NUMBER )) {

            checkNumberIfUsed ( ligneVM.getNumero (), ligneVM.getLogin () );

            Optional<AccountB2C> accountB2C = accountB2CRepository.findOneByNumero ( ligneVM.getLogin () );

            if (!accountB2C.isPresent ()) {
                throw new LigneNotFoundException ();
            }

            rattachement.setNumero ( ligneVM.getNumero () );
            rattachement.setTypeNumero ( ligneVM.getTypeNumero () );
            rattachement.setCodeVerification ( ligneVM.getCodeVerification () );
            rattachement.setTypeVerification ( ligneVM.getTypeVerification () );
            rattachement.setStatut ( ligneVM.getStatut () );
            rattachement.setAccountB2C ( accountB2C.get () );

            rattachement = rattachementLigneRepository.save ( rattachement );

        }

        return ResponseEntity.created ( new URI ( "/api/rattachement-lignes/" + rattachement.getId () ) )
            .headers ( HeaderUtil.createEntityCreationAlert ( ENTITY_NAME, rattachement.getId ().toString () ) )
            .body ( rattachement );
    }

    @Auditable(description = Message.Rattachement.List_By_MSISDN)
    @GetMapping("/rattachement-lignes/get-all-number/{msisdn}")
    @Timed
  //  @PreAuthorize("#msisdn == authentication.name")
    public ResponseEntity<List<InfoNumberVM>> getRattachementLignes(
        @PathVariable String msisdn) {
        log.debug ( "REST request to get RattachementLigne : {}", msisdn );

        List<InfoNumberVM> infoNumberVMList = new ArrayList<> ();

        msisdn = FormatNumberPhoneUtil.getNumberFormat ( msisdn );

        Optional<AccountB2C> user = accountB2CRepository.findOneByNumero ( msisdn );

        if (user.isPresent ()) {
            List<RattachementLigne> list = rattachementLigneRepository.findAllByAccountB2C ( user.get () );

            for (RattachementLigne rattachementLigne : list) {

                InfoNumberVM infoNumberVMS = new InfoNumberVM ();

                infoNumberVMS.setMsisdn ( rattachementLigne.getNumero () );

                SouscriptionDto dto = getSouscription ( rattachementLigne.getNumero () );
                if (dto != null) {

                    infoNumberVMS.setProfil ( dto.getProfil () );
                    infoNumberVMS.setFormule ( dto.getNomOffre () );

                } else {
                    infoNumberVMS.setProfil ( "" );
                    infoNumberVMS.setFormule ( "" );

                }

                infoNumberVMList.add ( infoNumberVMS );
            }
        }
        return ResponseEntity.ok ( infoNumberVMList );
    }


    @Auditable(description = Message.Rattachement.DELETE_ALL)
    @PostMapping("/rattachement-lignes/delete-multiple")
    @Timed
    @PreAuthorize("#deleteListe.login == authentication.name")
    public ResponseEntity<RattachementLignesDeleteMultipleVM> deleteMultipleRattachementLigne(
        @Valid @RequestBody RattachementLignesDeleteMultipleVM deleteListe) {

        for (String numero : deleteListe.getListMsisdn ()) {
            Optional<RattachementLigne> ligne = rattachementLigneRepository
                .findByNumero ( numero );
            if (ligne.isPresent ()) {
                rattachementLigneRepository.delete ( ligne.get () );
                deleteListe.setDeleted ( true );
            }
        }
        return ResponseEntity.ok ( deleteListe );

    }

    public void checkNumberIfUsed(String number, String login) {

        number = FormatNumberPhoneUtil.getNumberFormat ( number );
        Optional<RattachementLigne> ligne = rattachementLigneRepository
            .findByNumero ( number );

        if (ligne.isPresent ()) {
            throw new LigneAlreadyRattachedException ();
        }

        Optional<AccountB2C> account = accountB2CRepository.findOneByNumero ( login );

        if (!account.isPresent ()) {
            throw new LigneNotFoundException ();
        }

        Optional<AccountB2C> rattachementLigne = accountB2CRepository.findOneByNumero ( number );
        if (rattachementLigne.isPresent ()) {
            throw new LoginAlreadyUsedException ();
        }

    }


    public SouscriptionDto getSouscription(String msisdn) {

        HttpEntity<SOAPRequest> request = new HttpEntity<> ( new SOAPRequest ( msisdn ) );
        try {

            ResponseEntity<SouscriptionDto> response = selfcareSoapService.getSouscription ( request );
            if (response.getStatusCode () == HttpStatus.NOT_FOUND) {
                return null;
            } else if (response.getStatusCode () == HttpStatus.OK) {
                return response.getBody ();
            }

        } catch (Exception e) {

            log.debug ( "Exception get souscription abonne : {}", msisdn );
        }

        return null;
    }
}
