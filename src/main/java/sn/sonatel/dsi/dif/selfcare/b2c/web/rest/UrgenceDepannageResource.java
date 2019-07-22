package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.sonatel.dsi.dif.selfcare.b2c.service.MailSendService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.UrgenceDepannageService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.OperationDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Message;
import sn.sonatel.dsi.dif.selfcare.utils.selfcarelogging.annotation.Auditable;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
    @PostMapping(value = "/v1/mail/ouverture-compte",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity sendmail(@RequestParam String operationCode, @RequestParam String numero, @RequestParam String lastName, @RequestParam String firsName, @RequestParam String email, @RequestParam MultipartFile formulaire, @RequestParam MultipartFile rectoID, @RequestParam(required = false) MultipartFile verso) throws IOException, URISyntaxException {
        log.info("REST request to register ouverture-compte");

        OperationDTO operationDTO = new OperationDTO();
        operationDTO.setFirsName(firsName);
        operationDTO.setOperationCode(operationCode);
        operationDTO.setLastName(lastName);
        operationDTO.setNumero(numero);
        operationDTO.setEmail(email);
        operationDTO.setFormulaire(formulaire);
        operationDTO.setVerso(verso);
        operationDTO.setRectoID(rectoID);

        String idRequest = urgenceDepannageService.ouvertureCompte(operationDTO);

        return ResponseEntity.accepted ().body(new URI ( "/api/v1/mail/ouverture-compte/status/" +idRequest ));

    }


    @Auditable(description = Message.Account.STATUS_MAIL)
    @GetMapping(value = "/v1/mail/ouverture-compte/status/{idRequest}")
    public ResponseEntity getStatusMailSend(@PathVariable String idRequest){

        String statusMailSend = mailSendService.getStatusMailSend(idRequest);

        return ResponseEntity.ok().body(statusMailSend);
    }



}
