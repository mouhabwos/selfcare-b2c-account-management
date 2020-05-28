package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.junit.Assert;
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
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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

    private OrganizationInformation getOrganization(String msisdn){

        OrganizationInformation identification = new OrganizationInformation();
        identification.setId(msisdn);
        identification.setIsLegalEntity("");
        identification.setTradingName("DD");
        identification.setStatus("status");
        identification.setHref("");
        identification.setOrganizationIdentification(new HashSet<>());
        identification.setType("type");

        return identification;
    }


    @Test
    public void testGetIsOrangeNumberWithNotFoundNumber() {
        // Setup
        final String msisdn = "700000000";

        ResponseEntity response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        when(mockPartyManagementApiClient.getIndividualInformation(Mockito.anyString())).thenReturn(response);
        when(mockPartyManagementApiClient.getOrganizationInformation(Mockito.anyString())).thenReturn(response);

        // Run the test
        boolean orangeNumber = abonneServiceImplUnderTest.isOrangeNumber(msisdn);
        assertFalse(orangeNumber);

    }

    @Test
    public void testIsOrangeNumberWithTrue() {
        // Setup
        final String msisdn = "700000000";


        ResponseEntity response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.OK).body(getIndividualInformation(msisdn));

        when(mockPartyManagementApiClient.getIndividualInformation(Mockito.anyString())).thenReturn(responseEntity);
        when(mockPartyManagementApiClient.getOrganizationInformation(Mockito.anyString())).thenReturn(response);

        // Run the test
        boolean orangeNumber = abonneServiceImplUnderTest.isOrangeNumber(msisdn);
      //  assertTrue(orangeNumber);

    }



    @Test
    public void testIsOrangeNumbersWithTrue() {
        // Setup
        final String msisdn = "700000000";


        OrganizationInformation organization =  getOrganization(msisdn);
        ResponseEntity response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.OK).body(organization);

        when(mockPartyManagementApiClient.getIndividualInformation(Mockito.anyString())).thenReturn(response);
        when(mockPartyManagementApiClient.getOrganizationInformation(Mockito.anyString())).thenReturn(responseEntity);

        // Run the test
        boolean orangeNumber = abonneServiceImplUnderTest.isOrangeNumber(msisdn);

        assertTrue(orangeNumber);
    }

    @Test
    public void testGetBirthDate(){

        String msisdn = "776713165";

        IndividualInformation individualInformation = new IndividualInformation();
        individualInformation.setBirthDate("1996-03-21");

        when(mockPartyManagementApiClient.getIndividualInformation(msisdn)).thenReturn(ResponseEntity.ok(individualInformation));

        ResponseEntity<String> birthDate = abonneServiceImplUnderTest.getBirthDate(msisdn);

        Assert.assertEquals("1996-03-21", birthDate.getBody());

    }

    @Test(expected = BadRequestAlertException.class)
    public void testGetBirthDateReturn400(){

        String msisdn = "776713165";

        IndividualInformation individualInformation = new IndividualInformation();
        individualInformation.setBirthDate(null);

        when(mockPartyManagementApiClient.getIndividualInformation(msisdn)).thenReturn(ResponseEntity.ok(individualInformation));

        ResponseEntity<String> birthDate = abonneServiceImplUnderTest.getBirthDate(msisdn);

    }

    @Test(expected = BadRequestAlertException.class)
    public void GetBirthDateReturn400(){

        String msisdn = "776713165";

        when(mockPartyManagementApiClient.getIndividualInformation(msisdn)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        ResponseEntity<String> birthDate = abonneServiceImplUnderTest.getBirthDate(msisdn);

    }
}
