package sn.sonatel.dsi.dif.selfcare.b2c.service;

import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsee;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SponseeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsee}.
 */
public interface SponseeService {



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

    void sendSmsToSponsee( String msisdnSource, String msisdnDest);

    SponseeDTO register(SponseeDTO sponseeDTO);

    SponseeDTO update(SponseeDTO sponseeDTO);

    List<Sponsee> findAllSponseeBySponsor(String msisgn);

    void checkNumberIsSponsee(String msisdn);

    Sponsee updateEffectiveInscriptionOfSponsee(String msisdn);
}
