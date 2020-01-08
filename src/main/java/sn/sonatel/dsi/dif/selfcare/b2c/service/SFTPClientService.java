package sn.sonatel.dsi.dif.selfcare.b2c.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.config.SftpConfig;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
public class SFTPClientService {

    private final Logger log = LoggerFactory
        .getLogger ( SFTPClientService.class );

    private final ApplicationProperties applicationProperties;

    private final SftpConfig.UploadGateways uploadGateway;


    public SFTPClientService(ApplicationProperties applicationProperties, SftpConfig.UploadGateways uploadGateway) {
        this.applicationProperties = applicationProperties;
        this.uploadGateway = uploadGateway;

    }


    @Async
    public void sendFileToServerFtp(String fileName) {
        log.debug("@@@@@@@@@@  Service upload file to server SFTP: {}    @@@@@@@@@@@@", fileName);
        File file =  new File(applicationProperties.getTmpPath()+fileName);

          uploadGateway.upload(file);
    }


    public String zipFiles(List<MultipartFile> multipartFiles, String nameZip) {

        String fileName = nameZip+".zip";
        String directory = applicationProperties.getTmpPath()+fileName;


        try (FileOutputStream fos = new FileOutputStream(directory);
             ZipOutputStream zipOut = new ZipOutputStream(fos)){

        for (MultipartFile multipartFile : multipartFiles) {

            if(multipartFile!=null) {

                File fileToZip = new File(multipartFile.getOriginalFilename());

                InputStream inputStream = multipartFile.getInputStream();

                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());

                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while((length = inputStream.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }

            }
        }
            return fileName;
    } catch (IOException ex) {
        log.error("Error while upload file from SFTP ", ex.getMessage(),ex);
    } finally {
            log.error("Finally while upload file from SFTP ");
            return fileName;
    }
    }

}
