package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.sonatel.dsi.dif.selfcare.b2c.aop.logging.annotation.Auditable;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.service.RattachementLigneService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.RattachementLigneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.HeaderUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Message;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.PaginationUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.InfoNumberVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.RattachementLigneVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.RattachementLignesDeleteMultipleVM;

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

        return ResponseEntity.created ( new URI ( "/api/rattachement-lignes/" + result.getId () ) )
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
        log.debug ( "REST request to get RattachementLigne : {}", id );
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

        return ResponseEntity.created ( new URI ( "/api/rattachement-lignes/" + rattachement.getId () ) )
            .headers ( HeaderUtil.createEntityCreationAlert ( ENTITY_NAME, rattachement.getId ().toString () ) )
            .body ( rattachement );
    }



    @Auditable(description = Message.Rattachement.List_By_MSISDN)
    @GetMapping("/rattachement-lignes/get-all-number/{msisdn}")
    @Timed
    @PostAuthorize("#msisdn == authentication.name")
    public ResponseEntity<List<InfoNumberVM>> getRattachementLignes(
        @PathVariable String msisdn) {
        log.debug ( "REST request to get RattachementLigne : {}", msisdn );

        List<InfoNumberVM> infoNumberVMList = rattachementLigneService.getRattachementLignes(msisdn);

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

}
