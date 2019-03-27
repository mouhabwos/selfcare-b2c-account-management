package sn.sonatel.dsi.dif.selfcare.b2c.aop.logging.audit;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * @author mbbsow
 */
@Component
public class FileLogger implements ILogger {

    public static final Logger LOGGER = Logger.getLogger ( FileLogger.class.getName () );

    @Async
    @Override
    public void log(SelfcareLog log) {
        LOGGER.info ( log.toString () );
    }

}
