package io.security.corespringsecurity.test;

import io.security.corespringsecurity.security.aop.CustomMethodSecurityInterceptor;
import io.security.corespringsecurity.security.processor.ProtectPointcutPostProcessor;
import io.security.corespringsecurity.test.aop.AopFirstService;
import io.security.corespringsecurity.test.aop.AopSecondService;
import io.security.corespringsecurity.test.liveaop.LiveAopFirstService;
import io.security.corespringsecurity.test.method.MethodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@Slf4j
public class TestController {

    @Autowired
    private ProtectPointcutPostProcessor protectPoitcutPostProcessor;

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
    public void addPointcut(String fullName, String roleName) throws Exception {

        String expression = "execution(* io.security.corespringsecurity.test.liveaop.*Service.*(..))";
        ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

        String typeName = "io.security.corespringsecurity.test.liveaop.LiveAopFirstService";

        Class<?> type = ClassUtils.resolveClassName(typeName, beanClassLoader);
        List<ConfigAttribute> attr = Arrays.asList(new SecurityConfig("ROLE_MANAGER"));

        Map<String, List<ConfigAttribute>> pointcutMap = new LinkedHashMap<>();
        pointcutMap.put(expression,attr);
        String beanName = type.getSimpleName().substring(0, 1).toLowerCase() + type.getSimpleName().substring(1);
        protectPoitcutPostProcessor.setPointcutMap(pointcutMap);
        protectPoitcutPostProcessor.postProcessBeforeInitialization(type.getDeclaredConstructor().newInstance(),beanName);

//        mapBasedMethodSecurityMetadataSource.addSecureMethod(type,methodName, attr);
    }
}
