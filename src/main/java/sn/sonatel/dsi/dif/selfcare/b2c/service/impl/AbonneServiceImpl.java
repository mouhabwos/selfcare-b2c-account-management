package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.service.AbonneService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.PartyManagementApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.IndividualInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.OrganizationInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.FormatNumberPhoneUtil;

@Service
public class AbonneServiceImpl implements AbonneService {

    private final Logger log = LoggerFactory.getLogger ( AbonneServiceImpl.class );
    private final PartyManagementApiClient partyManagementApiClient;

    public AbonneServiceImpl(PartyManagementApiClient partyManagementApiClient) {
        this.partyManagementApiClient = partyManagementApiClient;
    }

    @Override
    public AbonneDTO getInformationAbonne(String msisdn){
        log.debug("Service get information msisdn: {}", msisdn);
        msisdn = FormatNumberPhoneUtil.extractNumberWithoutSuffix(msisdn);
        AbonneDTO abonneDTO = new AbonneDTO();
        InforClientWrapper inforClientWrapper;

        inforClientWrapper = getInformations(msisdn);
            ClientType clientType = inforClientWrapper.getClientType();
        switch (clientType){
            case INDIVIDUAL:
                abonneDTO.setNomAbonne("");
                abonneDTO.setPrenomAbonne("");
                abonneDTO.setMsisdn(inforClientWrapper.getOrganization().getId());
                return abonneDTO;

            case ORGANIZATION:
                abonneDTO.setNomAbonne(inforClientWrapper.getInformation().getFamilyName());
                abonneDTO.setPrenomAbonne(inforClientWrapper.getInformation().getGivenName());
                abonneDTO.setMsisdn(inforClientWrapper.getInformation().getId());
                return abonneDTO;
            default:
                return abonneDTO;
        }
    }


    private  InforClientWrapper getInformations(String msisdn){

        InforClientWrapper inforClientWrapper = new InforClientWrapper();

        ResponseEntity<IndividualInformation> informationResponseEntity = partyManagementApiClient.getIndividualInformation(msisdn);

        if (informationResponseEntity.getStatusCode() == HttpStatus.OK && informationResponseEntity.getBody() != null) {
            inforClientWrapper.setClientType(ClientType.ORGANIZATION);
            inforClientWrapper.setInformation(informationResponseEntity.getBody());
            return inforClientWrapper;
        }

        ResponseEntity<OrganizationInformation> responseEntity = partyManagementApiClient.getOrganizationInformation(msisdn);

            if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
                inforClientWrapper.setClientType(ClientType.INDIVIDUAL);
                inforClientWrapper.setOrganization(responseEntity.getBody());
                 return inforClientWrapper;
            }

        return inforClientWrapper;
    }



    @Override
    public boolean isOrangeNumber(String msisdn){
        log.debug("Service check number orange information msisdn: {}", msisdn);
        AbonneDTO informationAbonne = getInformationAbonne(msisdn);
        return informationAbonne.getMsisdn() != null;
    }


}

 class InforClientWrapper{

    private ClientType clientType = ClientType.NOT_FOUND;
    private IndividualInformation information = new IndividualInformation();
    private OrganizationInformation organization = new OrganizationInformation();

     public ClientType getClientType() {
         return clientType;
     }

     public void setClientType(ClientType clientType) {
         this.clientType = clientType;
     }

     public IndividualInformation getInformation() {
         return information;
     }

     public void setInformation(IndividualInformation information) {
         this.information = information;
     }

     public OrganizationInformation getOrganization() {
         return organization;
     }

     public void setOrganization(OrganizationInformation organization) {
         this.organization = organization;
     }
 }

 enum ClientType{
     INDIVIDUAL, ORGANIZATION,NOT_FOUND
 }
