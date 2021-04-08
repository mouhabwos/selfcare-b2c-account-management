package sn.sonatel.dsi.dif.selfcare.b2c.notification.conf.batch;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;

import java.util.List;

import static java.util.Arrays.asList;

public class InformationAccountWriter implements ItemWriter<AccountB2C> {

    private static final Logger log = LoggerFactory.getLogger(InformationAccountWriter.class);

    private final Sheet sheet;

    InformationAccountWriter(Sheet sheet) {
        this.sheet = sheet;
    }

    @Override
    public void write(List<? extends AccountB2C> list) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            writeRow(i, list.get(i));
        }
    }

    private void writeRow(int currentRowNumber, AccountB2C accountB2C) {
        log.debug("Service writer AccountB2C into file with index, and value: {}, {}",currentRowNumber, accountB2C.getNumero());

        if(accountB2C.getHashMsisdn() != null){
            List<String> columns = prepareColumns(accountB2C);
            if(currentRowNumber==0){
                createHeader(this.sheet.createRow(currentRowNumber));
            }

            currentRowNumber++;
            Row row = this.sheet.createRow(currentRowNumber);
            for (int i = 0; i < columns.size(); i++) {
                writeCell(row, i, columns.get(i));
            }
        }

    }

    private List<String> prepareColumns(AccountB2C accountB2C) {
        return asList(
            accountB2C.getHashMsisdn(),
            accountB2C.getFirstName(),
            accountB2C.getLastName()
        );
    }

    private void writeCell(Row row, int currentColumnNumber, String value) {
        Cell cell = row.createCell(currentColumnNumber);
        if(value != null){
            cell.setCellValue(value);
        }else {
            cell.setCellValue("");
        }
    }

    private void createHeader(Row row){
        row.createCell(0).setCellValue("HashMsisdn");
        row.createCell(1).setCellValue("FirstName");
        row.createCell(2).setCellValue("LastName");
    }
}
