package io.security.corespringsecurity.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	@GetMapping(value="/")
	public String home() throws Exception {
		return "home";
	}

	@PostMapping(value="/")
	public String postHome() throws Exception {
		return "home";
	}

}
