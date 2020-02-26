package sn.sonatel.dsi.dif.selfcare.b2c.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.NotificationInformation;

import java.util.List;


/**
 * Spring Data  repository for the AccountB2C entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationInformationRepository extends JpaRepository<NotificationInformation, Long> {

    List<NotificationInformation> findOneByCodeFormule(String codeFormule);

}
