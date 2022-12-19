package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsee;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.RattachementLigneRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponseeRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.BoosterManagerService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.SMSNotificationService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.SponseeService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.booster.dto.BoosterPromo;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SponseeDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.errors.ErrorMessages;
import sn.sonatel.dsi.dif.selfcare.b2c.service.mapper.SponseeMapper;
import sn.sonatel.dsi.dif.selfcare.b2c.service.vm.MessageVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.*;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.FormatNumberPhoneUtil;

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

    private static final String TRIGGER_TYPE_FORM_INSCRIPTION = "FORM_INSCRIPTION";

    private static final String FRCFA = " FCFA";

    private static final String POURCENTAGE = " %";

    private final ApplicationProperties applicationProperties;

    private final SMSNotificationService smsNotificationService;

    private final AccountB2CRepository accountB2CRepository;

    private final RattachementLigneRepository rattachementLigneRepository;

    private final BoosterManagerService boosterManagerService;

    public SponseeServiceImpl(
        SponseeRepository sponseeRepository,
        SponseeMapper sponseeMapper,
        ApplicationProperties applicationProperties,
        SMSNotificationService smsNotificationService,
        AccountB2CRepository accountB2CRepository,
        RattachementLigneRepository rattachementLigneRepository,
        BoosterManagerService boosterManagerService
    ) {
        this.sponseeRepository = sponseeRepository;
        this.sponseeMapper = sponseeMapper;
        this.applicationProperties = applicationProperties;
        this.smsNotificationService = smsNotificationService;
        this.accountB2CRepository = accountB2CRepository;
        this.rattachementLigneRepository = rattachementLigneRepository;
        this.boosterManagerService = boosterManagerService;
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
        List<SponseeDTO> sponseeDTOList = new ArrayList<>();
        List<Sponsee> sponseeRepositoryAll = sponseeRepository.findAll();
        for (Sponsee sponsee : sponseeRepositoryAll) {
            SponseeDTO sponseeDTO = sponseeMapper.toDto(sponsee);
            sponseeDTO.setMsisdnSponsor(sponsee.getAccountB2C().getNumero());
            sponseeDTOList.add(sponseeDTO);
        }
        return new PageImpl<>(sponseeDTOList, pageable, sponseeDTOList.size());
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
        if (sponsee.isPresent()) {
            SponseeDTO sponseeDT = sponseeMapper.toDto(sponsee.get());
            Optional<SponseeDTO> toDto = Optional.of(sponseeDT);

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

        checkNumberSponsee(msisdnSource, msisdnDest);
        MessageVM messageVM = new MessageVM();
        messageVM.setMsisdn(msisdnDest);
        messageVM.setSourceAddress(msisdnSource);
        String messagePro = getMessagePromo(msisdnDest, TRIGGER_TYPE_FORM_INSCRIPTION);

        if (messagePro != null && !messagePro.equals("")) {
            messageVM.setMessage(messagePro);
        } else messageVM.setMessage(String.format(applicationProperties.getSendSms().getSponsorship().getSmsSponsee(), ""));

        smsNotificationService.sendSMSPP(messageVM.getMsisdn(), messageVM.getMessage(), messageVM.getSourceAddress());
    }

    @Override
    public List<Sponsee> findAllSponseeBySponsor(String msisdn) {
        msisdn = FormatNumberPhoneUtil.extractNumberWithoutSuffix(msisdn);

        Optional<AccountB2C> user = accountB2CRepository.findOneByNumero(msisdn);

        if (user.isPresent()) {
            return sponseeRepository.findAllByAccountB2C(user.get());
        } else throw new NotFoundNumberException(ErrorMessages.USER_NOT_FOUND);
    }

    @Override
    public void checkNumberIsSponsee(String msisdn) {
        log.debug("Service Request check if number is sponsored : {}", msisdn);
        msisdn = FormatNumberPhoneUtil.extractNumberWithoutSuffix(msisdn);
        checkDisponibilityOfNumberForSponsored(msisdn);
    }

    @Override
    public SponseeDTO register(SponseeDTO sponseeDTO) {
        //format msisdn of sponsee and sponsor
        sponseeDTO.setMsisdnSponsor(FormatNumberPhoneUtil.extractNumberWithoutSuffix(sponseeDTO.getMsisdnSponsor()));
        sponseeDTO.setMsisdn(FormatNumberPhoneUtil.extractNumberWithoutSuffix(sponseeDTO.getMsisdn()));

        // check number of sponsee and sponsor
        checkNumberOfSponsor(sponseeDTO.getMsisdnSponsor());
        checkDisponibilityOfNumberForSponsored(sponseeDTO.getMsisdn());

        // save sponsee
        Sponsee sponsee = sponseeMapper.toEntity(sponseeDTO);
        Optional<AccountB2C> accountB2C = accountB2CRepository.findOneByNumero(sponseeDTO.getMsisdnSponsor());

        if (accountB2C.isPresent()) {
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
    public SponseeDTO update(SponseeDTO sponseeDTO) {
        log.debug("Request to save Sponsee : {}", sponseeDTO);
        //format msisdn of sponsee and sponsor
        sponseeDTO.setMsisdnSponsor(FormatNumberPhoneUtil.extractNumberWithoutSuffix(sponseeDTO.getMsisdnSponsor()));
        sponseeDTO.setMsisdn(FormatNumberPhoneUtil.extractNumberWithoutSuffix(sponseeDTO.getMsisdn()));

        Optional<Sponsee> sponseeBean = sponseeRepository.findOneByMsisdn(sponseeDTO.getMsisdn());
        if (sponseeBean.isPresent() && !sponseeBean.get().getAccountB2C().getNumero().equals(sponseeDTO.getMsisdnSponsor())) {
            throw new ForbiddenException();
        }

        Sponsee sponsee = sponseeMapper.toEntity(sponseeDTO);
        Optional<AccountB2C> accountB2C = accountB2CRepository.findOneByNumero(sponseeDTO.getMsisdnSponsor());

        if (accountB2C.isPresent()) {
            sponsee.setAccountB2C(accountB2C.get());
        }
        sponsee = sponseeRepository.save(sponsee);
        SponseeDTO toDto = sponseeMapper.toDto(sponsee);
        toDto.setMsisdnSponsor(sponsee.getAccountB2C().getNumero());
        return toDto;
    }

    @Async
    @Override
    public Sponsee updateEffectiveInscriptionOfSponsee(String msisdn) {
        log.debug("Request to update Sponsee : {}", msisdn);
        msisdn = FormatNumberPhoneUtil.extractNumberWithoutSuffix(msisdn);
        Optional<Sponsee> sponsee = sponseeRepository.findOneByMsisdn(msisdn);
        if (sponsee.isPresent()) {
            sponsee.get().setEffective(true);
            Sponsee saveSponsee = sponseeRepository.save(sponsee.get());
            MessageVM messageVM = new MessageVM();
            messageVM.setMsisdn(sponsee.get().getAccountB2C().getNumero());
            messageVM.setMessage(
                String.format(applicationProperties.getSendSms().getSponsorship().getSmsSponsor(), saveSponsee.getMsisdn())
            );

            smsNotificationService.sendSMSPP(messageVM.getMsisdn(), messageVM.getMessage(), messageVM.getSourceAddress());
            return saveSponsee;
        }
        return new Sponsee();
    }

    private void checkNumberSponsee(String msisdnSource, String msisdnDest) {
        Optional<Sponsee> sponsee = sponseeRepository.findOneByMsisdn(msisdnDest);
        if (sponsee.isPresent() && Boolean.TRUE.equals(sponsee.get().isEnabled())) {
            if (!sponsee.get().getAccountB2C().getNumero().equals(msisdnSource)) {
                throw new ForbiddenException();
            }
            if (Boolean.TRUE.equals(sponsee.get().isEffective())) {
                throw new BadRequestAlertException(ErrorMessages.NUMBER_IS_ALREADY_REGISTERED, ENTITY_NAME, "userExiste");
            }
        } else throw new BadRequestAlertException(ErrorMessages.NUMBER_ALREADY_SPONSORED, ENTITY_NAME, "sponseeNotFound");
    }

    private void checkDisponibilityOfNumberForSponsored(String msisdnSponsee) {
        Optional<RattachementLigne> rattachementLigne = rattachementLigneRepository.findByNumero(msisdnSponsee);
        if (rattachementLigne.isPresent()) {
            log.debug("Error Request Service msisdn is rattached : {}", msisdnSponsee);
            throw new LigneAlreadyRattachedException();
        }

        Optional<AccountB2C> accountB2C = accountB2CRepository.findOneByNumero(msisdnSponsee);
        if (accountB2C.isPresent()) {
            log.debug("Error Request Service msisdn have an account : {}", msisdnSponsee);
            throw new LoginAlreadyUsedException();
        }

        Optional<Sponsee> sponsee = sponseeRepository.findOneByMsisdn(msisdnSponsee);
        if (sponsee.isPresent() && Boolean.TRUE.equals(sponsee.get().isEnabled())) {
            log.debug("Error Request Service msisdn is sponsored : {}", msisdnSponsee);
            throw new BadRequestAlertException(ErrorMessages.NUMBER_IS_ALREADY_REGISTERED, ENTITY_NAME, "userExiste");
        }
    }

    private void checkNumberOfSponsor(String msisdSponsor) {
        Optional<RattachementLigne> rattachementLigne = rattachementLigneRepository.findByNumero(msisdSponsor);
        Optional<AccountB2C> accountB2C = accountB2CRepository.findOneByNumero(msisdSponsor);
        if (!rattachementLigne.isPresent() && !accountB2C.isPresent()) {
            log.debug("Error Request Service  msisdn of sponsor hav'nt a account  : {}", msisdSponsor);
            throw new BadRequestAlertException(ErrorMessages.MSISDN_DOES_NOT_HAVE_AN_ACCOUNT, ENTITY_NAME, "notFoundSponsor");
        }
    }

    private String getMessagePromo(String msisdn, String trigger) {
        List<BoosterPromo> boosterPromos = boosterManagerService.getActiveWelcomeBoosterValue(msisdn, trigger);
        if (boosterPromos != null && !boosterPromos.isEmpty()) {
            StringBuilder values = new StringBuilder();
            for (BoosterPromo promo : boosterPromos) {
                if (promo.getGift().getType().equals(BoosterPromo.Gift.GiftType.COUPON)) {
                    values.append(BoosterPromo.Gift.GiftType.COUPON + " " + promo.getGift().getPartner().getName() + ", ");
                } else if (promo.getGift().getValue() != null && promo.getGift().getType().equals(BoosterPromo.Gift.GiftType.RECHARGE)) {
                    if (promo.getGift().getValueType().equals(BoosterPromo.Gift.ValueType.AMONT)) {
                        values.append(promo.getGift().getValue() + FRCFA + ", ");
                    } else if (promo.getGift().getValueType().equals(BoosterPromo.Gift.ValueType.PERCENTAGE)) {
                        values.append(promo.getGift().getValue() + POURCENTAGE + ", ");
                    }
                }
            }

            String messagePromo = String.format(applicationProperties.getSendSms().getSponsorship().getSmsPromo(), values.toString());
            return String.format(applicationProperties.getSendSms().getSponsorship().getSmsSponsee(), messagePromo);
        } else return "";
    }
}
