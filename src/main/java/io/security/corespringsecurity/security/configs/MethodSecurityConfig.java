package io.security.corespringsecurity.security.configs;

import io.security.corespringsecurity.repository.ResourcesRepository;
import io.security.corespringsecurity.security.metadatasource.UrlSecurityMetadataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Slf4j
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration{

    @Autowired
    private ResourcesRepository resourcesRepository;

    @Bean
    public FilterInvocationSecurityMetadataSource urlSecurityMetadataSource() {
        return new UrlSecurityMetadataSource();
    }
}