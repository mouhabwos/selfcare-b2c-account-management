package sn.sonatel.dsi.dif.selfcare.b2c.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.FileInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.SearchFilterItem;

import java.time.ZonedDateTime;
import java.util.Date;

public interface FileInformationService {

    FileInformation save(String sourceFile, Date dateCreation);

    Page<FileInformation> getInformationFileUploaded(SearchFilterItem searchFilterItem, Pageable pageable, ZonedDateTime startDate, ZonedDateTime endDate, String user);
}
