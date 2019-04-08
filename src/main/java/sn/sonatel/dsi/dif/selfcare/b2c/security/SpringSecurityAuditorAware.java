package sn.sonatel.dsi.dif.selfcare.b2c.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;

import java.util.Optional;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of ( SecurityUtils.getCurrentUserLogin ().orElse ( Constants.SYSTEM_ACCOUNT ) );
    }
}
