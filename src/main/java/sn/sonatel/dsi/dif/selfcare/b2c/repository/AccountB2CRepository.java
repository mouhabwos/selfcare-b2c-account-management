package sn.sonatel.dsi.dif.selfcare.b2c.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;

import java.util.Optional;


/**
 * Spring Data  repository for the AccountB2C entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountB2CRepository extends JpaRepository<AccountB2C, Long> {


    Optional<AccountB2C> findOneByEmail(String email);


    Optional<AccountB2C> findOneByNumero(String numero);

    @Modifying
    @Transactional
    @Query(nativeQuery=true, value = "UPDATE account_b_2_c SET first_name=:firstname, last_name=:lastname WHERE numero=:msisdn ")
    int updateAccountForExploitant(@Param("msisdn")String msisdn, @Param("firstname") String firstname, @Param("lastname") String lastname);

}
