package com.neemshade.sniper.config;

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
            cm.createCache(com.neemshade.sniper.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.UserInfo.class.getName(), jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.UserInfo.class.getName() + ".snFiles", jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.UserInfo.class.getName() + ".ownerTasks", jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.UserInfo.class.getName() + ".transcriptTasks", jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.UserInfo.class.getName() + ".editorTasks", jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.UserInfo.class.getName() + ".managerTasks", jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.UserInfo.class.getName() + ".taskHistories", jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.Hospital.class.getName(), jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.Hospital.class.getName() + ".tasks", jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.Hospital.class.getName() + ".doctors", jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.Doctor.class.getName(), jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.Doctor.class.getName() + ".tasks", jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.Doctor.class.getName() + ".hospitals", jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.Company.class.getName(), jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.Company.class.getName() + ".userInfos", jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.Company.class.getName() + ".tasks", jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.Patient.class.getName(), jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.Patient.class.getName() + ".snFiles", jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.SnFile.class.getName(), jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.SnFile.class.getName() + ".patients", jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.SnFile.class.getName() + ".tasks", jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.TaskGroup.class.getName(), jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.TaskGroup.class.getName() + ".tasks", jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.Task.class.getName(), jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.Task.class.getName() + ".taskHistories", jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.Task.class.getName() + ".snFiles", jcacheConfiguration);
            cm.createCache(com.neemshade.sniper.domain.TaskHistory.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
