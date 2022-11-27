package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.ClientType;
import sn.sonatel.dsi.dif.selfcare.b2c.service.AbonneService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.MailService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.PartyManagementApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.*;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.FormatNumberPhoneUtil;

import java.util.HashSet;
import java.util.Set;

@Service
public class AbonneServiceImpl implements AbonneService {

    private final Logger log = LoggerFactory.getLogger ( AbonneServiceImpl.class );
    private final PartyManagementApiClient partyManagementApiClient;
    private final MailService mailService;

    public AbonneServiceImpl(PartyManagementApiClient partyManagementApiClient, MailService mailService) {
        this.partyManagementApiClient = partyManagementApiClient;
        this.mailService = mailService;
    }

    @Override
    public AbonneDTO getInformationAbonne(String msisdn){
        log.debug("Service get information msisdn: {}", msisdn);
        msisdn = FormatNumberPhoneUtil.extractNumberWithoutSuffix(msisdn);
        AbonneDTO abonneDTO = new AbonneDTO();
        InfoClientWrapper infoClientWrapper;

        infoClientWrapper = getInformations(msisdn);
            ClientType clientType = infoClientWrapper.getClientType();
        switch (clientType){
            case ORGANIZATION:
                abonneDTO.setMsisdn(infoClientWrapper.getOrganization().getId());
                return abonneDTO;

            case INDIVIDUAL:
                abonneDTO.setNomAbonne(infoClientWrapper.getInformation().getFamilyName());
                abonneDTO.setPrenomAbonne(infoClientWrapper.getInformation().getGivenName());
                abonneDTO.setMsisdn(infoClientWrapper.getInformation().getId());
                return abonneDTO;
            default:
                return abonneDTO;
        }
    }


    public InfoClientWrapper getInformations(String msisdn){

        InfoClientWrapper infoClientWrapper = new InfoClientWrapper();

        ResponseEntity<IndividualInformation> informationResponseEntity = partyManagementApiClient.getIndividualInformation(msisdn);

        if (informationResponseEntity.getStatusCode() == HttpStatus.OK && informationResponseEntity.getBody() != null) {
            infoClientWrapper.setClientType(ClientType.INDIVIDUAL);
            infoClientWrapper.setInformation(informationResponseEntity.getBody());
            return infoClientWrapper;
        }

        ResponseEntity<OrganizationInformation> responseEntity = partyManagementApiClient.getOrganizationInformation(msisdn);

            if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
                infoClientWrapper.setClientType(ClientType.ORGANIZATION);
                infoClientWrapper.setOrganization(responseEntity.getBody());
                 return infoClientWrapper;
            }

        return infoClientWrapper;
    }

    @Override
    public IndividualInformation getIndividualInformations(String msisdn) {

        InfoClientWrapper infoClientWrapper = getInformations(msisdn);
        if(infoClientWrapper.getClientType() == ClientType.INDIVIDUAL){
            return infoClientWrapper.getInformation();
        }
        else {
            throw new BadRequestAlertException("Ce numéro est rattaché a une entreprise","infos","msisdn.infos");
        }

    }

    @Override
    public boolean isCoorporateNumber(String msisdn) {
        InfoClientWrapper informations = getInformations(msisdn);
        return (informations.getClientType().equals(ClientType.ORGANIZATION));
    }


    @Override
    public boolean isOrangeNumber(String msisdn){
        log.debug("Service check number orange information msisdn: {}", msisdn);
        AbonneDTO informationAbonne = getInformationAbonne(msisdn);
        return informationAbonne.getMsisdn() != null;
    }

    @Override
    public Set<String> getMyContactNumbers(String msisdn){
        InfoClientWrapper informationsClientWrapper = getInformations(msisdn);

        if(informationsClientWrapper.getClientType().equals(ClientType.INDIVIDUAL)){
            return informationsClientWrapper.getInformation().getContactNumbers();
        }

        return new HashSet<>();
    }

    @Override
    public ResponseEntity<String> getNumberStatus(String msisdn) {
        log.debug("Service get Status Number: {}", msisdn);
        InfoClientWrapper informations = getInformations(msisdn);
        if(informations.getClientType().equals(ClientType.INDIVIDUAL)){
           return ResponseEntity.ok(informations.getInformation().getStatus());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * This method is for sending clent trouble to orange client service
     * @param troubleSignalingDTO client trouble info
     */
    @Override
    public void sendToClientService(TroubleSignalingDTO troubleSignalingDTO) {
        log.debug("Service get send client trouble: {}", troubleSignalingDTO);
        mailService.sendDerangementMailFromTemplate(troubleSignalingDTO);
    }
}

