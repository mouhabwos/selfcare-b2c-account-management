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
import sn.sonatel.dsi.dac.dif.ds.juf.middleware.logging.Auditable;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.service.RattachementLigneService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.RattachementLigneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.HeaderUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Message;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.PaginationUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
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
    private static final String ENDPOINT = "/api/rattachement-lignes/";

    private final RattachementLigneService rattachementLigneService;


    public RattachementLigneResource(RattachementLigneService rattachementLigneService) {

        this.rattachementLigneService = rattachementLigneService;
    }

    @Auditable(description = Message.Rattachement.ADD)
    @PostMapping("/rattachement-lignes")
    @PreAuthorize("#rattachementLigne.login == authentication.name")
    public ResponseEntity<RattachementLigne> createRattachementLigne(@Valid @RequestBody RattachementLigneDTO rattachementLigne) throws URISyntaxException {
        log.debug ( "REST request to save RattachementLigne : {}", rattachementLigne );

        if (rattachementLigne.getId() != null) {

            throw new BadRequestAlertException("A new rattachementLigne cannot already have an ID", ENTITY_NAME, "idexists");
        }

        RattachementLigne result = rattachementLigneService.createRattachementLigne(rattachementLigne);

        return ResponseEntity.created ( new URI ( ENDPOINT + result.getId () ) )
            .headers ( HeaderUtil.createEntityCreationAlert ( ENTITY_NAME, result.getId ().toString () ) )
            .body ( result );
    }



    @Auditable(description = Message.Rattachement.UPDATE)
    @PutMapping("/rattachement-lignes")
    @PreAuthorize("#rattachementLigne.login == authentication.name")
    public ResponseEntity<RattachementLigne> updateRattachementLigne(@Valid @RequestBody RattachementLigneDTO rattachementLigne) {
        log.debug ( "REST request to update RattachementLigne : {}", rattachementLigne );
        if (rattachementLigne.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        RattachementLigne result = rattachementLigneService.updateRattachementLigne(rattachementLigne);

        return ResponseEntity.ok ()
            .headers ( HeaderUtil.createEntityUpdateAlert ( ENTITY_NAME, rattachementLigne.getId ().toString () ) )
            .body ( result );
    }



    @Auditable(description = Message.Rattachement.LIST)
    @GetMapping("/rattachement-lignes")
    @PreAuthorize("#rattachementLigne.login == authentication.name")
    public ResponseEntity<List<RattachementLigne>> getAllRattachementLignes(Pageable pageable) {
        log.debug ( "REST request to get a page of RattachementLignes" );
        Page<RattachementLigne> page = rattachementLigneService.getAllRattachementLignes ( pageable );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders ( page, "/api/rattachement-lignes" );
        return ResponseEntity.ok ().headers ( headers ).body ( page.getContent () );
    }


    @Auditable(description = Message.Rattachement.LIST_BY_ID)
    @GetMapping("/rattachement-lignes/{id}")
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<RattachementLigne> getRattachementLigne(@PathVariable Long id) {
        log.debug ( "REST request to get RattachementLigne ");
        Optional<RattachementLigne> rattachementLigne = rattachementLigneService.getRattachementLigne ( id );
        return ResponseUtil.wrapOrNotFound ( rattachementLigne );
    }



    @Auditable(description = Message.Rattachement.DELETE)
    @DeleteMapping("/rattachement-lignes/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteRattachementLigne(@PathVariable Long id) {
        log.debug ( "REST request to delete RattachementLigne : {}", id );
        rattachementLigneService.deleteRattachementLigne ( id );
        return ResponseEntity.ok ().headers ( HeaderUtil.createEntityDeletionAlert ( ENTITY_NAME, id.toString () ) ).build ();
    }



    @Auditable(description = Message.Rattachement.SAVE)
    @PostMapping("/rattachement-lignes/register")
    @PreAuthorize("#ligneVM.login==authentication.name")
    public ResponseEntity<RattachementLigne> addRattachementLigne(
        @Valid @RequestBody RattachementLigneVM ligneVM) throws URISyntaxException {

        RattachementLigne rattachement = rattachementLigneService.addRattachementLigne(ligneVM);

        return ResponseEntity.created ( new URI ( ENDPOINT + rattachement.getId () ) )
            .headers ( HeaderUtil.createEntityCreationAlert ( ENTITY_NAME, rattachement.getId ().toString () ) )
            .body ( rattachement );
    }



    @Auditable(description = Message.Rattachement.List_By_MSISDN)
    @GetMapping("/rattachement-lignes/get-all-number/{msisdn}")
    @Timed
    @PreAuthorize("#msisdn == authentication.name")
    public ResponseEntity<List<InfoNumberVM>> getRattachementLignes(
        @PathVariable String msisdn, @RequestParam(required = false, defaultValue = "true") boolean withCustomerOffer) {
        log.debug ( "REST request to get RattachementLigne : {}, {}", msisdn, withCustomerOffer );

        List<InfoNumberVM> infoNumberVMList = rattachementLigneService.getRattachementLignes(msisdn, withCustomerOffer);

        return ResponseEntity.ok ( infoNumberVMList );
    }



    @Auditable(description = Message.Rattachement.DELETE_ALL)
    @PostMapping("/rattachement-lignes/delete-multiple")
    @Timed
    @PreAuthorize("#deleteListe.login == authentication.name")
    public ResponseEntity<RattachementLignesDeleteMultipleVM> deleteMultipleRattachementLigne(
        @Valid @RequestBody RattachementLignesDeleteMultipleVM deleteListe) {

        deleteListe = rattachementLigneService.deleteMultipleRattachementLigne(deleteListe);
        return ResponseEntity.ok ( deleteListe );

    }

    @Auditable(description = Message.Rattachement.ADD_LIGNE_FIXE)
    @PostMapping("/rattachement-lignes/fixe-register")
    @PreAuthorize("#ligneVM.login==authentication.name")
    public ResponseEntity<RattachementLigne> addRattachementLigneFixe(
        @Valid @RequestBody RattachementLigneFixeVM ligneVM) throws URISyntaxException {

        RattachementLigne rattachement = rattachementLigneService.addRattachementLigneFixe(ligneVM);
        return ResponseEntity.created ( new URI ( ENDPOINT + rattachement.getId () ) )
            .headers ( HeaderUtil.createEntityCreationAlert ( ENTITY_NAME, rattachement.getId ().toString () ) )
            .body ( rattachement );
    }

    @Auditable(description = Message.Rattachement.SAVE_RATTACHEMENT_LIGNE_BY_CNI)
    @PostMapping("/v2/rattachement-lignes/register/by-cni")
    @PreAuthorize("#rattachementLigneCNIVM.login==authentication.name")
    public ResponseEntity<RattachementLigne> rattachementLigneByCni(
        @Valid @RequestBody RattachementLigneCNIVM rattachementLigneCNIVM) throws URISyntaxException {
        log.debug ( "REST request to add RattachementLigne by cni : {}", rattachementLigneCNIVM );
        RattachementLigne rattachement = rattachementLigneService.rattachementLigneByCni(rattachementLigneCNIVM);
        return ResponseEntity.created ( new URI ( ENDPOINT + rattachement.getId () ) )
            .headers ( HeaderUtil.createEntityCreationAlert ( ENTITY_NAME, rattachement.getId ().toString () ) )
            .body ( rattachement );
    }

}
