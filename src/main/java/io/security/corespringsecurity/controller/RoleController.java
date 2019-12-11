package io.security.corespringsecurity.controller;

import io.security.corespringsecurity.domain.entity.Role;
import io.security.corespringsecurity.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@GetMapping(value="/role/register")
	public String displayRole() throws Exception {

		return "role/register";
	}

	@PostMapping(value="/role/register")
	public String registerRole(Role role) throws Exception {
		roleService.insertRole(role);
		return "redirect:/role/roles";
	}

	@GetMapping(value="/role/roles")
	public String selectRoles(Model model) throws Exception {
		List<Role> roles = roleService.selectRoles();
		model.addAttribute("roles", roles);
		return "/role/list";
	}

	@GetMapping(value="/role/{id}")
	public String selectRole(@PathVariable String id, Model model) throws Exception {
		Role role = roleService.selectRole(Long.valueOf(id));
		model.addAttribute("role", role);
		return "/role/detail";
	}
}
