package sn.sonatel.dsi.dif.selfcare.b2c.service;

import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SponseeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsee}.
 */
public interface SponseeService {

    /**
     * Save a sponsee.
     *
     * @param sponseeDTO the entity to save.
     * @return the persisted entity.
     */
    SponseeDTO save(SponseeDTO sponseeDTO);

    /**
     * Get all the sponsees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SponseeDTO> findAll(Pageable pageable);


    /**
     * Get the "id" sponsee.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SponseeDTO> findOne(Long id);

    /**
     * Delete the "id" sponsee.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void sendSmsToSponsoree( String msisdnSource, String msisdnDest);
}
