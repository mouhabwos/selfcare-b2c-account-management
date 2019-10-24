package sn.sonatel.dsi.dif.selfcare.b2c.service;

import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Sponsor}.
 */
public interface SponsorService {

    /**
     * Save a sponsor.
     *
     * @param sponsor the entity to save.
     * @return the persisted entity.
     */
    Sponsor save(Sponsor sponsor);

    /**
     * Get all the sponsors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Sponsor> findAll(Pageable pageable);


    /**
     * Get the "id" sponsor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Sponsor> findOne(Long id);

    /**
     * Delete the "id" sponsor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
