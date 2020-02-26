package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.NotificationInformationRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.NotificationInformationService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.NotificationInformationDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.mapper.NotificationInformationMapper;

import java.util.List;

@Service
public class NotificationInformationServiceImpl implements NotificationInformationService {

    private final Logger log = LoggerFactory.getLogger ( NotificationInformationServiceImpl.class );

    private final NotificationInformationRepository notificationInformationRepository;
    private final NotificationInformationMapper notificationInformationMapper;

    public NotificationInformationServiceImpl(NotificationInformationRepository notificationInformationRepository, NotificationInformationMapper notificationInformationMapper) {
        this.notificationInformationRepository = notificationInformationRepository;
        this.notificationInformationMapper = notificationInformationMapper;
    }


    @Override
    public List<NotificationInformationDTO> getNotificationInformationByCodeFormule(String codeFormule) {
        log.debug ( "SERVICE to get msisdn and firebaseId for code formule  {}", codeFormule );
        return notificationInformationMapper.toDto(notificationInformationRepository.findOneByCodeFormule(codeFormule));
    }
}
