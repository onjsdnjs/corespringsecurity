package io.security.corespringsecurity.controller;


import io.security.corespringsecurity.domain.dto.ResourcesDto;
import io.security.corespringsecurity.domain.entity.Resources;
import io.security.corespringsecurity.domain.entity.Role;
import io.security.corespringsecurity.repository.RoleRepository;
import io.security.corespringsecurity.service.ResourcesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ResourcesController {
	
	@Autowired
	private ResourcesService resourcesService;

	@Autowired
	private RoleRepository roleRepository;

	private ModelMapper modelMapper;
	
	@GetMapping(value="/resource/register")
	public String displayResources() throws Exception {

		return "resource/register";
	}

	@PostMapping(value="/resource/register")
	public String registerResources(ResourcesDto resourcesDto) throws Exception {
		Role role = roleRepository.findByRoleName(resourcesDto.getRoleName());
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		Resources resources = modelMapper.map(resourcesDto, Resources.class);
		resources.setRoleSet(roles);
		resourcesService.insertResources(resources);
		return "redirect:/resource/resources";
	}

	@GetMapping(value="/resource/resources")
	public String selectResources(Model model) throws Exception {
		List<Resources> resources = resourcesService.selectResources();
		model.addAttribute("resources", resources);
		return "/resource/list";
	}

	@GetMapping(value="/resource/{id}")
	public String selectResources(@PathVariable String id, Model model) throws Exception {
		Resources resources = resourcesService.selectResources(Long.valueOf(id));
		model.addAttribute("resources", resources);
		return "/resource/detail";
	}
}
