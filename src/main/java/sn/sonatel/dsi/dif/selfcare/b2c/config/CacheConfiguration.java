package sn.sonatel.dsi.dif.selfcare.b2c.config;

import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Mail;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.NotificationInformation;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader ( this.getClass ().getClassLoader () );
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache ().getEhcache ();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration (
            CacheConfigurationBuilder.newCacheConfigurationBuilder ( Object.class, Object.class,
                ResourcePoolsBuilder.heap ( ehcache.getMaxEntries () ) )
                .withExpiry ( ExpiryPolicyBuilder.timeToLiveExpiration ( Duration.ofSeconds ( ehcache.getTimeToLiveSeconds () ) ) )
                .build () );
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache ( sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C.class.getName (), jcacheConfiguration );
            cm.createCache ( sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C.class.getName () + ".users", jcacheConfiguration );
            cm.createCache ( sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne.class.getName (), jcacheConfiguration );
            cm.createCache ( NotificationInformation.class.getName (), jcacheConfiguration );
            cm.createCache ( sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C.class.getName ()+".notificationInformations", jcacheConfiguration );
            cm.createCache ( Mail.class.getName (), jcacheConfiguration );
            // jhipster-needle-ehcache-add-entry
        };
    }
}
