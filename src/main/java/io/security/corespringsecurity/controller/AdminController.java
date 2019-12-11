package io.security.corespringsecurity.controller;


import io.security.corespringsecurity.domain.entity.User;
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
public class AdminController {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@GetMapping(value="/user/register")
	public String displayUser(HttpServletRequest request, HttpServletResponse response) throws Exception {

		return "user/register";
	}

	@PostMapping(value="/user/register")
	public String registerUser(User user) throws Exception {
		userDetailsService.insertUser(user);
		return "redirect:/user/users";
	}

	@GetMapping(value="/user/users")
	public String selectUsers(Model model) throws Exception {
		List<User> users = userDetailsService.selectUsers();
		model.addAttribute("users", users);
		return "/user/list";
	}

	@GetMapping(value="/user/{id}")
	public String selectUser(@PathVariable String id, Model model) throws Exception {
		User user = userDetailsService.selectUser(Long.valueOf(id));
		model.addAttribute("user", user);
		return "/user/detail";
	}
}
