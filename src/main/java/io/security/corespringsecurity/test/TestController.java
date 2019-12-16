package io.security.corespringsecurity.test;

import io.security.corespringsecurity.security.advice.CustomMethodSecurityInterceptor;
import io.security.corespringsecurity.test.aop.AopFirstService;
import io.security.corespringsecurity.test.aop.AopSecondService;
import io.security.corespringsecurity.test.liveaop.LiveAopFirstService;
import io.security.corespringsecurity.test.method.MethodService;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@Slf4j
public class TestController {

    /*@Autowired
    private ProtectPointcutPostProcessor protectPoitcutPostProcessor;*/

    @Autowired
    MapBasedMethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource;

    @Autowired
    private MethodService methodService;

    @Autowired
    private AopFirstService aopFirstService;

    @Autowired
    private AopSecondService aopSecondService;

    @Autowired
    private LiveAopFirstService liveAopFirstService;

    @Autowired
    AnnotationConfigServletWebServerApplicationContext applicationContext;

    @Autowired
    CustomMethodSecurityInterceptor methodSecurityInterceptor;

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

    @GetMapping("/method")
    public String methodTest(){
        methodService.methodTest();
        return "method";
    }

    @GetMapping("/method2")
    public String methodTest2(){
        log.debug(methodService.getClass().getSimpleName());
        methodService.methodTest2(methodService);
        return "method2";
    }

    @GetMapping("/method3")
    public String methodTest3(){
        methodService.methodTest3();
        return "method3";
    }

    @GetMapping("/aop1")
    public String aopFirstService(){
        aopFirstService.aopService();
        return "aop1";
    }

    @GetMapping("/aop2")
    public String aopSecondService(){
        aopSecondService.aopService();
        return "aop2";
    }

    @GetMapping("/liveaop")
    public String liveAopService(){
        liveAopFirstService.liveAopService();
        return "liveAop";
    }

    @GetMapping("/addAop")
    public void addPointcut(String fullName, String roleName){
        Object obj = null;
        try {
            Class<?> classType = Class.forName("io.security.corespringsecurity.test.liveaop.LiveAopFirstService");
            ProxyFactory proxyFactory = new ProxyFactory();
            proxyFactory.setTarget(classType.getDeclaredConstructor().newInstance());
            proxyFactory.addAdvice(methodSecurityInterceptor);
            Object proxy = proxyFactory.getProxy();

            List<ConfigAttribute> attr = Arrays.asList(new SecurityConfig("ROLE_MANAGER"));
            mapBasedMethodSecurityMetadataSource.addSecureMethod(classType,"liveAopService", attr);

//          Map<String, List<ConfigAttribute>> pointcutMap = protectPoitcutPostProcessor.getPointcutMap();
//          pointcutMap.put("execution(* io.security.corespringsecurity.test.liveaop.*Service.*(..))",attr);
//          String beanName = classType.getSimpleName().substring(0, 1).toLowerCase() + classType.getSimpleName().substring(1);
//          protectPoitcutPostProcessor.setPointcutMap(pointcutMap);
//          protectPoitcutPostProcessor.postProcessBeforeInitialization(obj,beanName);
            obj = applicationContext.getBean(classType);
            obj = proxy;
            System.out.println(obj);
//
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
