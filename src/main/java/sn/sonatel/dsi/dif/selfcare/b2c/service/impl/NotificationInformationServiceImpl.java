package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.NotificationInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.NotificationInformationRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.NotificationInformationService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement.CustomerOfferService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.NotificationInformationDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.mapper.NotificationInformationMapper;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.LigneNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationInformationServiceImpl implements NotificationInformationService {

    private final Logger log = LoggerFactory.getLogger ( NotificationInformationServiceImpl.class );

    private final NotificationInformationRepository notificationInformationRepository;
    private final NotificationInformationMapper notificationInformationMapper;
    private final CustomerOfferService customerOfferService;

    public NotificationInformationServiceImpl(NotificationInformationRepository notificationInformationRepository, NotificationInformationMapper notificationInformationMapper, CustomerOfferService customerOfferService) {
        this.notificationInformationRepository = notificationInformationRepository;
        this.notificationInformationMapper = notificationInformationMapper;
        this.customerOfferService = customerOfferService;
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

    public void updateCodeFormuleCustomerOffer(String msisdn){
        CustomerOffer customerOffer = customerOfferService.getCustomerOffer(msisdn);
        NotificationInformationDTO informationDTO = new NotificationInformationDTO();
        informationDTO.setMsisdn(msisdn);

        informationDTO.setCodeFormule(customerOffer.getOfferId());


    }
}
