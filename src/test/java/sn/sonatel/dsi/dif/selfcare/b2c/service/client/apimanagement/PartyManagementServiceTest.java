package sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.PartyManagementApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.IndividualInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.OrganizationIdentification;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.NotFoundNumberException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PartyManagementServiceTest {

    @Mock
    private PartyManagementApiClient mockPartyManagementApiClient;

    private PartyManagementService partyManagementServiceUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        partyManagementServiceUnderTest = new PartyManagementService(mockPartyManagementApiClient);
    }

    private IndividualInformation getIndividualInformation(String msisdn){
        IndividualInformation information = new IndividualInformation();
        Set<OrganizationIdentification> individualIdentification = new HashSet<>();
        OrganizationIdentification identification = new OrganizationIdentification();

        identification.setExpiryDate("29-01-2025");
        identification.setHref("test");
        identification.setIdentificationId("CNI6436467");
        identification.setType("type");
        identification.setIssuingAuthority("jsfhh");
        identification.setIssuingDate("test");
        individualIdentification.add(identification);

        information.setId(msisdn);
        information.setFamilyName("ka");
        information.setTitle("Monsieur");

        information.setMaritalStatus("celibataire");
        information.setGender("");
        information.setStatus("");
        information.setContactNumbers(new HashSet<>());
        information.setGivenName("");
        information.setBirthDate("");

        information.setIndividualIdentification(individualIdentification);


        return information;
    }
    @Test(expected = NotFoundNumberException.class)
    public void testGetIndividualInformationWithNotFoundNumberException() {
        // Setup
        final String msisdn = "msisdn";
        /*final IndividualInformation expectedResult = null;*/

        ResponseEntity response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        when(mockPartyManagementApiClient.getIndividualInformation(Mockito.anyString())).thenReturn(response);

        // Run the test
        partyManagementServiceUnderTest.getIndividualInformation(msisdn);

    }

    @Test
    public void testGetIndividualInformationSuccess() {
        // init val
        final String msisdn = "77000 00 00";
        IndividualInformation information = getIndividualInformation(msisdn);


        ResponseEntity response = ResponseEntity.status(HttpStatus.OK).body(information);

        when(mockPartyManagementApiClient.getIndividualInformation(Mockito.anyString())).thenReturn(response);


        // Run the test
        final IndividualInformation result = partyManagementServiceUnderTest.getIndividualInformation(msisdn);

        // Verify the results
        assertEquals(information.getId(), result.getId());
        assertEquals(information.getFamilyName(), result.getFamilyName());
        assertEquals(information.getContactNumbers(), result.getContactNumbers());
        assertEquals(information.getBirthDate(), result.getBirthDate());
        assertEquals(information.getTitle(), result.getTitle());
        assertEquals(information.getGender(), result.getGender());
        assertEquals(information.getGivenName(), result.getGender());
        assertEquals(information.getStatus(), result.getGender());
        assertEquals(information.getMaritalStatus(), result.getMaritalStatus());

        assertEquals(information.getIndividualIdentification(), result.getIndividualIdentification());

    }

    public void testGetIndividualInformationWithEmptyResult() {
        // Setup
        final String msisdn = "msisdn";
        /*final IndividualInformation expectedResult = null;*/

        ResponseEntity response = ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();

        when(mockPartyManagementApiClient.getIndividualInformation(Mockito.anyString())).thenReturn(response);
        IndividualInformation information = new IndividualInformation();

        // Run the test
        final IndividualInformation result = partyManagementServiceUnderTest.getIndividualInformation(msisdn);

        // Verify the results
        assertEquals(information.getId(), result.getId());
        assertEquals(information.getFamilyName(), result.getFamilyName());
        assertEquals(information.getContactNumbers(), result.getContactNumbers());
        assertEquals(information.getBirthDate(), result.getBirthDate());
        assertEquals(information.getTitle(), result.getTitle());
        assertEquals(information.getGender(), result.getGender());
    }

}
