package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.sonatel.dsi.dac.dif.ds.juf.middleware.logging.Auditable;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsee;
import sn.sonatel.dsi.dif.selfcare.b2c.service.SponseeService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SponseeDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.HeaderUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Message;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link Sponsee}.
 */
@RestController
@RequestMapping("/api")
public class SponseeResource {

    private final Logger log = LoggerFactory.getLogger(SponseeResource.class);

    private static final String ENTITY_NAME = "selfcareB2CAccountManagementSponsee";

    private final SponseeService sponseeService;

    public SponseeResource(SponseeService sponseeService) {
        this.sponseeService = sponseeService;
    }

    /**
     * {@code POST  /sponsees} : Create a new sponsee.
     *
     * @param sponseeDTO the sponseeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sponseeDTO, or with status {@code 400 (Bad Request)} if the sponsee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Auditable(description = Message.Sponsee.CREATE_SPONSEE)
    @PostMapping("/sponsees")
    @PreAuthorize("#sponseeDTO.msisdnSponsor == @customSecurityResolver.login")
    public ResponseEntity<SponseeDTO> createSponsee(@Valid @RequestBody SponseeDTO sponseeDTO) throws URISyntaxException {
        log.debug("REST request to save Sponsee : {}", sponseeDTO);
        if (sponseeDTO.getId() != null) {
            throw new BadRequestAlertException("A new sponsee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SponseeDTO result = sponseeService.register(sponseeDTO);
        return ResponseEntity.accepted().body(result);
    }

    /**
     * {@code PUT  /sponsees} : Updates an existing sponsee.
     *
     * @param sponseeDTO the sponseeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sponseeDTO,
     * or with status {@code 400 (Bad Request)} if the sponseeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sponseeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Auditable(description = Message.Sponsee.UPDATE_SPONSEE)
    @PutMapping("/sponsees")
    @PreAuthorize("#sponseeDTO.msisdnSponsor == @customSecurityResolver.login")
    public ResponseEntity<SponseeDTO> updateSponsee(@Valid @RequestBody SponseeDTO sponseeDTO) throws URISyntaxException {
        log.debug("REST request to update Sponsee : {}", sponseeDTO);
        if (sponseeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SponseeDTO result = sponseeService.update(sponseeDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sponseeDTO.getId().toString())).body(result);
    }

    /**
     * {@code GET  /sponsees} : get all the sponsees.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sponsees in body.
     */
    @Auditable(description = Message.Sponsee.LIST_SPONSEE)
    @GetMapping("/sponsees")
    public ResponseEntity<List<SponseeDTO>> getAllSponsees(Pageable pageable) {
        log.debug("REST request to get a page of Sponsees");
        Page<SponseeDTO> page = sponseeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sponsees/:id} : get the "id" sponsee.
     *
     * @param id the id of the sponseeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sponseeDTO, or with status {@code 404 (Not Found)}.
     */
    @Auditable(description = Message.Sponsee.SPONSEE_BY_ID)
    @GetMapping("/sponsees/{id}")
    public ResponseEntity<SponseeDTO> getSponsee(@PathVariable Long id) {
        log.debug("REST request to get Sponsee : {}", id);
        Optional<SponseeDTO> sponseeDTO = sponseeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sponseeDTO);
    }

    /**
     * {@code DELETE  /sponsees/:id} : delete the "id" sponsee.
     *
     * @param id the id of the sponseeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Auditable(description = Message.Sponsee.DELETE_SPONSEE)
    @DeleteMapping("/sponsees/{id}")
    public ResponseEntity<Void> deleteSponsee(@PathVariable Long id) {
        log.debug("REST request to delete Sponsee : {}", id);
        sponseeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @Auditable(description = Message.Sponsee.SMS_TO_SPONSEE)
    @PostMapping("/sponsees/send-sms")
    public ResponseEntity<Void> sendSmsToSponsee(@RequestParam String sMsisdn, @RequestParam String dMsisdn) {
        log.debug("REST request to send sms to Sponsoree : {}", dMsisdn);
        sponseeService.sendSmsToSponsee(sMsisdn, dMsisdn);
        return ResponseEntity.ok().build();
    }

    @Auditable(description = Message.Sponsee.LIST_SPONSEE_BY_MSISDN)
    @GetMapping("/sponsees/by-account/{msisdn}")
    public ResponseEntity<List<Sponsee>> getAllSponseesBySponsor(@PathVariable String msisdn) {
        log.debug("REST request to get a page of Sponsees");
        List<Sponsee> allSponseeByMsisdn = sponseeService.findAllSponseeBySponsor(msisdn);
        return ResponseEntity.ok().body(allSponseeByMsisdn);
    }

    @Auditable(description = Message.Sponsee.CHECK_SPONSEE)
    @GetMapping("/sponsees/check-number/{msisdn}")
    public ResponseEntity<Void> checkNumberSponsee(@PathVariable String msisdn) {
        log.debug("REST request to check number sponsee {} : ", msisdn);
        sponseeService.checkNumberIsSponsee(msisdn);
        return ResponseEntity.ok().build();
    }
}
