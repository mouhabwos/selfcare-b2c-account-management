package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Mail;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.StatusMail;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.MailSendRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.SFTPClientService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.UrgenceDepannageService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.OperationDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 *
 *
 */

@Transactional
@Service
class UrgenceDepannageServiceImpl implements UrgenceDepannageService {

    private final MailSendRepository mailSendRepository;

    private final ApplicationProperties applicationProperties;

    private final SFTPClientService ftpService;

    UrgenceDepannageServiceImpl(MailSendRepository mailSendRepository, ApplicationProperties applicationProperties, SFTPClientService ftpService) {
        this.mailSendRepository = mailSendRepository;
        this.applicationProperties = applicationProperties;
        this.ftpService = ftpService;
    }

    @Override
    public String ouvertureCompte( String dto, MultipartFile formulaire, MultipartFile rectoID,  MultipartFile versoID, String canal) throws IOException {


        OperationDTO operationDTO = convertStringToOperationDTO(dto);
        operationDTO.setFormulaire(formulaire);
        operationDTO.setVerso(versoID);
        operationDTO.setRectoID(rectoID);
        if(canal != null && !canal.equals("")){
            operationDTO.setCanal(canal);
        }

        List<MultipartFile> multipartFiles = new ArrayList<>();
        multipartFiles.add(rectoID);
        multipartFiles.add(formulaire);
        if(versoID != null){
            multipartFiles.add(versoID);
        }

        Date date = new Date();
        long millis = date.getTime();

        // verification of validity files
        operationDTO.checkFormatImageFile(operationDTO.getRectoID().getOriginalFilename());
        operationDTO.checkFormatPDFFile(operationDTO.getFormulaire().getOriginalFilename());

        if(operationDTO.getVerso() != null){
            operationDTO.checkFormatImageFile(operationDTO.getVerso().getOriginalFilename());
        }

        // get operation title
        operationDTO.setOperationTitre(getTitleOperation(operationDTO.getOperationCode()));

        if(operationDTO.getOperationTitre().equals("")){
            throw new BadRequestAlertException("Le code de l operation est introuvable", operationDTO.getOperationCode(),"");
        }


        Mail mail = sauvegardeFiles(operationDTO,millis);

        // send to server ftp

        String zipFiles = ftpService.zipFiles(multipartFiles, constructionNameFile(operationDTO.getNumero(), operationDTO.getCanal()));
        ftpService.sendFileToServerFtp(zipFiles);

        return  mail.getIdRequest()+"";

    }

    private Mail sauvegardeFiles(OperationDTO operationDTO, long millis){

        Mail mail = new Mail();
        if(operationDTO.getVerso()!= null){
            mail.setIdVerso(operationDTO.getVerso().getOriginalFilename());
        }

        mail.setIdRequest(operationDTO.getNumero()+"_"+millis);
        mail.setStatus(StatusMail.IN_PROGRESS);
        mail.setEmail(operationDTO.getEmail());
        mail.setIdFormulaire(operationDTO.getFormulaire().getOriginalFilename());
        mail.setIdRecto(operationDTO.getRectoID().getOriginalFilename());
        mail.setOperationTitre(operationDTO.getOperationTitre());
        mail.setFirsName(operationDTO.getFirstName());
        mail.setLastName(operationDTO.getLastName());
        mail.setNumero(operationDTO.getNumero());

         return mailSendRepository.save(mail);

    }

    private OperationDTO convertStringToOperationDTO(String operationDTO) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(operationDTO, OperationDTO.class);


    }

    private String getTitleOperation(String codeOperation){

        String title = "";

        for (ApplicationProperties.UrgenceDepannage.Operation operation1:
            applicationProperties.getUrgenceDepannage().getOperation()){
            if(operation1.getCode().equals(codeOperation)){
                title = operation1.getTitle();

            }

        }
        return title;
    }

    private String constructionNameFile(String msisdn, String canal){

        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedString = zonedDateTime.format(formatter);

        return msisdn+"_"+formattedString+"_"+canal;
    }




}
