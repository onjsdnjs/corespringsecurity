package io.security.corespringsecurity.service.impl;

import io.security.corespringsecurity.service.LoginAttemptService;
import org.springframework.cglib.core.internal.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final int MAX_ATTEMPT = 5;
    private Map<String, Integer> attemptsCache;

    public LoginAttemptServiceImpl() {
        super();
        attemptsCache = new HashMap<String, Integer>();
    }

    @Override
    public void loginSucceeded(final String key) {

        attemptsCache.put(key, 0);
    }

    @Override
    public void loginFailed(final String key) {
        int attempts;
        try {
            attempts = attemptsCache.get(key);
        } catch (Exception e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    @Override
    public boolean isBlocked(final String key) {
        try {
            return attemptsCache.get(key) >= MAX_ATTEMPT;
        } catch (Exception e) {
            return false;
        }
    }
}
