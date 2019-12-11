package io.security.corespringsecurity.repository;

import io.security.corespringsecurity.domain.entity.PersistentLogin;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JpaTokenRepositoryCleaner implements Runnable {

    private final RememberMeTokenRepository rememberMeTokenRepository;

    private final long tokenValidityInMs;


    public JpaTokenRepositoryCleaner(RememberMeTokenRepository rememberMeTokenRepository, long tokenValidityInMs) {
        if (rememberMeTokenRepository == null) {
            throw new IllegalArgumentException("jdbcOperations cannot be null");
        }
        if (tokenValidityInMs < 1) {
            throw new IllegalArgumentException("tokenValidityInMs must be greater than 0. Got " + tokenValidityInMs);
        }
        this.rememberMeTokenRepository = rememberMeTokenRepository;
        this.tokenValidityInMs = tokenValidityInMs;
    }

    public void run() {
        long expiredInMs = System.currentTimeMillis() - tokenValidityInMs;

        log.info("Searching for persistent logins older than {}ms", tokenValidityInMs);

        try {
            Iterable<PersistentLogin> expired = rememberMeTokenRepository.findByLastUsedAfter(new Date(expiredInMs));
            for(PersistentLogin pl: expired){
                log.info("*** Removing persistent login for {} ***", pl.getUsername());
                rememberMeTokenRepository.delete(pl);
            }
        } catch(Throwable t) {
            log.error("**** Could not clean up expired persistent remember me tokens. ***", t);
        }
    }
}
