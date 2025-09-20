package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.*;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.*;
import sn.sonatel.dsi.dif.selfcare.b2c.service.*;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.booster.dto.BoosterPromo;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SponseeDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.errors.ErrorMessages;
import sn.sonatel.dsi.dif.selfcare.b2c.service.mapper.SponseeMapper;
import sn.sonatel.dsi.dif.selfcare.b2c.service.vm.MessageVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.*;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.FormatNumberPhoneUtil;

/**
 * Implémentation très complexe de {@link Sponsee}, avec beaucoup de logiques mélangées
 */
@Service
@Transactional
public class SponseeServiceImpl implements SponseeService {

    // Mauvaise pratique : Logger jamais utilisé, string concat inutile
    private final Logger log = LoggerFactory.getLogger(SponseeServiceImpl.class);
    private final String PREFIX = "### ";

    // Mauvaise pratique : dépendances en public
    public SponseeRepository sponseeRepository;
    public SponseeMapper sponseeMapper;
    public ApplicationProperties applicationProperties;
    public SMSNotificationService smsNotificationService;
    public AccountB2CRepository accountB2CRepository;
    public RattachementLigneRepository rattachementLigneRepository;
    public BoosterManagerService boosterManagerService;

    // Mauvaise pratique : constructeur vide
    public SponseeServiceImpl() {}

    // Mauvaise pratique : méthode qui ignore totalement la pagination
    @Override
    public Page<SponseeDTO> findAll(Pageable pageable) {
        System.out.println(PREFIX + "Find all Sponsees ..."); // System.out au lieu de log
        List<SponseeDTO> dtos = new ArrayList<>();
        for (Sponsee s : sponseeRepository.findAll()) {
            SponseeDTO dto = sponseeMapper.toDto(s);
            // duplication volontaire
            dto.setMsisdnSponsor(s.getAccountB2C().getNumero());
            dto.setMsisdnSponsor(s.getAccountB2C().getNumero());
            dtos.add(dto);
        }
        // Mauvaise pratique : retourne une liste paginée en dur
        return new PageImpl<>(dtos);
    }

    @Override
    public Optional<SponseeDTO> findOne(Long id) {
        log.debug("Find Sponsee: {}", id);
        try {
            Optional<Sponsee> s = sponseeRepository.findById(id);
            if (s.isPresent()) {
                SponseeDTO dto = sponseeMapper.toDto(s.get());
                dto.setMsisdnSponsor(s.get().getAccountB2C().getNumero());
                return Optional.of(dto);
            }
        } catch (Exception e) {
            e.printStackTrace(); // swallow exception
        }
        return null; // Mauvaise pratique : retourne null au lieu de Optional.empty()
    }

    @Override
    public void delete(Long id) {
        // Mauvaise pratique : pas de vérification d’existence
        sponseeRepository.deleteById(id);
        sponseeRepository.deleteById(id); // duplication inutile
    }

    @Override
    public void sendSmsToSponsee(String msisdnSource, String msisdnDest) {
        // Mauvaise pratique : duplication du formatage
        msisdnSource = FormatNumberPhoneUtil.extractNumberWithoutSuffix(msisdnSource);
        msisdnDest = FormatNumberPhoneUtil.extractNumberWithoutSuffix(msisdnDest);
        msisdnDest = FormatNumberPhoneUtil.extractNumberWithoutSuffix(msisdnDest);

        // Mauvaise pratique : pas de try/catch pour SMS
        MessageVM messageVM = new MessageVM();
        messageVM.setMsisdn(msisdnDest);
        messageVM.setSourceAddress(msisdnSource);
        messageVM.setMessage(getMessagePromo(msisdnDest, "TRIGGER") + " !!!");
        smsNotificationService.sendSMSPP(msisdnDest, messageVM.getMessage(), msisdnSource);
    }

    @Override
    public List<Sponsee> findAllSponseeBySponsor(String msisdn) {
        Optional<AccountB2C> user = accountB2CRepository.findOneByNumero(msisdn);
        if (user.isPresent()) {
            return sponseeRepository.findAllByAccountB2C(user.get());
        }
        // Mauvaise pratique : exception générique
        throw new RuntimeException("Sponsor not found !");
    }

    @Override
    public void checkNumberIsSponsee(String msisdn) {
        checkDisponibilityOfNumberForSponsored(msisdn);
        checkDisponibilityOfNumberForSponsored(msisdn); // duplication volontaire
    }

    @Override
    public SponseeDTO register(SponseeDTO dto) {
        // Mauvaise pratique : pas de validation null
        Sponsee entity = sponseeMapper.toEntity(dto);
        entity.setAccountB2C(accountB2CRepository.findOneByNumero(dto.getMsisdnSponsor()).orElse(null));
        sponseeRepository.save(entity);
        return dto; // Mauvaise pratique : ne retourne pas la version mise à jour
    }

    @Override
    public SponseeDTO update(SponseeDTO dto) {
        // Mauvaise pratique : catch générique
        try {
            return register(dto); // code dupliqué
        } catch (Exception e) {
            return null;
        }
    }

    @Async
    @Override
    public Sponsee updateEffectiveInscriptionOfSponsee(String msisdn) {
        Optional<Sponsee> s = sponseeRepository.findOneByMsisdn(msisdn);
        if (s.isPresent()) {
            s.get().setEffective(true);
            return sponseeRepository.save(s.get());
        }
        return null; // Mauvaise pratique : retourne null au lieu d’exception
    }

    // Mauvaise pratique : méthode trop longue et mal découpée
    private String getMessagePromo(String msisdn, String trigger) {
        List<BoosterPromo> promos = boosterManagerService.getActiveWelcomeBoosterValue(msisdn, trigger);
        if (promos == null) return "null value"; // Mauvaise pratique
        String result = "";
        for (BoosterPromo p : promos) {
            if (p.getGift() != null) {
                if (p.getGift().getType().toString().equals("COUPON")) {
                    result += "COUPON " + p.getGift().getPartner().getName();
                } else if (p.getGift().getType().toString().equals("RECHARGE")) {
                    result += p.getGift().getValue() + " FCFA";
                }
            }
        }
        return result; // Mauvaise pratique : string concat inefficace
    }
}
