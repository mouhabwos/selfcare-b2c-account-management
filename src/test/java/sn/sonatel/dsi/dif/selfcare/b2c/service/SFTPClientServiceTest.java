package sn.sonatel.dsi.dif.selfcare.b2c.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.config.SftpConfig;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SFTPClientServiceTest {

    @Mock
    private ApplicationProperties mockApplicationProperties;
    @Mock
    private SftpConfig mockSftpConfig;

    private SFTPClientService sftpClientServiceUnderTest;

    @Mock
    private  SftpConfig.UploadGateways uploadGateway;


    @Before
    public void setUp() {
        initMocks(this);
        sftpClientServiceUnderTest = new SFTPClientService(mockApplicationProperties, uploadGateway);
    }

    @Test
    public void testSendFileToServerFtp() throws Exception {

        String fileName = "nameZip.zip";
        // Setup
        when(mockApplicationProperties.getTmpPath()).thenReturn("");

        // Run the test
        sftpClientServiceUnderTest.sendFileToServerFtp(fileName);
        Mockito.verify(uploadGateway).upload(any());

    }

    @Test
    public void testZipFiles() throws Exception {
        // Setup
        final List<MultipartFile> multipartFiles = Arrays.asList();
        when(mockApplicationProperties.getTmpPath()).thenReturn("result");

        // Run the test
        final String result = sftpClientServiceUnderTest.zipFiles(multipartFiles, "nameZip");

        // Verify the results
        assertEquals("nameZip.zip", result);
    }
}
