package io.security.corespringsecurity.controller;


import io.security.corespringsecurity.security.authentication.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@GetMapping(value="/")
	public String home() throws Exception {
		return "home";
	}

	@GetMapping(value="/login")
	public String login() throws Exception {
		return "login";
	}

}
