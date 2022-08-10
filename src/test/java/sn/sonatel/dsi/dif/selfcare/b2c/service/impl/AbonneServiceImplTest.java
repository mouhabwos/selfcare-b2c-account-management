package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.PartyManagementApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.IndividualInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.InfoClientWrapper;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.OrganizationIdentification;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.OrganizationInformation;

class AbonneServiceImplTest {

    private static final String RESPONSE_NUMERO_ORGANIZATION =
        "{\n" +
        "    \"clientType\": \"ORGANIZATION\",\n" +
        "    \"information\": {\n" +
        "        \"id\": null,\n" +
        "        \"contactNumbers\": [],\n" +
        "        \"givenName\": null,\n" +
        "        \"familyName\": null,\n" +
        "        \"birthDate\": null,\n" +
        "        \"title\": null,\n" +
        "        \"status\": null,\n" +
        "        \"gender\": null,\n" +
        "        \"maritalStatus\": null,\n" +
        "        \"type\": null,\n" +
        "        \"individualIdentification\": []\n" +
        "    },\n" +
        "    \"organization\": {\n" +
        "        \"id\": \"782363572\",\n" +
        "        \"href\": null,\n" +
        "        \"isLegalEntity\": null,\n" +
        "        \"type\": null,\n" +
        "        \"tradingName\": \"SONATEL MOBILES\",\n" +
        "        \"nameType\": null,\n" +
        "        \"status\": \"ACTIF\",\n" +
        "        \"organizationIdentification\": [\n" +
        "            {\n" +
        "                \"id\": null,\n" +
        "                \"type\": \"COMPANYREGISTRATIONNUMBER\",\n" +
        "                \"identificationId\": \"SNDKR1999B1877\",\n" +
        "                \"issuingAuthority\": null,\n" +
        "                \"href\": null,\n" +
        "                \"issuingDate\": \"2007-02-08T12:00:00\",\n" +
        "                \"expiryDate\": null\n" +
        "            }\n" +
        "        ]\n" +
        "    }\n" +
        "}";

    private static final String RESPONSE_NUMERO_INDIVIDUAL =
        "{\n" +
        "    \"clientType\": \"INDIVIDUAL\",\n" +
        "    \"information\": {\n" +
        "        \"id\": \"781040956\",\n" +
        "        \"contactNumbers\": [\"770000000\",\"770000001\" ],\n" +
        "        \"givenName\": \"TEST\",\n" +
        "        \"familyName\": \"TEST\",\n" +
        "        \"birthDate\": \"1000-00-00\",\n" +
        "        \"title\": \"MME\",\n" +
        "        \"status\": \"ACTIF\",\n" +
        "        \"gender\": \"FEMALE\",\n" +
        "        \"maritalStatus\": null,\n" +
        "        \"type\": null,\n" +
        "        \"individualIdentification\": [\n" +
        "            {\n" +
        "                \"id\": null,\n" +
        "                \"type\": \"IDENTITYCARD\",\n" +
        "                \"identificationId\": \"000000060000\",\n" +
        "                \"issuingAuthority\": null,\n" +
        "                \"href\": null,\n" +
        "                \"issuingDate\": null,\n" +
        "                \"expiryDate\": null\n" +
        "            }\n" +
        "        ]\n" +
        "    },\n" +
        "    \"organization\": {\n" +
        "        \"id\": null,\n" +
        "        \"href\": null,\n" +
        "        \"isLegalEntity\": null,\n" +
        "        \"type\": null,\n" +
        "        \"tradingName\": null,\n" +
        "        \"nameType\": null,\n" +
        "        \"status\": null,\n" +
        "        \"organizationIdentification\": []\n" +
        "    }\n" +
        "}";

    @Mock
    private PartyManagementApiClient mockPartyManagementApiClient;

