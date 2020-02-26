package sn.sonatel.dsi.dif.selfcare.b2c.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.NotificationInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.NotificationInformationDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationInformationMapper extends EntityMapper<NotificationInformationDTO, NotificationInformation> {

    @Mapping(source = "accountB2C.numero", target = "msisdn")
    NotificationInformationDTO toDto(NotificationInformation notificationInformation);

    List<NotificationInformationDTO> toDto(List<NotificationInformation> notificationInformations);

}
