//package io.security.corespringsecurity.security.configs;
//
//import io.security.corespringsecurity.security.metadatasource.UrlSecurityMetadataSource;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.access.AccessDecisionManager;
//import org.springframework.security.access.AccessDecisionVoter;
//import org.springframework.security.access.vote.AffirmativeBased;
//import org.springframework.security.access.vote.RoleVoter;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
//import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
//import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
//@Slf4j
//public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration{
//
//    @Bean
//    public FilterSecurityInterceptor customFilterSecurityInterceptor() throws Exception {
//        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
//        filterSecurityInterceptor.setSecurityMetadataSource(urlSecurityMetadataSource());
//        filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased());
//        return filterSecurityInterceptor;
//    }
//
//    @Bean
//    public FilterInvocationSecurityMetadataSource urlSecurityMetadataSource() {
//        return new UrlSecurityMetadataSource();
//    }
//
//    @Bean
//    public AccessDecisionManager affirmativeBased() {
//        AffirmativeBased accessDecisionManager = new AffirmativeBased(getAccessDecisionVoters());
//        return accessDecisionManager;
//    }
//
//    private List<AccessDecisionVoter<?>> getAccessDecisionVoters() {
//        return Arrays.asList(new RoleVoter());
//    }
//}