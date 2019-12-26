package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.ClientType;
import sn.sonatel.dsi.dif.selfcare.b2c.service.AbonneService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.PartyManagementApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.IndividualInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.InfoClientWrapper;
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
        InfoClientWrapper infoClientWrapper;

        infoClientWrapper = getInformations(msisdn);
            ClientType clientType = infoClientWrapper.getClientType();
        switch (clientType){
            case ORGANIZATION:
                abonneDTO.setNomAbonne("");
                abonneDTO.setPrenomAbonne("");
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
    public boolean isOrangeNumber(String msisdn){
        log.debug("Service check number orange information msisdn: {}", msisdn);
        AbonneDTO informationAbonne = getInformationAbonne(msisdn);
        return informationAbonne.getMsisdn() != null;
    }


}

