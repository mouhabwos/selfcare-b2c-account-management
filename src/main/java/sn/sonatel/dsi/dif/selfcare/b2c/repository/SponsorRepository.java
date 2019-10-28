package sn.sonatel.dsi.dif.selfcare.b2c.repository;

import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the Sponsor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Long> {

    Optional<Sponsor> findOneByMsisdn(String msisdn);
}
