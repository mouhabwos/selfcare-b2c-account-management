package sn.sonatel.dsi.dif.selfcare.b2c;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import sn.sonatel.dsi.dif.selfcare.b2c.config.TestSecurityConfiguration;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { SelfcareB2CApp.class, TestSecurityConfiguration.class })
public @interface IntegrationTest {
}
