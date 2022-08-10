package sn.sonatel.dsi.dif.selfcare.b2c.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.config.SftpConfig;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.OperationDTO;

class SFTPClientServiceTest {

    @Mock
    private ApplicationProperties mockApplicationProperties;

    @Mock
    private SftpConfig mockSftpConfig;

    private SFTPClientService sftpClientServiceUnderTest;

    @Mock
    private SftpConfig.UploadGateways uploadGateway;

    @BeforeEach
    void setUp() {
        initMocks(this);
        sftpClientServiceUnderTest = new SFTPClientService(mockApplicationProperties, uploadGateway);
    }

    private OperationDTO operationDTO() throws Exception {
        OperationDTO operationDTO = new OperationDTO();

        String filePdf = "fichierTest.pdf";
        String image = "imageTest.png";

        File filePdfM = new File(filePdf);
        File fileImageRectoM = new File(image);
        File fileImageVersoM = new File(image);

        FileInputStream inputPDF = new FileInputStream(filePdfM);
        FileInputStream inputRecto = new FileInputStream(filePdfM);
        FileInputStream inputVerso = new FileInputStream(filePdfM);

        MultipartFile multipartFilePDF = new MockMultipartFile("file", filePdfM.getName(), "text/plain", IOUtils.toByteArray(inputPDF));
        MultipartFile multipartFileRecto = new MockMultipartFile(
            "file",
            fileImageRectoM.getName(),
            "text/plain",
            IOUtils.toByteArray(inputRecto)
        );
        MultipartFile multipartFileVerso = new MockMultipartFile(
            "file",
            fileImageVersoM.getName(),
            "text/plain",
            IOUtils.toByteArray(inputVerso)
        );

        operationDTO.setFirstName("firstName");
        operationDTO.setOperationCode("operation-200");
        operationDTO.setLastName("lastname");
        operationDTO.setNumero("777777700");
        operationDTO.setEmail("email@gmail.com");
        operationDTO.setFormulaire(multipartFilePDF);
        operationDTO.setRectoID(multipartFileRecto);
        operationDTO.setVerso(multipartFileVerso);

        return operationDTO;
    }

    @Test
    void testSendFileToServerFtp() throws Exception {
        String fileName = "nameZip.zip";
        // Setup
        when(mockApplicationProperties.getTmpPath()).thenReturn("");

        // Run the test
        sftpClientServiceUnderTest.sendFileToServerFtp(fileName);
        Mockito.verify(uploadGateway).upload(any());
    }

    @Test
    void testZipFiles() throws Exception {
        // Setup
        final List<MultipartFile> multipartFiles = Arrays.asList();
        when(mockApplicationProperties.getTmpPath()).thenReturn("result");

        // Run the test
        final String result = sftpClientServiceUnderTest.zipFiles(multipartFiles, "nameZip");

        // Verify the results
        assertEquals("nameZip.zip", result);
    }

    @Test
    void testZipFiless() throws Exception {
        // Setup
        OperationDTO operationDTO = operationDTO();
        List<MultipartFile> multipartFiles = new ArrayList<>();
        multipartFiles.add(operationDTO.getVerso());
        multipartFiles.add(operationDTO.getFormulaire());
        multipartFiles.add(operationDTO.getRectoID());
        when(mockApplicationProperties.getTmpPath()).thenReturn("result");

        // Run the test
        final String result = sftpClientServiceUnderTest.zipFiles(multipartFiles, "nameZip");

        // Verify the results
        assertEquals("nameZip.zip", result);
    }
}
