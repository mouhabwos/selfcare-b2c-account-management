package sn.sonatel.dsi.dif.selfcare.b2c.repository;

import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the AccountB2C entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountB2CRepository extends JpaRepository<AccountB2C, Long> {


    Optional<AccountB2C> findOneByEmail(String email);


    Optional<AccountB2C> findOneByNumero(String numero);

}
