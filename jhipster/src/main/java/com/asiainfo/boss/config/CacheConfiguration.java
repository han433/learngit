package com.asiainfo.boss.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache("users", jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.Hot.class.getName(), jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.HotUser.class.getName(), jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.HotTalent.class.getName(), jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.SummaryReport.class.getName(), jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.ActivityReport.class.getName(), jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.Report.class.getName(), jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.ActivityDetailsReport.class.getName(), jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.Application_commission.class.getName(), jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.Invoice.class.getName(), jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.UserAlert.class.getName(), jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.Division.class.getName(), jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.Sequence.class.getName(), jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.InvoiceActivity.class.getName(), jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.CreditTransaction.class.getName(), jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.JobLocation.class.getName(), jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.TalentLocation.class.getName(), jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.UserFavoriteJob.class.getName(), jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.UserFavoriteTalent.class.getName(), jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.HotListUser.class.getName(), jcacheConfiguration);
            cm.createCache(com.asiainfo.boss.domain.TeamUser.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
