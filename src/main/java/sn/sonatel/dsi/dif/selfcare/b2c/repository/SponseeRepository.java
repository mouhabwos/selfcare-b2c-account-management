package sn.sonatel.dsi.dif.selfcare.b2c.repository;

import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsee;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Sponsee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SponseeRepository extends JpaRepository<Sponsee, Long> {

    Optional<Sponsee> findOneByMsisdn(String msisdn);

    List<Sponsee> findAllByAccountB2C(AccountB2C user);

    @Query("SELECT s FROM Sponsee s WHERE s.effective=false AND s.enabled=true")
    List<Sponsee> findAllSponseeNoRegistered();

}
