package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.sonatel.dsi.dac.dif.ds.juf.middleware.logging.Auditable;
import sn.sonatel.dsi.dif.selfcare.b2c.service.MailSendService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.UrgenceDepannageService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Message;


import java.io.IOException;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 */

@RestController
@RequestMapping("/api")
public class UrgenceDepannageResource {

    private final Logger log = LoggerFactory.getLogger(UrgenceDepannageResource.class);

    private final UrgenceDepannageService urgenceDepannageService;

    private final MailSendService mailSendService;

    public UrgenceDepannageResource(UrgenceDepannageService urgenceDepannageService, MailSendService mailSendService) {
        this.urgenceDepannageService = urgenceDepannageService;
        this.mailSendService = mailSendService;
    }


    @Auditable(description = Message.Account.OUVERTURE_COMPTE)
    @PostMapping(value = "/v1/mail/ouverture-compte",consumes = {MediaType.ALL_VALUE})
    public ResponseEntity sendmail(@RequestPart String operationDTO, @RequestPart MultipartFile formulaire, @RequestPart MultipartFile rectoID, @RequestPart( required = false) MultipartFile verso, @RequestHeader(name = "X-CANAL", required = false) String canal) throws IOException {
        log.info("REST request to register ouverture-compte");

         urgenceDepannageService.ouvertureCompte(operationDTO, formulaire, rectoID, verso,canal);

        return ResponseEntity.accepted ().build();

    }


    @Auditable(description = Message.Account.STATUS_MAIL)
    @GetMapping(value = "/v1/mail/ouverture-compte/status/{idRequest}")
    public ResponseEntity getStatusMailSend(@PathVariable String idRequest){

        String statusMailSend = mailSendService.getStatusMailSend(idRequest);

        return ResponseEntity.ok().body(statusMailSend);
    }



}
