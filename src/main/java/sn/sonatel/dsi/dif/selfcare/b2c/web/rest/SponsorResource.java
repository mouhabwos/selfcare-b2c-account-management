package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsor;
import sn.sonatel.dsi.dif.selfcare.b2c.service.SponsorService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.UploadResponse;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.SponsorException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.HeaderUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Message;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.PaginationUtil;
import sn.sonatel.dsi.dif.selfcare.utils.selfcarelogging.annotation.Auditable;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsor}.
 */
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ROLE_B2C_ADMIN_MARKETING')")
public class SponsorResource {

    private final Logger log = LoggerFactory.getLogger(SponsorResource.class);

    private static final String ENTITY_NAME = "selfcareB2CAccountManagementSponsor";

    private final SponsorService sponsorService;

    public SponsorResource(SponsorService sponsorService) {
        this.sponsorService = sponsorService;
    }

    /**
     * {@code POST  /sponsors} : Create a new sponsor.
     *
     * @param sponsor the sponsor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sponsor, or with status {@code 400 (Bad Request)} if the sponsor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Auditable(description = Message.Sponsor.CREATE)
    @PostMapping("/sponsors")
    public ResponseEntity<Sponsor> createSponsor(@RequestBody Sponsor sponsor) throws URISyntaxException, SponsorException {
        log.debug("REST request to save Sponsor : {}", sponsor);
        if (sponsor.getId() != null) {
            throw new BadRequestAlertException("A new sponsor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sponsor result = sponsorService.save(sponsor);
        return ResponseEntity.created(new URI("/api/sponsors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sponsors} : Updates an existing sponsor.
     *
     * @param sponsor the sponsor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sponsor,
     * or with status {@code 400 (Bad Request)} if the sponsor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sponsor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Auditable(description = Message.Sponsor.UPDATE)
    @PutMapping("/sponsors")
    public ResponseEntity<Sponsor> updateSponsor(@RequestBody Sponsor sponsor) throws URISyntaxException {
        log.debug("REST request to update Sponsor : {}", sponsor);
        if (sponsor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Sponsor result = sponsorService.update(sponsor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sponsor.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sponsors} : get all the sponsors.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sponsors in body.
     */
    @Auditable(description = Message.Sponsor.LIST)
    @GetMapping("/sponsors")
    public ResponseEntity<List<Sponsor>> getAllSponsors(Pageable pageable) {
        log.debug("REST request to get a page of Sponsors");
        Page<Sponsor> page = sponsorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,"/api/sponsors");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sponsors/:id} : get the "id" sponsor.
     *
     * @param id the id of the sponsor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sponsor, or with status {@code 404 (Not Found)}.
     */
    @Auditable(description = Message.Sponsor.SPONSOR_BY_ID)
    @GetMapping("/sponsors/{id}")
    public ResponseEntity<Sponsor> getSponsor(@PathVariable Long id) {
        log.debug("REST request to get Sponsor : {}", id);
        Optional<Sponsor> sponsor = sponsorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sponsor);
    }

    @Auditable(description = Message.Sponsor.CHECK_SPONSOR)
    @GetMapping("/sponsors/{msisdn}/check")
    public ResponseEntity<Boolean> isThisNumerASponsor(@PathVariable String msisdn) {
        log.debug("REST request to get Sponsor : {}", msisdn);
        Boolean result = sponsorService.isNumberPresent(msisdn);

        return ResponseEntity.ok(result);
    }

    /**
     *This API upload a xls file of sponsors
     *
     * @param multipartFile
     * @return List of duplicate elements
     */
    @Auditable(description = Message.Sponsor.UPLOAD)
    @PostMapping("/sponsors/upload")
    public ResponseEntity<UploadResponse> uploadSponsor(@RequestParam("file") MultipartFile multipartFile) {
        log.debug("REST request to upload a file of Sponsors ");

        UploadResponse uploadResponse = new UploadResponse();
        try {
            uploadResponse= sponsorService.upload(multipartFile);
        } catch (SponsorException e) {
            log.debug("Failed to upload file of Sponsors : {}", e.getMessage());
        }
        return ResponseEntity.ok(uploadResponse);
    }

    /**
     * {@code DELETE  /sponsors/:id} : delete the "id" sponsor.
     *
     * @param id the id of the sponsor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Auditable(description = Message.Sponsor.DELETE)
    @DeleteMapping("/sponsors/{id}")
    public ResponseEntity<Void> deleteSponsor(@PathVariable Long id) {
        log.debug("REST request to delete Sponsor : {}", id);
        sponsorService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
