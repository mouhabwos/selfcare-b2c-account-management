package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.PartyManagementApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.IndividualInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.OrganizationIdentification;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.OrganizationInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.NotFoundNumberException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AbonneServiceImplTest {

    @Mock
    private PartyManagementApiClient mockPartyManagementApiClient;

    private AbonneServiceImpl abonneServiceImplUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        abonneServiceImplUnderTest = new AbonneServiceImpl(mockPartyManagementApiClient);
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

    private OrganizationInformation getOrganizationInformation(){
        OrganizationInformation information = new OrganizationInformation();
        information.setHref("");
        information.setId("771326617");
        information.setIndividualIdentification(new HashSet<>());
        information.setNameType("test");
        information.setStatus("actif");
        information.setTradingName("");
        information.setIsLegalEntity("djhf");

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
        abonneServiceImplUnderTest.getIndividualInformation(msisdn);

    }

    @Test
    public void testGetIndividualInformationSuccess() {
        // init val
        final String msisdn = "77000 00 00";
        IndividualInformation information = getIndividualInformation(msisdn);


        ResponseEntity response = ResponseEntity.status(HttpStatus.OK).body(information);

        when(mockPartyManagementApiClient.getIndividualInformation(Mockito.anyString())).thenReturn(response);


        // Run the test
        final IndividualInformation result = abonneServiceImplUnderTest.getIndividualInformation(msisdn);

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

    @Test
    public void testGetIndividualInformationWithEmptyResult() {
        // Setup
        final String msisdn = "msisdn";
        /*final IndividualInformation expectedResult = null;*/

        ResponseEntity response = ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();

        when(mockPartyManagementApiClient.getOrganizationInformation(Mockito.anyString())).thenReturn(response);
        OrganizationInformation information = new OrganizationInformation();

        // Run the test
        final OrganizationInformation result = abonneServiceImplUnderTest.getOrganizationInformation(msisdn);

        // Verify the results
        assertEquals(information.getId(), result.getId());
        assertEquals(information.getHref(), result.getHref());
        assertEquals(information.getIndividualIdentification(), result.getIndividualIdentification());
        assertEquals(information.getIsLegalEntity(), result.getIsLegalEntity());
        assertEquals(information.getNameType(), result.getNameType());
        assertEquals(information.getStatus(), result.getStatus());
        assertEquals(information.getTradingName(), result.getTradingName());
        assertEquals(information.getType(), result.getType());
    }

    @Test
    public void testGetIndividualInformationWithSuccess() {
        // Setup
        final String msisdn = "771326617";
        /*final IndividualInformation expectedResult = null;*/

        OrganizationInformation information = getOrganizationInformation();
        ResponseEntity response = ResponseEntity.status(HttpStatus.OK).body(information);

        when(mockPartyManagementApiClient.getOrganizationInformation(Mockito.anyString())).thenReturn(response);


        // Run the test
        final OrganizationInformation result = abonneServiceImplUnderTest.getOrganizationInformation(msisdn);

        // Verify the results
        assertEquals(information.getId(), result.getId());
        assertEquals(information.getHref(), result.getHref());
        assertEquals(information.getIndividualIdentification(), result.getIndividualIdentification());
        assertEquals(information.getIsLegalEntity(), result.getIsLegalEntity());
        assertEquals(information.getNameType(), result.getNameType());
        assertEquals(information.getStatus(), result.getStatus());
        assertEquals(information.getTradingName(), result.getTradingName());
        assertEquals(information.getType(), result.getType());
    }
}
