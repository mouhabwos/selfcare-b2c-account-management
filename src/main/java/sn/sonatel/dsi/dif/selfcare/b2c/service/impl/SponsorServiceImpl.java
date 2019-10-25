package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import sn.sonatel.dsi.dif.selfcare.b2c.service.SponsorService;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsor;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponsorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.MsisdnAlreadyUsedException;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Sponsor}.
 */
@Service
@Transactional
public class SponsorServiceImpl implements SponsorService {

    private final Logger log = LoggerFactory.getLogger(SponsorServiceImpl.class);

    private final SponsorRepository sponsorRepository;

    public SponsorServiceImpl(SponsorRepository sponsorRepository) {
        this.sponsorRepository = sponsorRepository;
    }

    /**
     * Save a sponsor.
     *
     * @param sponsor the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Sponsor save(Sponsor sponsor) {
        log.debug("Request to save Sponsor : {}", sponsor);
        if(isNumberPresent(sponsor.getMsisdn())){
            throw new MsisdnAlreadyUsedException();
        }
        return sponsorRepository.save(sponsor);
    }

    @Override
    public Sponsor update(Sponsor sponsor) {
        log.debug("Request to update Sponsor : {}", sponsor);
        Optional<Sponsor> sponsorToUpdate = findOne(sponsor.getId());
        if(sponsorToUpdate.isPresent() && isNumberPresent(sponsor.getMsisdn()) && getSponsorByMsisdn(sponsor.getMsisdn()).getId().compareTo(sponsorToUpdate.get().getId())!=0){
            throw new MsisdnAlreadyUsedException();
        }
        return sponsorRepository.save(sponsor);
    }

    /**
     * Get all the sponsors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Sponsor> findAll(Pageable pageable) {
        log.debug("Request to get all Sponsors");
        return sponsorRepository.findAll(pageable);
    }


    /**
     * Get one sponsor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Sponsor> findOne(Long id) {
        log.debug("Request to get Sponsor : {}", id);
        return sponsorRepository.findById(id);
    }

    @Override
    public Boolean isNumberPresent(String msisdn) {

        Optional<Sponsor> sponsor=sponsorRepository.getSponsorByMsisdn(msisdn);
        return sponsor.isPresent();

    }

    @Override
    public Sponsor getSponsorByMsisdn(String msisdn) {

        Optional<Sponsor> sponsor = sponsorRepository.getSponsorByMsisdn(msisdn);

        return sponsor.orElse(null);
    }

    /**
     * Delete the sponsor by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sponsor : {}", id);
        sponsorRepository.deleteById(id);
    }
}
