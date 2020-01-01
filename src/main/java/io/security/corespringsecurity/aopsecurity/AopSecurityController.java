package io.security.corespringsecurity.aopsecurity;

import io.security.corespringsecurity.aopsecurity.method.AopMethodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class AopSecurityController {

    @Autowired
    MapBasedMethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource;

    @Autowired
    private AopMethodService aopMethodService;

    @GetMapping("/methodSecured")
    public String methodSecured(){
        aopMethodService.methodSecured();
        return "method3";
    }
}
