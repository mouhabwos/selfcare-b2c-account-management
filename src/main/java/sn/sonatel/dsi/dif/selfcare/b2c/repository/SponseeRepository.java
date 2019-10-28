package sn.sonatel.dsi.dif.selfcare.b2c.repository;

import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsee;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the Sponsee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SponseeRepository extends JpaRepository<Sponsee, Long> {

    Optional<Sponsee> findOneByMsisdn(String msisdn);

}
