package io.security.corespringsecurity.controller.admin;

import io.security.corespringsecurity.domain.entity.AccessIp;
import io.security.corespringsecurity.domain.entity.RoleHierarchy;
import io.security.corespringsecurity.repository.AccessIpRepository;
import io.security.corespringsecurity.repository.RoleHierarchyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IpAddressController {

	@Autowired
	private AccessIpRepository accessIpRepository;

	@GetMapping(value="/admin/accessIp")
	public String getIpAddress(Model model) throws Exception {

		List<AccessIp> accessIp = accessIpRepository.findAll();
		model.addAttribute("accessIp", accessIp);

		return "admin/accessIp/list";
	}
}
