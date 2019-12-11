package io.security.corespringsecurity.security.configs;

import io.security.corespringsecurity.repository.AccessIpRepository;
import io.security.corespringsecurity.repository.ResourcesRepository;
import io.security.corespringsecurity.security.enums.SecurtiyMethodType;
import io.security.corespringsecurity.security.factory.MethodResourcesMapFactoryBean;
import io.security.corespringsecurity.security.factory.UrlResourcesMapFactoryBean;
import io.security.corespringsecurity.security.filter.UrlSecurityMetadataSource;
import io.security.corespringsecurity.security.voter.IpAddressVoter;
import io.security.corespringsecurity.service.impl.RoleHierarchyServiceImpl;
import io.security.corespringsecurity.service.SecurityResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.access.vote.*;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Slf4j
class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Autowired
    private ResourcesRepository resourcesRepository;
    @Autowired
    private RoleHierarchyServiceImpl roleHierarchyService;
    @Autowired
    private AccessIpRepository accessIpRepository;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SecurityResourceService securityResourceService;

    protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
        return mapBasedMethodSecurityMetadataSource();
    }

    @Bean
    public MapBasedMethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource() {
        return new MapBasedMethodSecurityMetadataSource(methodResourcesMapFactoryBean().getObject());
    }

    @Bean
    public MethodResourcesMapFactoryBean methodResourcesMapFactoryBean(){
        MethodResourcesMapFactoryBean methodResourcesMapFactoryBean = new MethodResourcesMapFactoryBean();
        methodResourcesMapFactoryBean.setSecurityResourceService(securityResourceService);
        methodResourcesMapFactoryBean.setResourceType(SecurtiyMethodType.METHOD.getValue());
        return methodResourcesMapFactoryBean;
    }

    @Bean
    @Profile("pointcut")
    BeanPostProcessor protectPointcutPostProcessor() throws Exception {

        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();

        Class<?> clazz = Class.forName("org.springframework.security.config.method.ProtectPointcutPostProcessor");
        Constructor<?> declaredConstructor = clazz.getDeclaredConstructor(MapBasedMethodSecurityMetadataSource.class);
        declaredConstructor.setAccessible(true);
        Object instance = declaredConstructor.newInstance(mapBasedMethodSecurityMetadataSource());
        Method setPointcutMap = instance.getClass().getMethod("setPointcutMap", Map.class);
        setPointcutMap.setAccessible(true);
        setPointcutMap.invoke(instance, pointcutResourcesMapFactoryBean().getObject());

        return (BeanPostProcessor)instance;
    }

    /**
     *
     * 설정클래스에서 람다 형식으로 선언된 빈이 존재할 경우 오류가 발생하여 스프링 빈과 동일한 클래스를 생성하여 처리
     * 아직 AspectJ 라이브러리에서 Fix 하지 못한 것으로 판단되지만 다른 오류 원인이 존재하는지 계속 살펴보도록 함
     */
    /*@Bean
    @Profile("pointcut")
    public ProtectPointcutPostProcessor protectPointcutPostProcessor() {

        ProtectPointcutPostProcessor protectPointcutPostProcessor = new ProtectPointcutPostProcessor(mapBasedMethodSecurityMetadataSource());
        protectPointcutPostProcessor.setPointcutMap(pointcutResourcesMapFactoryBean().getObject());

        return protectPointcutPostProcessor;
    }*/

    @Bean
    @Profile("pointcut")
    public MethodResourcesMapFactoryBean pointcutResourcesMapFactoryBean(){

        MethodResourcesMapFactoryBean pointcutResourcesMapFactoryBean = new MethodResourcesMapFactoryBean();
        pointcutResourcesMapFactoryBean.setSecurityResourceService(securityResourceService);
        pointcutResourcesMapFactoryBean.setResourceType(SecurtiyMethodType.POINTCUT.getValue());
        return pointcutResourcesMapFactoryBean;
    }

    @Bean
    public FilterInvocationSecurityMetadataSource urlSecurityMetadataSource() {
        return new UrlSecurityMetadataSource(urlResourcesMapFactoryBean().getObject(),securityResourceService);
    }

    @Bean
    public UrlResourcesMapFactoryBean urlResourcesMapFactoryBean(){
        UrlResourcesMapFactoryBean urlResourcesMapFactoryBean = new UrlResourcesMapFactoryBean();
        urlResourcesMapFactoryBean.setSecurityResourceService(securityResourceService);
        return urlResourcesMapFactoryBean;
    }

    @Bean
    public AccessDecisionManager affirmativeBased() {
        AffirmativeBased accessDecisionManager = new AffirmativeBased(getAccessDecisionVoters());
        accessDecisionManager.setAllowIfAllAbstainDecisions(false); // 접근 승인 거부 보류시 접근 허용은 true 접근 거부는 false
        return accessDecisionManager;
    }

    private List<AccessDecisionVoter<?>> getAccessDecisionVoters() {

        AuthenticatedVoter authenticatedVoter = new AuthenticatedVoter();
        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        IpAddressVoter ipAddressVoter = new IpAddressVoter(securityResourceService);

        List<AccessDecisionVoter<? extends Object>> accessDecisionVoterList = Arrays.asList(ipAddressVoter, authenticatedVoter, webExpressionVoter, roleVoter());
        return accessDecisionVoterList;
    }

    @Bean
    public RoleHierarchyVoter roleVoter() {
        RoleHierarchyVoter roleHierarchyVoter = new RoleHierarchyVoter(roleHierarchy());
        roleHierarchyVoter.setRolePrefix("ROLE_");
        return roleHierarchyVoter;
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        return roleHierarchy;
    }

    @Bean
    public SecurityResourceService securityResourceService() {
        SecurityResourceService SecurityResourceService = new SecurityResourceService(resourcesRepository, roleHierarchy(), roleHierarchyService, accessIpRepository);
        return SecurityResourceService;
    }
}