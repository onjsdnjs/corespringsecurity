package io.security.corespringsecurity.controller.user;


import io.security.corespringsecurity.domain.dto.AccountDto;
import io.security.corespringsecurity.domain.entity.Account;
import io.security.corespringsecurity.domain.entity.Role;
import io.security.corespringsecurity.repository.RoleRepository;
import io.security.corespringsecurity.security.service.AccountContext;
import io.security.corespringsecurity.security.token.AjaxAuthenticationToken;
import io.security.corespringsecurity.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@GetMapping(value="/users")
	public String createUser() throws Exception {

		return "user/login/register";
	}

	@PostMapping(value="/users")
	public String createUser(AccountDto accountDto) throws Exception {

		ModelMapper modelMapper = new ModelMapper();
		Account account = modelMapper.map(accountDto, Account.class);
		account.setPassword(passwordEncoder.encode(accountDto.getPassword()));

		userService.createUser(account);

		return "redirect:/";
	}

	@GetMapping(value="/mypage")
	public String myPage(@AuthenticationPrincipal Account account, Authentication authentication, Principal principal) throws Exception {

		String username1 = account.getUsername();
		System.out.println("username1 = " + username1);

		Account account2 = (Account) authentication.getPrincipal();
		String username2 = account2.getUsername();
		System.out.println("username2 = " + username2);

		Account account3 = null;
		if (principal instanceof UsernamePasswordAuthenticationToken) {
			account3 = (Account) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

		}else if(principal instanceof AjaxAuthenticationToken){
			account3 = (Account) ((AjaxAuthenticationToken) principal).getPrincipal();
		}

		String username3 = account3.getUsername();
		System.out.println("username3 = " + username3);

		Account account4 = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username4 = account4.getUsername();
		System.out.println("username4 = " + username4);

		return "user/mypage";
	}
}
