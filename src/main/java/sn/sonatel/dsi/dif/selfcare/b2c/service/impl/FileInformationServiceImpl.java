package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.FileInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.SearchFilterItem;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.FileInformationRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.FileInformationService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;

import javax.batch.runtime.BatchStatus;
import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;

@Service
public class FileInformationServiceImpl implements FileInformationService {

    private static final Logger log = LoggerFactory.getLogger(FileInformationServiceImpl.class);

    private final FileInformationRepository fileInformationRepository;
    private final RestTemplate restTemplate;
    private final ApplicationProperties applicationProperties;

    public FileInformationServiceImpl(FileInformationRepository fileInformationRepository, @Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate, ApplicationProperties applicationProperties) {
        this.fileInformationRepository = fileInformationRepository;
        this.restTemplate = restTemplate;
        this.applicationProperties = applicationProperties;
    }


    @Override
    public FileInformation save(String sourceFile, Date dateCreation, String login) {
        log.debug("Service request to upload and save information upload file with information : {}", sourceFile);
        FileInformation information = new FileInformation();
        try {

            //Get file into tmp path
            File file = new File(sourceFile);

            //upload file into fileManager
            ResponseEntity<String> responseEntity = uploadFile(file);

            information.setCreatedByUser(login);
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(dateCreation.toInstant(),
                ZoneId.systemDefault());
            information.setCreatedDate(zonedDateTime);

            // Save information of File
            if(responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody()!= null){
                information.setFileName(responseEntity.getBody());
                information.setStatus(BatchStatus.COMPLETED.name());
                fileInformationRepository.save(information);
            }else {
                information.setFileName(sourceFile);
                information.setStatus(BatchStatus.FAILED.name());
                fileInformationRepository.save(information);
            }
        }catch (Exception e){
            log.error("Error with message : {}", e.getMessage());
        }

        return information;
    }

    @Override
    public Page<FileInformation> getInformationFileUploaded(SearchFilterItem searchFilterItem, Pageable pageable, ZonedDateTime startDate, ZonedDateTime endDate, String user) {
        log.debug("Service request to get information of file uploaded : {}, {}, {}, {}", searchFilterItem, startDate, endDate, user);

        Page<FileInformation> page = new PageImpl<>(new ArrayList<>());

        switch(searchFilterItem) {
            case BETWEEN_TWO_DATES:
                if(startDate != null && endDate != null && user != null){
                    page = fileInformationRepository.findAllByCreatedDateBetweenAndCreatedByUserOrderByCreatedDate(startDate, endDate, user, pageable);
                }else {
                    throw new BadRequestAlertException("Les champs startDate, endDate et user sont obligatoire","","");
                }
                break;

            case BY_USER:
                if( user == null){
                    throw new BadRequestAlertException("Veuillez renseigner le login de l'utilisateur à rechercher","","");
                }
                page = fileInformationRepository.findAllByCreatedByUser( user, pageable);
                break;

            case ALL:
                page = fileInformationRepository.findAll(pageable);
                break;
        }

        return page;
    }


    private ResponseEntity<String> uploadFile(File file){
        log.debug("Service request to upload file in fileManager");
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("file", new FileSystemResource(file));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(parameters, headers);
        String urlApiUpload = applicationProperties.getSelfcareB2cFileManager()+"/api/upload";
        return restTemplate.exchange(urlApiUpload,
            HttpMethod.POST, requestEntity, String.class);
    }

}
