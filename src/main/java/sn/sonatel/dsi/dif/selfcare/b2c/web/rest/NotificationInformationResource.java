package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.sonatel.dsi.dif.selfcare.b2c.service.NotificationInformationService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.NotificationInformationDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Message;
import sn.sonatel.dsi.dif.selfcare.utils.selfcarelogging.annotation.Auditable;

import java.util.List;

@RestController
@RequestMapping("/api/notification-information")
public class NotificationInformationResource {

    private final Logger log = LoggerFactory.getLogger ( NotificationInformationResource.class );

    private final NotificationInformationService notificationInformationService;

    public NotificationInformationResource(NotificationInformationService notificationInformationService) {
        this.notificationInformationService = notificationInformationService;
    }


    @Auditable(description = Message.NotificationInformation.MSISDN_FIREBASEID_BY_CODE_FORMULE)
    @GetMapping("/{codeFormule}")
    @PreAuthorize("hasRole('ROLE_B2C_ADMIN_MARKETING')")
    public ResponseEntity<List<NotificationInformationDTO>> getNotificationInformationByCodeFormule(@PathVariable String codeFormule) {
        log.debug ( "REST request to get msisdn and firebaseId for code formule  {}", codeFormule );
        return ResponseEntity.ok(notificationInformationService.getNotificationInformationByCodeFormule(codeFormule));
    }

    @Auditable(description = Message.NotificationInformation.UPDATE_CODE_FORMULE_BY_MSISDN)
    @PutMapping
    @PreAuthorize("#informationDTO.msisdn == authentication.name")
    public ResponseEntity updateCodeFormuleByMsisdn(@RequestBody NotificationInformationDTO informationDTO) {
        log.debug ( "REST request to update for msisdn {}", informationDTO );
        notificationInformationService.updateCodeFormuleByMsisdn(informationDTO);
        return ResponseEntity.ok().build();
    }

}
