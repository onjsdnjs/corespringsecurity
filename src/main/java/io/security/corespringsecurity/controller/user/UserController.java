package io.security.corespringsecurity.controller.user;


import io.security.corespringsecurity.domain.dto.UserDto;
import io.security.corespringsecurity.domain.entity.Account;
import io.security.corespringsecurity.service.RoleService;
import io.security.corespringsecurity.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;
	
	@GetMapping(value="/users")
	public String createUser() throws Exception {

		return "user/login/register";
	}

	@PostMapping(value="/users")
	public String createUser(UserDto userDto) throws Exception {

		ModelMapper modelMapper = new ModelMapper();
		Account account = modelMapper.map(userDto, Account.class);
		userService.createUser(account);

		return "redirect:/";
	}
}
