package sn.sonatel.dsi.dif.selfcare.b2c.service.export;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.notification.conf.batch.BatchConfiguration;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.MailService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.SFTPClientService;

import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
public class ExportServiceImpl implements ExportService {

    private static final Logger log = LoggerFactory.getLogger(ExportServiceImpl.class);

    private static final String NAME_FILE_CSV_USERS = "UsersOrangeEtMoi.xlsx";

    private static final String NAME_FILE_ZIP_USERS = "UsersOrangeEtMoi.zip";

    private static final String KEY_NAME_FILE_SOURCE = "sourceFile";

    private static final String EXTENSION = ".csv";

    private final AccountB2CRepository accountB2CRepository;

    private final ApplicationProperties applicationProperties;

    private final SFTPClientService sftpClientService;

    private final MailService mailService;

    private final JobLauncher asyncJobLauncher;

    private final Job job;


    public ExportServiceImpl(AccountB2CRepository accountB2CRepository, ApplicationProperties applicationProperties, SFTPClientService sftpClientService, MailService mailService, JobLauncher asyncJobLauncher, Job job) {
        this.accountB2CRepository = accountB2CRepository;
        this.applicationProperties = applicationProperties;
        this.sftpClientService = sftpClientService;
        this.mailService = mailService;
        this.asyncJobLauncher = asyncJobLauncher;
        this.job = job;
    }

    @Async
    @Override
    public void exportAllUsers(String mailAdmin){
        log.debug ( "Service request to export All User in to an excel file with email : {}", mailAdmin);

        List<AccountB2C> accountB2CList = accountB2CRepository.findAll();
        if(!accountB2CList.isEmpty()){

            String directory = applicationProperties.getTmpPath();
            String sourceFile = directory+NAME_FILE_CSV_USERS;
            String sourceFileZip = directory+NAME_FILE_ZIP_USERS;

            // This data needs to be written (Object[])
            Map<String, Object[]> data = getMapData( accountB2CList);

            creationsAndExportFileCSV(accountB2CList, data, mailAdmin, sourceFile, sourceFileZip);
        }

    }

    @Override
    public void uploadFileMsisdn(MultipartFile file) {
        log.debug ( "Service request to upload file of list of Msisdn ");
        try {
            //Send File uploaded into /tmp
            String time = System.currentTimeMillis()+EXTENSION;
            String nameFile = "Msisdn_File"+time;
            String sourceFile = applicationProperties.getTmpPath() +nameFile;
            File fileSourcePath =  new File(sourceFile);
            Files.copy(file.getInputStream(), fileSourcePath.toPath(),REPLACE_EXISTING);

            //Add file name into jobParameters Obbject
            JobParameters params = new JobParametersBuilder()
                .addString(KEY_NAME_FILE_SOURCE, sourceFile)
                .addString(BatchConfiguration.JOB_EXPORT_INFO_USER_NAME, String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

            // Job excecution
            asyncJobLauncher.run(job, params);

        }catch (Exception ignored){
            log.error("Error when saving list of Msisdn for boosterId : {}, with error : {}",ignored, ignored.getMessage());
        }
    }


    private void creationsAndExportFileCSV(List<AccountB2C> accountB2CList, Map<String, Object[]> data, String mail, String sourceFile, String sourceFileZip) {

        log.debug ( "Service request to export the User List with size : {} ", accountB2CList.size() );

            // Blank workbook

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            // Create a blank sheet
            XSSFSheet sheet = workbook.createSheet("Clients OrangeEtMoi");

            // Iterate over data and write to sheet
            Set<String> keyset = data.keySet();
            int rownum = 0;
            for (String key : keyset) {
                // this creates a new row in the sheet
                Row row = sheet.createRow(rownum++);
                Object[] objArr = data.get(key);
                int cellnum = 0;
                for (Object obj : objArr) {
                    // this line creates a cell in the next column of that row
                    Cell cell = row.createCell(cellnum++);
                    if (obj instanceof String)
                        cell.setCellValue((String) obj);
                    else if (obj instanceof Integer)
                        cell.setCellValue((Integer) obj);
                }
            }

            // this Writes the workbook gfgcontribute
            FileOutputStream out = new FileOutputStream(new File(sourceFile));
            workbook.write(out);
            out.close();
            log.debug("The export of users is done with success");
            sourceFileZip = sftpClientService.compressFile(sourceFile, sourceFileZip);
            if (!sourceFileZip.isEmpty()) {
                mailService.sendEmailToAdmin(mail, sourceFileZip);
            }

        } catch (Exception e) {
            log.debug("Error during export : {}, {}", e.getCause(), e.getMessage());
        }

    }

    private Map<String, Object[]> getMapData(List<AccountB2C> accountB2CList){
        // This data needs to be written (Object[])
        Map<String, Object[]> data = new TreeMap<>();

        data.put("1", new Object[]{ "ID", "MSISDN", "PRENOM", "NOM", "EMAIL", "DATE D INSCRIPTION", "HASH", "CLE FOLLOW" });
        int index = 1;
        for (AccountB2C account : accountB2CList) {
            index++;
            SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
            String myDate = formatter.format(Date.from(account.getCreatedDate()));
            data.put(""+index+"", new Object[]{account.getId().toString(), account.getNumero(), account.getFirstName(), account.getLastName(), account.getEmail(), myDate, account.getHashMsisdn(), account.getClientId() });
        }
        return data;
    }

}
