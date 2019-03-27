package sn.sonatel.dsi.dif.selfcare.b2c.aop.logging.annotation;

import java.lang.annotation.*;

/**
 * @author mbbsow
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Auditable {

    String description();

}
