package sn.sonatel.dsi.dif.selfcare.b2c.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Mail;

import java.util.Optional;


/**
 * Spring Data  repository for the AccountB2C entity.
 */

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 */

@SuppressWarnings("unused")
@Repository
public interface MailSendRepository extends JpaRepository<Mail, Long> {

 Optional<Mail> findByIdRequest(String idRequest);

}
