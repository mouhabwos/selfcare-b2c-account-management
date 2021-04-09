package sn.sonatel.dsi.dif.selfcare.b2c.service.export;


import org.springframework.web.multipart.MultipartFile;

public interface ExportService {

    void exportAllUsers(String mailAdmin);

    void uploadFileMsisdn(MultipartFile file, String login);
}