    private AbonneServiceImpl abonneServiceImplUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        abonneServiceImplUnderTest = new AbonneServiceImpl(mockPartyManagementApiClient);
    }

    private IndividualInformation getIndividualInformation(String msisdn) {
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

    private OrganizationInformation getOrganization(String msisdn) {
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
    void testGetIsOrangeNumberWithNotFoundNumber() {
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
    void testIsOrangeNumberWithTrue() {
        // Setup
        final String msisdn = "700000000";

        ResponseEntity<OrganizationInformation> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        ResponseEntity<IndividualInformation> responseEntity = ResponseEntity.status(HttpStatus.OK).body(getIndividualInformation(msisdn));

        when(mockPartyManagementApiClient.getIndividualInformation(Mockito.anyString())).thenReturn(responseEntity);
        when(mockPartyManagementApiClient.getOrganizationInformation(Mockito.anyString())).thenReturn(response);

        // Run the test
        boolean orangeNumber = abonneServiceImplUnderTest.isOrangeNumber(msisdn);
        Assertions.assertTrue(orangeNumber);
    }

    @Test
    void testIsOrangeNumbersWithTrue() {
        // Setup
        final String msisdn = "700000000";

        OrganizationInformation organization = getOrganization(msisdn);
        ResponseEntity response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.OK).body(organization);

        when(mockPartyManagementApiClient.getIndividualInformation(Mockito.anyString())).thenReturn(response);
        when(mockPartyManagementApiClient.getOrganizationInformation(Mockito.anyString())).thenReturn(responseEntity);

        // Run the test
        boolean orangeNumber = abonneServiceImplUnderTest.isOrangeNumber(msisdn);

        assertTrue(orangeNumber);
    }

    @Test
    void testGetIndividualInfos() {
        String msisdn = "776713165";

        IndividualInformation individualInformation = new IndividualInformation();
        individualInformation.setBirthDate("1996-03-21");
        individualInformation.setGivenName("Pape Sogui");
        individualInformation.setFamilyName("KONATE");

        when(mockPartyManagementApiClient.getIndividualInformation(msisdn)).thenReturn(ResponseEntity.ok(individualInformation));

        IndividualInformation information = abonneServiceImplUnderTest.getIndividualInformations(msisdn);

        Assert.assertEquals("1996-03-21", information.getBirthDate());
        Assert.assertEquals(individualInformation.getGivenName(), information.getGivenName());
        Assert.assertEquals(individualInformation.getFamilyName(), information.getFamilyName());
    }

    @Test
    void testIsOrangeNumberResponseFalse() throws IOException {
        ResponseEntity<IndividualInformation> informationResponseEntity = ResponseEntity.ok(
            getInfoClientWrapperIndividual().getInformation()
        );
        when(mockPartyManagementApiClient.getIndividualInformation(anyString())).thenReturn(informationResponseEntity);

        boolean organizationNumber = abonneServiceImplUnderTest.isCoorporateNumber("770000000");
        Assert.assertFalse(organizationNumber);
    }

    @Test
    void testIsOrangeNumberResponseTrue() throws IOException {
        InfoClientWrapper infoClientWrapperIndividual = getInfoClientWrapperOrganization();

        when(mockPartyManagementApiClient.getIndividualInformation(anyString())).thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<OrganizationInformation> informationResponseEntity = ResponseEntity.ok(
            infoClientWrapperIndividual.getOrganization()
        );
        when(mockPartyManagementApiClient.getOrganizationInformation(anyString())).thenReturn(informationResponseEntity);

        boolean organizationNumber = abonneServiceImplUnderTest.isCoorporateNumber("782362572");
        Assert.assertTrue(organizationNumber);
    }

    @Test
    void testGetMyContactNumbersWithNotValidMsisdn() throws IOException {
        InfoClientWrapper infoClientWrapperIndividual = getInfoClientWrapperOrganization();
        when(mockPartyManagementApiClient.getIndividualInformation(anyString())).thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<OrganizationInformation> informationResponseEntity = ResponseEntity.ok(
            infoClientWrapperIndividual.getOrganization()
        );
        when(mockPartyManagementApiClient.getOrganizationInformation(anyString())).thenReturn(informationResponseEntity);

        Set<String> contactNumbers = abonneServiceImplUnderTest.getMyContactNumbers("test");
        Assert.assertTrue(contactNumbers.isEmpty());
    }

    @Test
    void testGetMyContactNumbers() throws IOException {
        InfoClientWrapper infoClientWrapperIndividual = getInfoClientWrapperIndividual();
        ResponseEntity<IndividualInformation> informationResponseEntity = ResponseEntity.ok(infoClientWrapperIndividual.getInformation());

        when(mockPartyManagementApiClient.getIndividualInformation(anyString())).thenReturn(informationResponseEntity);
        when(mockPartyManagementApiClient.getOrganizationInformation(anyString())).thenReturn(ResponseEntity.notFound().build());

        Set<String> contactNumbers = abonneServiceImplUnderTest.getMyContactNumbers("test");
        Assert.assertEquals(2, contactNumbers.size());
    }

    @Test
    void testGetNumberStatus() throws IOException {
        InfoClientWrapper infoClientWrapperIndividual = getInfoClientWrapperIndividual();

        when(mockPartyManagementApiClient.getOrganizationInformation(anyString())).thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<IndividualInformation> informationResponseEntity = ResponseEntity.ok(infoClientWrapperIndividual.getInformation());
        when(mockPartyManagementApiClient.getIndividualInformation(anyString())).thenReturn(informationResponseEntity);

        ResponseEntity<String> responseEntity = abonneServiceImplUnderTest.getNumberStatus("330000000");
        Assert.assertEquals(ResponseEntity.ok("ACTIF"), responseEntity);
    }

    @Test
    void testGetNumberStatusWithNotFoundException() {
        when(mockPartyManagementApiClient.getOrganizationInformation(anyString())).thenReturn(ResponseEntity.notFound().build());
        when(mockPartyManagementApiClient.getIndividualInformation(anyString())).thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<String> responseEntity = abonneServiceImplUnderTest.getNumberStatus("330000000");
        Assert.assertEquals(ResponseEntity.notFound().build(), responseEntity);
    }

    private InfoClientWrapper getInfoClientWrapperOrganization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InfoClientWrapper infoClientWrapper = mapper.readValue(RESPONSE_NUMERO_ORGANIZATION, InfoClientWrapper.class);
        return infoClientWrapper;
    }

    private InfoClientWrapper getInfoClientWrapperIndividual() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InfoClientWrapper infoClientWrapper = mapper.readValue(RESPONSE_NUMERO_INDIVIDUAL, InfoClientWrapper.class);

        return infoClientWrapper;
    }
}
