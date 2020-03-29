package io.security.corespringsecurity.controller.user;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MessageController {
	
	@GetMapping(value={"/messages"})
	public String messages() throws Exception {
		return "user/messages";
	}

	@PostMapping(value={"/api/messages"})
	@ResponseBody
	public ResponseEntity apiMessages() throws Exception {
		return ResponseEntity.ok().body("ok");
	}
}
