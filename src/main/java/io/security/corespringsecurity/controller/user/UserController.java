package io.security.corespringsecurity.controller.user;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
	
	@PostMapping(value="/mypage")
	public String myPage() throws Exception {

		return "user/mypage";
	}
}
