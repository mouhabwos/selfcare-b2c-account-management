package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.IntegrationTest;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.FileInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.FileInformationRepository;

import javax.batch.runtime.BatchStatus;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@IntegrationTest
public class FileInformationServiceImplTest {

    @Autowired
    private FileInformationRepository fileInformationRepository;

    private FileInformationServiceImpl fileInformationServiceImpl;

    @Mock
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Before
    public void setUp() {
        initMocks(this);
        fileInformationServiceImpl = new FileInformationServiceImpl(fileInformationRepository, restTemplate, applicationProperties);
    }

    @Test
    public void testSaveFileInformation(){
        String sourceFile = "src/test/resources/files/numero.csv";
        Date date = new Date();

        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(),
            ZoneId.systemDefault());

        String fileNameResponse = "file_test.csv";

        Mockito.when(restTemplate.exchange(Mockito.anyString(),Mockito.any(HttpMethod.class),Mockito.any(HttpEntity.class),Mockito.any(Class.class))).thenReturn(ResponseEntity.ok(fileNameResponse));

        FileInformation response = fileInformationServiceImpl.save(sourceFile, date, "test@orange-sonatel.com");

        Assert.assertEquals(BatchStatus.COMPLETED.name(), response.getStatus());
        Assert.assertEquals(fileNameResponse, response.getFileName());
        Assert.assertEquals(zonedDateTime, response.getCreatedDate());

    }


    @Test
    public void testSaveFileInformationWithEmptyBody(){
        String sourceFile = "src/test/resources/files/numero.csv";
        Date date = new Date();

        Mockito.when(restTemplate.exchange(Mockito.anyString(),Mockito.any(HttpMethod.class),Mockito.any(HttpEntity.class),Mockito.any(Class.class))).thenReturn(ResponseEntity.ok().build());
        FileInformation response = fileInformationServiceImpl.save(sourceFile, date, "test@orange-sonatel.com");
        Assert.assertEquals(BatchStatus.FAILED.name(), response.getStatus());
    }

    @Test
    public void testSaveFileInformationWithErrorUploadFile(){
        String sourceFile = "src/test/resources/files/numero.csv";
        Date date = new Date();

        Mockito.when(restTemplate.exchange(Mockito.anyString(),Mockito.any(),Mockito.any(),Mockito.any(Class.class))).thenReturn(ResponseEntity.badRequest().build());

        FileInformation response = fileInformationServiceImpl.save(sourceFile, date, "test@orange-sonatel.com");
        Assert.assertEquals(BatchStatus.FAILED.name(), response.getStatus());
    }

    @Test
    public void testSaveFileInformationWithErrorFilename(){
        String sourceFile = "src/test/resources/files/numero.csvd";
        Date date = new Date();
        fileInformationServiceImpl.save(sourceFile, date, "test@orange-sonatel.com");
    }

}
