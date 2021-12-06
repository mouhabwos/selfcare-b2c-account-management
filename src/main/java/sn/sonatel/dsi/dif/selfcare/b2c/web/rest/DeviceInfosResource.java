package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.DeviceInfos;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.DeviceInfosRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link DeviceInfos}.
 */
@RestController
@RequestMapping("/api")
public class DeviceInfosResource {

    private final Logger log = LoggerFactory.getLogger(DeviceInfosResource.class);

    private static final String ENTITY_NAME = "selfcareB2CAccountManagementDeviceInfos";

    private final DeviceInfosRepository deviceInfosRepository;

    public DeviceInfosResource(DeviceInfosRepository deviceInfosRepository) {
        this.deviceInfosRepository = deviceInfosRepository;
    }

    /**
     * {@code POST  /device-infos} : Create a new deviceInfos.
     *
     * @param deviceInfos the deviceInfos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deviceInfos, or with status {@code 400 (Bad Request)} if the deviceInfos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/device-infos")
    public ResponseEntity<DeviceInfos> createDeviceInfos(@RequestBody DeviceInfos deviceInfos) throws URISyntaxException {
        log.debug("REST request to save DeviceInfos : {}", deviceInfos);
        if (deviceInfos.getId() != null) {
            throw new BadRequestAlertException("A new deviceInfos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeviceInfos result = deviceInfosRepository.save(deviceInfos);
        return ResponseEntity.created(new URI("/api/device-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert( ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /device-infos} : Updates an existing deviceInfos.
     *
     * @param deviceInfos the deviceInfos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceInfos,
     * or with status {@code 400 (Bad Request)} if the deviceInfos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deviceInfos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/device-infos")
    public ResponseEntity<DeviceInfos> updateDeviceInfos(@RequestBody DeviceInfos deviceInfos) throws URISyntaxException {
        log.debug("REST request to update DeviceInfos : {}", deviceInfos);
        if (deviceInfos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DeviceInfos result = deviceInfosRepository.save(deviceInfos);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, deviceInfos.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /device-infos} : get all the deviceInfos.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deviceInfos in body.
     */
    @GetMapping("/device-infos")
    public ResponseEntity<List<DeviceInfos>> getAllDeviceInfos(Pageable pageable) {
        log.debug("REST request to get a page of DeviceInfos");
        Page<DeviceInfos> page = deviceInfosRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /device-infos/:id} : get the "id" deviceInfos.
     *
     * @param id the id of the deviceInfos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deviceInfos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/device-infos/{id}")
    public ResponseEntity<DeviceInfos> getDeviceInfos(@PathVariable Long id) {
        log.debug("REST request to get DeviceInfos : {}", id);
        Optional<DeviceInfos> deviceInfos = deviceInfosRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(deviceInfos);
    }

    /**
     * {@code DELETE  /device-infos/:id} : delete the "id" deviceInfos.
     *
     * @param id the id of the deviceInfos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/device-infos/{id}")
    public ResponseEntity<Void> deleteDeviceInfos(@PathVariable Long id) {
        log.debug("REST request to delete DeviceInfos : {}", id);
        deviceInfosRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
