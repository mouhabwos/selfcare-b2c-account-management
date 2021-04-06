package sn.sonatel.dsi.dif.selfcare.b2c.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.FileInformation;

import java.time.ZonedDateTime;

public interface FileInformationRepository extends JpaRepository<FileInformation, Long> {

    Page<FileInformation> findAllByCreatedByUser(String user, Pageable pageable);

    Page<FileInformation> findAllByCreatedDateBetweenAndCreatedByUserOrderByCreatedDate(ZonedDateTime startDate, ZonedDateTime endDate, String user, Pageable pageable);
}
