package sn.sonatel.dsi.dif.selfcare.b2c.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.DeviceInfos;


/**
 * Spring Data  repository for the DeviceInfos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeviceInfosRepository extends JpaRepository<DeviceInfos, Long> {

}
