package io.security.corespringsecurity.service;

import io.security.corespringsecurity.security.aop.CustomMethodSecurityInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class MethodSecurityService {

    private MapBasedMethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource;
    private AnnotationConfigServletWebServerApplicationContext applicationContext;
    private CustomMethodSecurityInterceptor methodSecurityInterceptor;

    public MethodSecurityService(MapBasedMethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource, AnnotationConfigServletWebServerApplicationContext applicationContext, CustomMethodSecurityInterceptor methodSecurityInterceptor) {
        this.mapBasedMethodSecurityMetadataSource = mapBasedMethodSecurityMetadataSource;
        this.applicationContext = applicationContext;
        this.methodSecurityInterceptor = methodSecurityInterceptor;
    }

    public void addMethodSecured(String className, String roleName) throws Exception{

        int lastDotIndex = className.lastIndexOf(".");
        String methodName = className.substring(lastDotIndex + 1);
        String typeName = className.substring(0, lastDotIndex);
        Class<?> type = ClassUtils.resolveClassName(typeName, ClassUtils.getDefaultClassLoader());
        String beanName = type.getSimpleName().substring(0, 1).toLowerCase() + type.getSimpleName().substring(1);

        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(type.getDeclaredConstructor().newInstance());
        proxyFactory.addAdvice(methodSecurityInterceptor);
        Object proxy = proxyFactory.getProxy();

        List<ConfigAttribute> attr = Arrays.asList(new SecurityConfig(roleName));
        mapBasedMethodSecurityMetadataSource.addSecureMethod(type,methodName, attr);

        DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry)applicationContext.getBeanFactory();
        registry.destroySingleton(beanName);
        registry.registerSingleton(beanName, proxy);

    }

    public void removeMethodSecured(String className) throws Exception{

        int lastDotIndex = className.lastIndexOf(".");
        String typeName = className.substring(0, lastDotIndex);
        Class<?> type = ClassUtils.resolveClassName(typeName, ClassUtils.getDefaultClassLoader());
        String beanName = type.getSimpleName().substring(0, 1).toLowerCase() + type.getSimpleName().substring(1);
        Object newInstance = type.getDeclaredConstructor().newInstance();

        DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry)applicationContext.getBeanFactory();
        Object singleton = registry.getSingleton(beanName);
        registry.destroySingleton(beanName);
        registry.registerSingleton(beanName, newInstance);

    }
}
