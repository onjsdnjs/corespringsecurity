package io.security.corespringsecurity.config;

import io.security.corespringsecurity.repository.AccessIpRepository;
import io.security.corespringsecurity.repository.ResourcesRepository;
import io.security.corespringsecurity.security.configs.SecurityConfig;
import io.security.corespringsecurity.service.SecurityResourceService;
import io.security.corespringsecurity.service.impl.RoleHierarchyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Configuration
@AutoConfigureAfter(SecurityConfig.class)
public class AppConfig {

    @Autowired
    private ResourcesRepository resourcesRepository;
    @Autowired
    private RoleHierarchyServiceImpl roleHierarchyService;
    @Autowired
    private AccessIpRepository accessIpRepository;
    @Autowired
    private RoleHierarchyImpl roleHierarchy;

    @Bean
    public SecurityResourceService securityResourceService() {
        SecurityResourceService SecurityResourceService = new SecurityResourceService(resourcesRepository, roleHierarchy, roleHierarchyService, accessIpRepository);
        return SecurityResourceService;
    }

}
