package io.security.corespringsecurity.security.factory;

import io.security.corespringsecurity.service.SecurityResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;

import java.util.LinkedHashMap;
import java.util.List;

    @Slf4j
    public class MethodResourcesMapFactoryBean implements FactoryBean<LinkedHashMap<String, List<ConfigAttribute>>> {

        private SecurityResourceService securityResourceService;
        private String resourceType;

        public void setResourceType(String resourceType) {
            this.resourceType = resourceType;
        }

        public void setSecurityResourceService(SecurityResourceService securityResourceService) {
            this.securityResourceService = securityResourceService;
        }

        private LinkedHashMap<String, List<ConfigAttribute>> resourcesMap;

        public void init() {
            if ("method".equals(resourceType)) {
                resourcesMap = securityResourceService.getMethodResourceList();
            }else if("pointcut".equals(resourceType)){
                resourcesMap = securityResourceService.getPointcutResourceList();
            }
        }

        public LinkedHashMap<String, List<ConfigAttribute>> getObject() {
            if (resourcesMap == null) {
                init();
            }
            return resourcesMap;
        }

        @SuppressWarnings("rawtypes")
        public Class<LinkedHashMap> getObjectType() {
            return LinkedHashMap.class;
        }

        public boolean isSingleton() {
            return true;
        }
    }
