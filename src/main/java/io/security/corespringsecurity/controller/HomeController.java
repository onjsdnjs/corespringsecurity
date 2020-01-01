package io.security.corespringsecurity.controller;


import io.security.corespringsecurity.domain.dto.AccountDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping(value="/")
	public String home() throws Exception {
		return "home";
	}

	@GetMapping("/preAuthorize")
	@PreAuthorize("hasRole('ROLE_USER') AND #account.username == principal.username")
	public String preAuthorize(AccountDto account){
		System.out.println("account.username = " + account.getUsername() + "");
		return "home";
	}
}
