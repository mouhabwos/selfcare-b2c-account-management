package sn.sonatel.dsi.dif.selfcare.b2c.service;

import org.springframework.web.multipart.MultipartFile;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.UploadResponse;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.SponsorException;

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
    Sponsor save(Sponsor sponsor) throws SponsorException;


    /**
     * Update a sponsor.
     *
     * @param sponsor the entity to save.
     * @return the updated entity.
     */
    Sponsor update(Sponsor sponsor);

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
     * check if the msisdn is present
     * @param msisdn
     * @return
     */
    Boolean isNumberPresent(String msisdn);

    /**
     * get the "msisdn" Sponsor
     *
     * @param msisdn
     * @return
     */
    Sponsor getSponsorByMsisdn(String msisdn);

    /**
     * Delete the "id" sponsor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     *
     * @param multipartFile
     * @return
     * @throws SponsorException
     */
    UploadResponse upload(MultipartFile multipartFile) throws SponsorException;
}
