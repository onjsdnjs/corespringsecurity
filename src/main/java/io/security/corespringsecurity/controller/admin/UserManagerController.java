package io.security.corespringsecurity.controller.admin;


import io.security.corespringsecurity.domain.dto.UserDto;
import io.security.corespringsecurity.domain.entity.Account;
import io.security.corespringsecurity.domain.entity.Role;
import io.security.corespringsecurity.service.RoleService;
import io.security.corespringsecurity.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserManagerController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@GetMapping(value="/admin/users")
	public String getUsers(Model model) throws Exception {
		List<Account> accounts = userService.getUsers();
		model.addAttribute("users", accounts);
		return "admin/user/list";
	}

	@PostMapping(value="/admin/users")
	public String createUser(UserDto userDto) throws Exception {

		ModelMapper modelMapper = new ModelMapper();
		Account account = modelMapper.map(userDto, Account.class);
		userService.createUser(account);

		return "redirect:/admin/users";
	}

	@GetMapping(value = "/admin/users/{id}")
	public String getUser(@PathVariable(value = "id") Long id, Model model) {
		UserDto userDto = userService.getUser(id);
		List<Role> roleList = roleService.getRoles();

		model.addAttribute("act", (id > 0)? "modify":"add");
		model.addAttribute("user", userDto);
		model.addAttribute("roleList", roleList);

		return "admin/user/detail";
	}

	@GetMapping(value = "/admin/users/delete/{id}")
	public String removeUser(@PathVariable(value = "id") Long id, Model model) {
		userService.deleteUser(id);
		return "redirect:/admin/users";
	}
}
