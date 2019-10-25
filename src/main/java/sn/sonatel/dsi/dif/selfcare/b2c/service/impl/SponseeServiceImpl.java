package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.service.SponseeService;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsee;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponseeRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SponseeDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.mapper.SponseeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServicesOTP;
import sn.sonatel.dsi.dif.selfcare.b2c.service.vm.MessageVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.FormatNumberPhoneUtil;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Sponsee}.
 */
@Service
@Transactional
public class SponseeServiceImpl implements SponseeService {

    private final Logger log = LoggerFactory.getLogger(SponseeServiceImpl.class);

    private final SponseeRepository sponseeRepository;

    private final SponseeMapper sponseeMapper;

    private static final String ENTITY_NAME = "selfcareB2CAccountManagementSponseeServiceImpl";

    private final ApplicationProperties applicationProperties;

    private final ServicesOTP servicesOTP;

    public SponseeServiceImpl(SponseeRepository sponseeRepository, SponseeMapper sponseeMapper, ApplicationProperties applicationProperties, ServicesOTP servicesOTP) {
        this.sponseeRepository = sponseeRepository;
        this.sponseeMapper = sponseeMapper;
        this.applicationProperties = applicationProperties;
        this.servicesOTP = servicesOTP;
    }

    /**
     * Save a sponsee.
     *
     * @param sponseeDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public SponseeDTO save(SponseeDTO sponseeDTO) {
        log.debug("Request to save Sponsee : {}", sponseeDTO);
        Sponsee sponsee = sponseeMapper.toEntity(sponseeDTO);
        sponsee = sponseeRepository.save(sponsee);
        return sponseeMapper.toDto(sponsee);
    }

    /**
     * Get all the sponsees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SponseeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sponsees");
        return sponseeRepository.findAll(pageable)
            .map(sponseeMapper::toDto);
    }


    /**
     * Get one sponsee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SponseeDTO> findOne(Long id) {
        log.debug("Request to get Sponsee : {}", id);
        return sponseeRepository.findById(id)
            .map(sponseeMapper::toDto);
    }

    /**
     * Delete the sponsee by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sponsee : {}", id);
        sponseeRepository.deleteById(id);
    }

    @Override
    public void sendSmsToSponsoree(String msisdnSource, String msisdnDest) {
        log.debug("Request Service to send sms to Sponsee : {}", msisdnDest);

        msisdnSource = FormatNumberPhoneUtil.extractNumberWithoutSuffix(msisdnSource);
        msisdnDest = FormatNumberPhoneUtil.extractNumberWithoutSuffix(msisdnDest);

        check(msisdnSource,msisdnDest);

        MessageVM messageVM = new MessageVM();
        messageVM.setMsisdn(msisdnDest);
        messageVM.setSourceAddress(msisdnSource);
        messageVM.setMessage(applicationProperties.getSendSms().getSponsorship().getSmsSponsee());
        boolean generateMessage = servicesOTP.generateMessage(messageVM);
        if(!generateMessage){
            log.debug("Error Request Service sms not be sent to Sponsee : {}", msisdnDest);
            throw new BadRequestAlertException("Le sms n'a pas été envoyé",ENTITY_NAME,"smsNotSend");
        }

    }


    private void check(String msisdnSource, String msisdnDest){

        Optional<Sponsee> sponsee = sponseeRepository.findOneByMsisdn(msisdnDest);
        if(sponsee.isPresent()){
            if(sponsee.get().getAccountB2C().getNumero().equals(msisdnSource)){
                throw new BadRequestAlertException("Ce numero est parrainé par un autre",ENTITY_NAME,"notYourSponsee");
            }

        }else throw new BadRequestAlertException("Ce numero n'est pas encore parrainé",ENTITY_NAME,"sponseeNotFound");
    }
}
