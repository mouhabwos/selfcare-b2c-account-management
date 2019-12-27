package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Mail;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.StatusMail;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.MailSendRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.UrgenceDepannageService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.OperationDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 *
 *
 */

@Transactional
@Service
class UrgenceDepannageServiceImpl implements UrgenceDepannageService {

    private static final String RECTO = "_recto_";
    private static final String FILE = "file";
    private static final String FORMULAIRE = "_formulaire_";
    private static final String TEXT = "text/plain";

    private final MailSendRepository mailSendRepository;

    private final ApplicationProperties applicationProperties;

    UrgenceDepannageServiceImpl(MailSendRepository mailSendRepository, ApplicationProperties applicationProperties) {
        this.mailSendRepository = mailSendRepository;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public String ouvertureCompte( String dto, MultipartFile formulaire, MultipartFile rectoID,  MultipartFile versoID) throws IOException {


        OperationDTO operationDTO = convertStringToOperationDTO(dto);
        operationDTO.setFormulaire(formulaire);
        operationDTO.setVerso(versoID);
        operationDTO.setRectoID(rectoID);

        Date date = new Date();
        long millis = date.getTime();

        // verification of validity files
        operationDTO.checkFormatImageFile(operationDTO.getRectoID().getOriginalFilename());
        operationDTO.checkFormatPDFFile(operationDTO.getFormulaire().getOriginalFilename());

        // get operation title
        operationDTO.setOperationTitre(getTitleOperation(operationDTO.getOperationCode()));

        if(operationDTO.getOperationTitre().equals("")){
            throw new BadRequestAlertException("Le code de l operation est introuvable", operationDTO.getOperationCode(),"");
        }

        String idFormulaire = operationDTO.getNumero()+FORMULAIRE+millis;
        String idRecto = operationDTO.getNumero()+ RECTO +millis;



        Map<String, DataSource> dataSource = new HashMap<>();

        DataSource dsFormulaire = new ByteArrayDataSource(operationDTO.getFormulaire().getBytes(), operationDTO.getFormulaire().getContentType());
        DataSource dsRecto = new ByteArrayDataSource(operationDTO.getRectoID().getBytes(), operationDTO.getRectoID().getContentType());

        dataSource.put(operationDTO.getFormulaire().getOriginalFilename(), dsFormulaire);


        dataSource.put(operationDTO.getRectoID().getOriginalFilename(), dsRecto);

        if(operationDTO.getVerso() != null){
            operationDTO.checkFormatImageFile(operationDTO.getVerso().getOriginalFilename());
            DataSource dsVerso = new ByteArrayDataSource(operationDTO.getVerso().getBytes(), operationDTO.getVerso().getContentType());

            dataSource.put(operationDTO.getVerso().getOriginalFilename(), dsVerso);

            MultipartFile verso  = new MockMultipartFile(FILE, idRecto+getExtension(operationDTO.getVerso().getOriginalFilename()), TEXT, IOUtils.toByteArray(operationDTO.getVerso().getInputStream()));
            operationDTO.setVerso(verso);
        }

        MultipartFile pdf  = new MockMultipartFile(FILE, idFormulaire+getExtension(operationDTO.getFormulaire().getOriginalFilename()), TEXT, IOUtils.toByteArray(operationDTO.getFormulaire().getInputStream()));
        MultipartFile recto  = new MockMultipartFile(FILE, idRecto+getExtension(operationDTO.getRectoID().getOriginalFilename()), TEXT, IOUtils.toByteArray(operationDTO.getRectoID().getInputStream()));

        operationDTO.setFormulaire(pdf);
        operationDTO.setRectoID(recto);

        Mail mail = sauvegardeFiles(operationDTO,millis);

        return mail.getIdRequest()+"";

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

    private String getExtension(String name){
        StringBuilder bld = new StringBuilder();
        boolean trouve = false;
        for ( int i = 0; i < name.length(); ++i ) {
            char c = name.charAt( i );
            if(c == '.'){
                trouve = true;
            }
            if(trouve){
                bld.append(c);
            }
        }
        return bld.toString();
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

}
