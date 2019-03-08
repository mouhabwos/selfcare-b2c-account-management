package sn.sonatel.dsi.dif.selfcare.b2c.repository;

import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the RattachementLigne entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RattachementLigneRepository extends JpaRepository<RattachementLigne, Long> {

    List<RattachementLigne> findAllByAccountB2C(AccountB2C user);

    Optional<RattachementLigne> findByNumero(String numero);
}
