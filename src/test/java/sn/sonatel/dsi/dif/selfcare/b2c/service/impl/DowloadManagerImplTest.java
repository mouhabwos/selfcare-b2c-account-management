package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import io.undertow.server.handlers.resource.URLResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.UserInfoOuvertureCompte;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServiceFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class DowloadManagerImplTest {

    @Mock
    private ServiceFile mockServiceFile;

    @InjectMocks
    private DowloadManagerImpl dowloadManagerImplUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        dowloadManagerImplUnderTest = new DowloadManagerImpl(mockServiceFile);
    }

    @Test
    public void testAddResources() {



        UserInfoOuvertureCompte user = new UserInfoOuvertureCompte();

        user.setNumero("771326617");
        user.setFirstName("bouya");
        user.setLastName("kande");
        user.setOperation("Ouverture compte OM");
        user.setOperationTitle("Ouverture compte OM");
        user.setFormulaire("1.PNG");
        user.setRectoID("1.PNG");
        user.setVersoID("1.PNG");
        user.setEmail("bouya@gmail.com");

        File path = new File("pom.xml");
        System.out.println(path.getAbsolutePath());
        Resource resource = new FileSystemResource(path);
        user.setObjectRectoID(resource);
        user.setObjectFormulaire(resource);

        ResponseEntity<Resource> response = new ResponseEntity<>(resource, HttpStatus.OK);
        when(mockServiceFile.downloadFile(anyString())).thenReturn(response);

        dowloadManagerImplUnderTest.addResources(user);


    }
}
