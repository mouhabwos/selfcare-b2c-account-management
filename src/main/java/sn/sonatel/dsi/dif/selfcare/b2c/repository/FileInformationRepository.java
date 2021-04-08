package sn.sonatel.dsi.dif.selfcare.b2c.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.FileInformation;

public interface FileInformationRepository extends JpaRepository<FileInformation, Long> {
}
