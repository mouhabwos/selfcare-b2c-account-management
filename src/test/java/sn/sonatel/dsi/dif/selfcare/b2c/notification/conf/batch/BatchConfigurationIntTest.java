package sn.sonatel.dsi.dif.selfcare.b2c.notification.conf.batch;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.config.SecurityBeanOverrideConfiguration;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;

import java.io.File;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.awaitility.Awaitility.await;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SelfcareB2CApp.class})
public class BatchConfigurationIntTest {

    @Autowired
    @Qualifier("asyncJobLauncher")
    private JobLauncher asyncJobLauncher;

    @Autowired
    private Job job;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private AccountB2CRepository accountB2CRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    private void createEntityAccount(){

        AccountB2C  accountB2C = new AccountB2C();
        accountB2C.setFirstName("FirstName");
        accountB2C.setLastName("LastName");
        accountB2C.setNumero("782363572");
        accountB2C.setEmail("782363572@orange.com");
        accountB2CRepository.save(accountB2C);

        AccountB2C  account = new AccountB2C();
        account.setFirstName("FirstName");
        account.setLastName("LastName");
        account.setHashMsisdn("hash_770000000");
        account.setNumero("770000000");
        account.setEmail("770000000@orange.com");
        accountB2CRepository.save(account);
    }

    @Test
    public void testBatchConfig() throws Exception {
         createEntityAccount();
        File resourcesDirectory = new File("src/test/resources/files/numero.csv");

        MockMultipartFile file = new MockMultipartFile("file", "numeros.csv", "", Files.readAllBytes(resourcesDirectory.toPath()));

        String nameFile = "Msisdn_File.csv";
        String sourceFile = applicationProperties.getTmpPath()+nameFile;
        File fileSourcePath =  new File(sourceFile);
        Files.copy(file.getInputStream(), fileSourcePath.toPath(),REPLACE_EXISTING);

        JobParameters params = new JobParametersBuilder()
            .addString("sourceFile", sourceFile)
            .addString(BatchConfiguration.JOB_EXPORT_INFO_USER_NAME, String.valueOf(System.currentTimeMillis()))
            .toJobParameters();
        JobExecution jobExecution = asyncJobLauncher.run(job, params);

        await().until( () -> jobExecution.getStatus() == BatchStatus.COMPLETED );

        Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

    }


}
