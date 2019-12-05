package io.security.corespringsecurity.security.authentication.services;

import io.security.corespringsecurity.domain.User;
import io.security.corespringsecurity.repository.UserRepository;
import io.security.corespringsecurity.service.LoginAttemptServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginAttemptServiceImpl loginAttemptService;

    @Autowired
    private HttpServletRequest request;

    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final String ip = request.getRemoteAddr();
        if (loginAttemptService.isBlocked(ip)) {
            throw new RuntimeException("blocked");
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            if (userRepository.countByUsername(username) == 0) {
                throw new UsernameNotFoundException("No user found with username: " + username);
            }
        }
        Set<String> userRoles = user.getUserRoles()
                                    .stream()
                                    .map(userRole -> userRole.getRoleName())
                                    .collect(Collectors.toSet());

        return new UserDetail(user, userRoles.stream().collect(Collectors.toList()));
    }

}