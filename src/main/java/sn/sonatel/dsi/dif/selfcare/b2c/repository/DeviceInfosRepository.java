package sn.sonatel.dsi.dif.selfcare.b2c.repository;

import sn.sonatel.dsi.dif.selfcare.b2c.domain.DeviceInfos;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DeviceInfos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeviceInfosRepository extends JpaRepository<DeviceInfos, Long> {

}
