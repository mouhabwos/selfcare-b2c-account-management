package sn.sonatel.dsi.dif.selfcare.b2c.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sn.sonatel.dsi.dif.selfcare.utils.selfcarelogging.interceptor.LogManager;
import sn.sonatel.dsi.dif.selfcare.utils.selfcarelogging.service.LoggingProperties;
import sn.sonatel.dsi.dif.selfcare.utils.selfcarelogging.service.impl.FileLogger;

@Configuration
public class LogManagerConfiguration {

    private final LoggingProperties loggingProperties;

    public LogManagerConfiguration(LoggingProperties loggingProperties) {
        super();
        this.loggingProperties = loggingProperties;
    }

    @Bean
    public LogManager logManager() { return new LogManager(new FileLogger(),loggingProperties);}
}
