package com.kemal.spring.configuration;


import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
                "cache.allUsersPageable",
                "cache.allUsers",
                "cache.allUsersEagerly",
                "cache.userByEmail",
                "cache.userById",
                "cache.allRoles",
                "cache.roleByName",
                "cache.roleById",
                "cache.byNameContaining",
                "cache.bySurnameContaining",
                "cache.byUsernameContaining",
                "cache.byEmailContaining");
    }
}
