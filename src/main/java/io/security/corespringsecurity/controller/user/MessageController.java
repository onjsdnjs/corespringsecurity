package io.security.corespringsecurity.controller.user;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MessageController {
	
	@GetMapping(value="/messages")
	public String messages() throws Exception {

		return "user/messages";
	}
}
