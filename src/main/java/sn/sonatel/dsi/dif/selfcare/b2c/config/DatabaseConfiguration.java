package sn.sonatel.dsi.dif.selfcare.b2c.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableJpaRepositories("sn.sonatel.dsi.dif.selfcare.b2c.repository")
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
@EnableElasticsearchRepositories("sn.sonatel.dsi.dif.selfcare.b2c.repository.search")
public class DatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger ( DatabaseConfiguration.class );


}
