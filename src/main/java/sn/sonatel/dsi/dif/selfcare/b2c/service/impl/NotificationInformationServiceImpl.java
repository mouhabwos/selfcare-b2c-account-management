package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.NotificationInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.OfferTypeEnum;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.NotificationInformationRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.NotificationInformationService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement.CustomerOfferService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.NotificationInformationDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.mapper.NotificationInformationMapper;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.LigneNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationInformationServiceImpl implements NotificationInformationService {

    private final Logger log = LoggerFactory.getLogger ( NotificationInformationServiceImpl.class );

    private static final String CODE_FORMULE_HYBRIDE = "9132";
    private final NotificationInformationRepository notificationInformationRepository;
    private final NotificationInformationMapper notificationInformationMapper;
    private final CustomerOfferService customerOfferService;
    private final AccountB2CRepository accountB2CRepository;

    public NotificationInformationServiceImpl(NotificationInformationRepository notificationInformationRepository, NotificationInformationMapper notificationInformationMapper, AccountB2CRepository accountB2CRepository, CustomerOfferService customerOfferService) {
        this.notificationInformationRepository = notificationInformationRepository;
        this.notificationInformationMapper = notificationInformationMapper;
        this.customerOfferService = customerOfferService;
        this.accountB2CRepository = accountB2CRepository;
    }


    @Override
    public List<NotificationInformationDTO> getNotificationInformationByCodeFormule(String codeFormule) {
        log.debug ( "SERVICE to get msisdn and firebaseId for code formule  {}", codeFormule );
        return notificationInformationMapper.toDto(notificationInformationRepository.findOneByCodeFormule(codeFormule));
    }

    @Override
    public void updateCodeFormuleByMsisdn(NotificationInformationDTO informationDTO){
        log.debug ("Service to update NotificationInformation  {}", informationDTO);
        Optional<NotificationInformation> byAccountB2CNumero = notificationInformationRepository.findOneByAccountB2CNumero(informationDTO.getMsisdn());
        if(byAccountB2CNumero.isPresent()){
            if(informationDTO.getFirebaseId() != null && !informationDTO.getFirebaseId().equals("")){
                byAccountB2CNumero.get().setFirebaseId(informationDTO.getFirebaseId());
            }
            byAccountB2CNumero.get().setCodeFormule(informationDTO.getCodeFormule());
            notificationInformationRepository.save(byAccountB2CNumero.get());
        }else throw new LigneNotFoundException();
    }

    @Override
    public List<NotificationInformationDTO> getFirebaseIdByMsisdn(List<String> listMsisdn) {
        log.debug ( "SERVICE request to get FirebaseId By Msisdn with list msisdn");
        return notificationInformationMapper.toDto(notificationInformationRepository.findAllByAccountB2CNumeroIn(listMsisdn));
    }

    @Override
    public void register(NotificationInformationDTO informationDTO) {
        log.debug ("Service to register NotificationInformation  {}", informationDTO);
        Optional<NotificationInformation> notificationInformation = notificationInformationRepository.findOneByAccountB2CNumero(informationDTO.getMsisdn());
        if(notificationInformation.isPresent()){
            throw new BadRequestAlertException("Les informations pour ce numéro sont deja renseignées","","");
        }
        Optional<AccountB2C> oneByNumero = accountB2CRepository.findOneByNumero(informationDTO.getMsisdn());
        if(oneByNumero.isPresent()){

            NotificationInformation information = notificationInformationMapper.toEntity(informationDTO);
            information.setAccountB2C(oneByNumero.get());
            NotificationInformation save = notificationInformationRepository.save(information);
            log.debug ("Service to register NotificationInformation  after registration {}", save);
        }else throw new LigneNotFoundException();

    }

    @Override
    public List<NotificationInformationDTO> getNotificationInformationByListCodeFormule(List<String> codeFormule) {
        log.debug ( "SERVICE request to get FirebaseId By Msisdn with list codeFormule");
        return notificationInformationMapper.toDto(notificationInformationRepository.findAllByCodeFormuleIn(codeFormule));
    }

    @Override
    public void addCodeFormuleCustomerOffer(String msisdn){
        log.debug ("Service to add codeFormule  for new user registration {}", msisdn);
        CustomerOffer customerOffer = customerOfferService.getCustomerOffer(msisdn);
        NotificationInformationDTO informationDTO = new NotificationInformationDTO();
        informationDTO.setMsisdn(msisdn);

        informationDTO.setCodeFormule(findCodeFormule(customerOffer.getOfferType(), customerOffer.getOfferId()));

        register(informationDTO);
    }

    @Override
    public void updateNotificationInformationFormulCode(String msisdn){

        log.debug ("Service to update NotificationInformation from customer OFFER {}", msisdn);
        Optional<NotificationInformation> byAccountB2CNumero = notificationInformationRepository.findOneByAccountB2CNumero(msisdn);
        if(byAccountB2CNumero.isPresent()){
            CustomerOffer customerOffer = customerOfferService.getCustomerOffer(msisdn);
            NotificationInformationDTO informationDTO = new NotificationInformationDTO();
            informationDTO.setMsisdn(msisdn);
            informationDTO.setCodeFormule(findCodeFormule(customerOffer.getOfferType(), customerOffer.getOfferId()));

            updateCodeFormuleByMsisdn(informationDTO);

        } else addCodeFormuleCustomerOffer(msisdn);
    }

    private String findCodeFormule(OfferTypeEnum offerType, String codeFormule){
        return (OfferTypeEnum.HYBRIDE.equals(offerType))? CODE_FORMULE_HYBRIDE:codeFormule;
    }
}
