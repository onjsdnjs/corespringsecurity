package io.security.corespringsecurity.aopsecurity;

import io.security.corespringsecurity.aopsecurity.service.AopMethodService;
import io.security.corespringsecurity.domain.dto.AccountDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class AopSecurityController {

    @Autowired
    private AopMethodService aopMethodService;

    @GetMapping("/preAuthorize")
    @PreAuthorize("hasRole('ROLE_USER') AND #account.username == principal.username")
    public String preAuthorize(AccountDto account, Model model){

        System.out.println("account.username = " + account.getUsername() + "");
        model.addAttribute("method", "Success @PreAuthorize");

        return "aop/method";
    }

    @GetMapping("/methodSecured")
    public String methodSecured(Model model){

        aopMethodService.methodSecured();
        model.addAttribute("method", "Success MethodSecured");

        return "aop/method";
    }
}
