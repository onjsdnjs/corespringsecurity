package io.security.corespringsecurity.security.authentication.provider;

import io.security.corespringsecurity.security.authentication.services.UserDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Slf4j
public class FormAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Authentication authenticate(Authentication auth) throws AuthenticationException {

        String loginId = auth.getName();
        String passwd = (String) auth.getCredentials();

        UserDetails userDetails = null;
        try {

            // 사용자 조회
            userDetails = userDetailsService.loadUserByUsername(loginId);

            if (userDetails == null || !passwordEncoder.matches(passwd, userDetails.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }

            if (!userDetails.isEnabled()) {
                throw new BadCredentialsException("not user confirm");
            }

        } catch(UsernameNotFoundException e) {
            log.info(e.toString());
            throw new UsernameNotFoundException(e.getMessage());
        } catch(BadCredentialsException e) {
            log.info(e.toString());
            throw new BadCredentialsException(e.getMessage());
        } catch(Exception e) {
            log.info(e.toString());
            throw new RuntimeException(e.getMessage());
        }

        return new UsernamePasswordAuthenticationToken(((UserDetail)userDetails).getAccount(), null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
