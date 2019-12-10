package io.security.corespringsecurity.controller;


import io.security.corespringsecurity.domain.User;
import io.security.corespringsecurity.security.authentication.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
