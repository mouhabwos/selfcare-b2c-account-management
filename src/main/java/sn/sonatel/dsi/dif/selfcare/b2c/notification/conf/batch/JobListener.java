package sn.sonatel.dsi.dif.selfcare.b2c.notification.conf.batch;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import javax.batch.runtime.BatchStatus;
import java.io.FileOutputStream;
import java.io.IOException;

import static javax.batch.runtime.BatchStatus.COMPLETED;
import static javax.batch.runtime.BatchStatus.STARTED;


@Slf4j
public class JobListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobListener.class);
    private final HSSFWorkbook workbook;
    private FileOutputStream fileOutputStream;

    JobListener(HSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        try {
            if (jobExecution.getStatus().getBatchStatus() == STARTED) {
                String recipientFile = "/var/tmp/Information_Account_"+System.currentTimeMillis()+".csv";
                this.fileOutputStream = new FileOutputStream(recipientFile);
                fileOutputStream.flush();
            }

        } catch (IOException e) {
            log.error("Error when creating file with message : {}", e.getMessage());
        }

    }

    @SneakyThrows
    @Override
    public void afterJob(JobExecution jobExecution) {
        BatchStatus batchStatus = jobExecution.getStatus().getBatchStatus();

        if (batchStatus == COMPLETED) {
            try {
                workbook.write(fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                workbook.close();
            } catch (IOException e) {
                log.error("Error when closing file with message : {}", e.getMessage());
            }
        }
    }
}
