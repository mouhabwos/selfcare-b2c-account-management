package sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.PartyManagementApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.IndividualInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.NotFoundNumberException;

@Service
public class PartyManagementService {

    private final PartyManagementApiClient partyManagementApiClient;

    public PartyManagementService(PartyManagementApiClient partyManagementApiClient) {
        this.partyManagementApiClient = partyManagementApiClient;
    }

    public IndividualInformation getIndividualInformation(String msisdn) {

        ResponseEntity<IndividualInformation> individualInformation = partyManagementApiClient.getIndividualInformation(msisdn);
        if (individualInformation.getStatusCode() == HttpStatus.OK && individualInformation.getBody() != null) {
            return individualInformation.getBody();
        }

        if (individualInformation.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new NotFoundNumberException("");

        }
        return new IndividualInformation();
    }
}
