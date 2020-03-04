package sn.sonatel.dsi.dif.selfcare.b2c.service;



import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.NotificationInformationDTO;

import java.util.List;

public interface NotificationInformationService {

   List<NotificationInformationDTO> getNotificationInformationByCodeFormule(String codeFormule);

    void updateCodeFormuleByMsisdn(NotificationInformationDTO informationDTO);

    List<NotificationInformationDTO> getFirebaseIdByMsisdn(List<String> listMsisdn);

    void register(NotificationInformationDTO informationDTO);

    List<NotificationInformationDTO> getNotificationInformationByListCodeFormule(List<String> codeFormule);
}
