package io.security.corespringsecurity.service;

import io.security.corespringsecurity.domain.entity.Resources;
import io.security.corespringsecurity.repository.AccessIpRepository;
import io.security.corespringsecurity.repository.ResourcesRepository;
import io.security.corespringsecurity.security.aop.CustomMethodSecurityInterceptor;
import io.security.corespringsecurity.service.impl.RoleHierarchyServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SecurityResourceService {

    private ResourcesRepository resourcesRepository;
    private RoleHierarchyServiceImpl roleHierarchyService;
    //private RoleHierarchyImpl roleHierarchy;
    private AccessIpRepository accessIpRepository;

    public SecurityResourceService(ResourcesRepository resourcesRepository, /*RoleHierarchyImpl roleHierarchy, */RoleHierarchyServiceImpl roleHierarchyService, AccessIpRepository accessIpRepository/*, MapBasedMethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource, AnnotationConfigServletWebServerApplicationContext applicationContext, CustomMethodSecurityInterceptor methodSecurityInterceptor*/) {
        this.resourcesRepository = resourcesRepository;
        //this.roleHierarchy = roleHierarchy;
        this.roleHierarchyService = roleHierarchyService;
        this.accessIpRepository = accessIpRepository;
    }

    @Cacheable(value = "resourceList")
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceList() {

        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<Resources> resourcesList = resourcesRepository.findAllResources();

        resourcesList.forEach(re ->
                {
                    List<ConfigAttribute> configAttributeList = new ArrayList<>();
                    re.getRoleSet().forEach(ro -> {
                        configAttributeList.add(new SecurityConfig(ro.getRoleName()));
                        result.put(new AntPathRequestMatcher(re.getResourceName()), configAttributeList);
                    });
                }
        );
        log.debug("cache test");
        return result;
    }

    @Cacheable(value = "methodResourceList")
    public LinkedHashMap<String, List<ConfigAttribute>> getMethodResourceList() {

        LinkedHashMap<String, List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<Resources> resourcesList = resourcesRepository.findAllMethodResources();

        getResourceMap(result, resourcesList);
        return result;
    }

    @Cacheable(value = "pointcutResourceList")
    public LinkedHashMap<String, List<ConfigAttribute>> getPointcutResourceList() {

        LinkedHashMap<String, List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<Resources> resourcesList = resourcesRepository.findAllPointcutResources();

        getResourceMap(result, resourcesList);
        return result;
    }

    @Cacheable(value = "accessIpList")
    public List<String> getAccessIpList() {

        List<String> accessIpList = accessIpRepository.findAll().stream().map(accessIp -> accessIp.getIpAddress()).collect(Collectors.toList());

        return accessIpList;
    }

    private void getResourceMap(LinkedHashMap<String, List<ConfigAttribute>> result, List<Resources> resourcesList) {
        resourcesList.forEach(re ->
                {
                    List<ConfigAttribute> configAttributeList = new ArrayList<>();
                    re.getRoleSet().forEach(ro -> {
                        configAttributeList.add(new SecurityConfig(ro.getRoleName()));
                        result.put(re.getResourceName(), configAttributeList);
                    });
                }
        );
        log.debug("cache test");
    }

    @CacheEvict(value = "resourceList")
    public void clearCacheResourceList() {

    }

    @CacheEvict(value = "methodResourceList")
    public void clearCacheMethodResourceList() {

    }

    @CacheEvict(value = "pointcutResourceList")
    public void clearCachePointcutResourceList() {

    }

    @CacheEvict(value = "accessIpList")
    public void clearAccessIpList() {

    }

    public void setRoleHierarchy() {
        String allHierarchy = roleHierarchyService.findAllHierarchy();
        //roleHierarchy.setHierarchy(allHierarchy);
    }

    private void init() {
        getResourceList();
    }

}
