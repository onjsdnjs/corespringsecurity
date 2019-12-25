package io.security.corespringsecurity.security.authentication.services;

import io.security.corespringsecurity.domain.entity.Account;
import io.security.corespringsecurity.domain.entity.Role;
import io.security.corespringsecurity.repository.RoleRepository;
import io.security.corespringsecurity.repository.UserRepository;
import io.security.corespringsecurity.service.impl.LoginAttemptServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private LoginAttemptServiceImpl loginAttemptService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final String ip = request.getRemoteAddr();
        if (loginAttemptService.isBlocked(ip)) {
            throw new RuntimeException("blocked");
        }

        Account account = userRepository.findByUsername(username);
        if (account == null) {
            if (userRepository.countByUsername(username) == 0) {
                throw new UsernameNotFoundException("No user found with username: " + username);
            }
        }
        Set<String> userRoles = account.getUserRoles()
                                    .stream()
                                    .map(userRole -> userRole.getRoleName())
                                    .collect(Collectors.toSet());

        return new UserDetail(account, userRoles.stream().collect(Collectors.toList()));
    }

    @Transactional
    public Account selectUser(long id) {
        return userRepository.findById(id).orElse(new Account());
    }

    @Transactional
    public List<Account> selectUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void insertUser(Account account){

        Role role = roleRepository.findByRoleName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        account.setUserRoles(roles);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        userRepository.save(account);
    }
}