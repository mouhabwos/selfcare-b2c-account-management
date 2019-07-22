package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Mail;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.StatusMail;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.MailSendRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.UrgenceDepannageService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.mailmanagment.ServiceSendMail;
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

@Service
class UrgenceDepannageServiceImpl implements UrgenceDepannageService {

    private static final String RECTO = "_recto_";
    private static final String VERSO = "_verso_";

    private final MailSendRepository mailSendRepository;


    private final ServiceSendMail serviceSendMail;

    UrgenceDepannageServiceImpl(MailSendRepository mailSendRepository, ServiceSendMail serviceSendMail) {
        this.mailSendRepository = mailSendRepository;
        this.serviceSendMail = serviceSendMail;
    }

    @Override
    public String ouvertureCompte( OperationDTO operationDTO) throws IOException {


        // verification of validity files
        operationDTO.checkFormatImageFile(operationDTO.getRectoID().getOriginalFilename());

        operationDTO.checkFormatPDFFile(operationDTO.getFormulaire().getOriginalFilename());

        operationDTO.setOperationTitre(serviceSendMail.getTitleOperation(operationDTO.getOperationCode()));
        if(operationDTO.getOperationTitre().equals("")){
            throw new BadRequestAlertException("Le code de l operation est introuvable", operationDTO.getOperationCode(),"");
        }

        String idFormulaire = "";
        String idRecto = "";
        String idVerso = "";


        Map<String, DataSource> dataSource = new HashMap<>();

        Date date = new Date();
        long millis = date.getTime();

        DataSource dsFormulaire = new ByteArrayDataSource(operationDTO.getFormulaire().getBytes(), operationDTO.getFormulaire().getContentType());
        DataSource dsRecto = new ByteArrayDataSource(operationDTO.getRectoID().getBytes(), operationDTO.getRectoID().getContentType());
        idFormulaire = operationDTO.getNumero()+"_"+millis;
        dataSource.put(idFormulaire, dsFormulaire);

        idRecto = operationDTO.getNumero()+ RECTO +millis;
        dataSource.put(idRecto, dsRecto);


        if(operationDTO.getVerso() != null){
            operationDTO.checkFormatImageFile(operationDTO.getVerso().getOriginalFilename());
            DataSource dsVerso = new ByteArrayDataSource(operationDTO.getVerso().getBytes(), operationDTO.getVerso().getContentType());
            idVerso = operationDTO.getNumero()+VERSO+millis;
            dataSource.put(idVerso, dsVerso);
        }

        Mail mail = new Mail();

        mail.setIdRequest(operationDTO.getNumero()+"_"+millis);
        mail.setStatus(StatusMail.IN_PROGRESS);
        mail.setEmail(operationDTO.getEmail());
        mail.setIdFormulaire(idFormulaire);
        mail.setIdVerso(idVerso);
        mail.setIdRecto(idRecto);
        mail.setOperationTitre(operationDTO.getOperationTitre());
        mail.setFirsName(operationDTO.getFirsName());
        mail.setLastName(operationDTO.getLastName());
        mail.setNumero(operationDTO.getNumero());

        mail = mailSendRepository.save(mail);

        serviceSendMail.sendEmailToServiceClient(dataSource, mail);

        return mail.getIdRequest()+"";


    }




}
