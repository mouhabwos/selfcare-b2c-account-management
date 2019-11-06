package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsor;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.RattachementLigneRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponsorRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.SponseeService;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsee;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponseeRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SponseeDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.errors.ErrorMessages;
import sn.sonatel.dsi.dif.selfcare.b2c.service.mapper.SponseeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServicesOTP;
import sn.sonatel.dsi.dif.selfcare.b2c.service.vm.MessageVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.*;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.FormatNumberPhoneUtil;

import java.util.List;
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

    private final AccountB2CRepository accountB2CRepository;

    private final RattachementLigneRepository rattachementLigneRepository;

    private final SponsorRepository sponsorRepository;

    public SponseeServiceImpl(SponseeRepository sponseeRepository, SponseeMapper sponseeMapper, ApplicationProperties applicationProperties, ServicesOTP servicesOTP, AccountB2CRepository accountB2CRepository, RattachementLigneRepository rattachementLigneRepository, SponsorRepository sponsorRepository) {
        this.sponseeRepository = sponseeRepository;
        this.sponseeMapper = sponseeMapper;
        this.applicationProperties = applicationProperties;
        this.servicesOTP = servicesOTP;
        this.accountB2CRepository = accountB2CRepository;
        this.rattachementLigneRepository = rattachementLigneRepository;
        this.sponsorRepository = sponsorRepository;
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

        Optional<Sponsee> sponsee = sponseeRepository.findById(id);
        if(sponsee.isPresent()){
            SponseeDTO sponseeDT = sponseeMapper.toDto(sponsee.get());
            Optional<SponseeDTO>  toDto = Optional.of(sponseeDT);

            toDto.ifPresent(sponseeDTO -> sponseeDTO.setMsisdnSponsor(sponsee.get().getAccountB2C().getNumero()));
            return toDto;
        }

       return Optional.empty();
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
    public void sendSmsToSponsee(String msisdnSource, String msisdnDest) {
        log.debug("Request Service to send sms to Sponsee : {}", msisdnDest);

        msisdnSource = FormatNumberPhoneUtil.extractNumberWithoutSuffix(msisdnSource);
        msisdnDest = FormatNumberPhoneUtil.extractNumberWithoutSuffix(msisdnDest);

        checkNumberSponsee(msisdnSource,msisdnDest);

        MessageVM messageVM = new MessageVM();
        messageVM.setMsisdn(msisdnDest);
        messageVM.setSourceAddress(msisdnSource);
        messageVM.setMessage(applicationProperties.getSendSms().getSponsorship().getSmsSponsee());
        boolean generateMessage = servicesOTP.generateMessage(messageVM);
        if(!generateMessage){
            log.debug("Error Request Service sms not be sent to Sponsee : {}", msisdnDest);
            throw new BadRequestAlertException(ErrorMessages.SMS_NOT_BE_SEND,ENTITY_NAME,"smsNotSend");
        }

    }

    @Override
    public List<Sponsee> findAllSponseeBySponsor(String msisdn) {

        msisdn = FormatNumberPhoneUtil.extractNumberWithoutSuffix(msisdn);

        Optional<AccountB2C> user = accountB2CRepository.findOneByNumero(msisdn);

        if (user.isPresent()) {
            return sponseeRepository.findAllByAccountB2C(user.get());
        }else  throw new NotFoundNumberException( ErrorMessages.USER_NOT_FOUND) ;
    }

    @Override
    public void checkNumberIsSponsee(String msisdn) {
        log.debug("Service Request check if number is sponsored : {}", msisdn);
        msisdn = FormatNumberPhoneUtil.extractNumberWithoutSuffix(msisdn);
        checkDisponibilityOfNumberForSponsored(msisdn);
    }

    @Override
    public SponseeDTO register(SponseeDTO sponseeDTO){

        //format msisdn of sponsee and sponsor
        sponseeDTO.setMsisdnSponsor(FormatNumberPhoneUtil.extractNumberWithoutSuffix(sponseeDTO.getMsisdnSponsor()));
        sponseeDTO.setMsisdn(FormatNumberPhoneUtil.extractNumberWithoutSuffix(sponseeDTO.getMsisdn()));

        // check number of sponsee and sponsor
        checkNumberOfSponsor(sponseeDTO.getMsisdnSponsor());
        checkDisponibilityOfNumberForSponsored(sponseeDTO.getMsisdn());

        // save sponsee
        Sponsee sponsee = sponseeMapper.toEntity(sponseeDTO);
        Optional<AccountB2C> accountB2C = accountB2CRepository.findOneByNumero(sponseeDTO.getMsisdnSponsor());

        if(accountB2C.isPresent()){
            sponsee.setAccountB2C(accountB2C.get());
        }
        sponsee = sponseeRepository.save(sponsee);

        // Send sms to sponsee
        sendSmsToSponsee(sponsee.getAccountB2C().getNumero(), sponsee.getMsisdn());

        SponseeDTO toDto = sponseeMapper.toDto(sponsee);

        toDto.setMsisdnSponsor(sponseeDTO.getMsisdnSponsor());

        return toDto;
    }

    @Override
    public SponseeDTO update(SponseeDTO sponseeDTO){
        log.debug("Request to save Sponsee : {}", sponseeDTO);
        //format msisdn of sponsee and sponsor
        sponseeDTO.setMsisdnSponsor(FormatNumberPhoneUtil.extractNumberWithoutSuffix(sponseeDTO.getMsisdnSponsor()));
        sponseeDTO.setMsisdn(FormatNumberPhoneUtil.extractNumberWithoutSuffix(sponseeDTO.getMsisdn()));


        Optional<Sponsee> sponseeBean = sponseeRepository.findOneByMsisdn(sponseeDTO.getMsisdn());
        if(sponseeBean.isPresent() && !sponseeBean.get().getAccountB2C().getNumero().equals(sponseeDTO.getMsisdnSponsor())){
            throw new ForbiddenException();
        }

        Sponsee sponsee = sponseeMapper.toEntity(sponseeDTO);
        Optional<AccountB2C> accountB2C = accountB2CRepository.findOneByNumero(sponseeDTO.getMsisdnSponsor());

        if(accountB2C.isPresent()){
            sponsee.setAccountB2C(accountB2C.get());
        }
        sponsee = sponseeRepository.save(sponsee);
        SponseeDTO toDto = sponseeMapper.toDto(sponsee);
        toDto.setMsisdnSponsor(sponsee.getAccountB2C().getNumero());
        return toDto;
    }

    private void checkNumberSponsee(String msisdnSource, String msisdnDest){

        Optional<Sponsee> sponsee = sponseeRepository.findOneByMsisdn(msisdnDest);
        if(sponsee.isPresent() && sponsee.get().isEnabled()){

            if(!sponsee.get().getAccountB2C().getNumero().equals(msisdnSource)){
                throw new ForbiddenException();
            }
            if(sponsee.get().isEffective()){
                throw new BadRequestAlertException(ErrorMessages.NUMBER_IS_ALREADY_REGISTERED,ENTITY_NAME,"userExiste");
            }

        }else throw new BadRequestAlertException(ErrorMessages.NUMBER_ALREADY_SPONSORED,ENTITY_NAME,"sponseeNotFound");
    }


    private void checkDisponibilityOfNumberForSponsored(String msisdnSponsee){

        Optional<RattachementLigne> rattachementLigne = rattachementLigneRepository.findByNumero(msisdnSponsee);
        if(rattachementLigne.isPresent()){
            log.debug("Error Request Service msisdn is rattached : {}", msisdnSponsee);
            throw new LigneAlreadyRattachedException();
        }

        Optional<AccountB2C> accountB2C = accountB2CRepository.findOneByNumero(msisdnSponsee);
        if(accountB2C.isPresent()){
            log.debug("Error Request Service msisdn have an account : {}", msisdnSponsee);
            throw new LoginAlreadyUsedException();
        }

        Optional<Sponsee> sponsee = sponseeRepository.findOneByMsisdn(msisdnSponsee);
        if(sponsee.isPresent() && sponsee.get().isEnabled()){
            log.debug("Error Request Service msisdn is sponsored : {}", msisdnSponsee);
            throw new BadRequestAlertException(ErrorMessages.NUMBER_IS_ALREADY_REGISTERED,ENTITY_NAME,"userExiste");
        }

    }

    private void checkNumberOfSponsor(String msisdSponsor){

        Optional<Sponsor> optionalSponsor = sponsorRepository.getSponsorByMsisdn(msisdSponsor);
        if(!optionalSponsor.isPresent()){
            log.debug("Error Request Service  msisdn does not have the possibility to sponsor: {}", msisdSponsor);
            throw new ForbiddenException();
        }

        Optional<RattachementLigne> rattachementLigne = rattachementLigneRepository.findByNumero(msisdSponsor);
        Optional<AccountB2C> accountB2C = accountB2CRepository.findOneByNumero(msisdSponsor);
        if(!rattachementLigne.isPresent() && !accountB2C.isPresent()){
            log.debug("Error Request Service  msisdn of sponsor hav'nt a account  : {}", msisdSponsor);
            throw new BadRequestAlertException(ErrorMessages.MSISDN_DOES_NOT_HAVE_AN_ACCOUNT,ENTITY_NAME,"notFoundSponsor");
        }

    }
}
