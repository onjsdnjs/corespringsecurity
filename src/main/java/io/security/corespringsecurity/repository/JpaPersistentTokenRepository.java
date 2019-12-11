package io.security.corespringsecurity.repository;

import io.security.corespringsecurity.domain.entity.PersistentLogin;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.util.Date;
import java.util.List;

public class JpaPersistentTokenRepository implements PersistentTokenRepository {

    private final RememberMeTokenRepository rememberMeTokenRepository;

    public JpaPersistentTokenRepository(RememberMeTokenRepository rememberMeTokenRepository) {
        this.rememberMeTokenRepository = rememberMeTokenRepository;
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        PersistentLogin newToken = new PersistentLogin(token);
        this.rememberMeTokenRepository.save(newToken);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        PersistentLogin token = this.rememberMeTokenRepository.findBySeries(series);
        if (token != null) {
            token.setToken(tokenValue);
            token.setLastUsed(lastUsed);
            this.rememberMeTokenRepository.save(token);
        }
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        PersistentLogin token = this.rememberMeTokenRepository.findBySeries(seriesId);
        return new PersistentRememberMeToken(token.getUsername(),
                token.getSeries(),
                token.getToken(),
                token.getLastUsed());
    }

    @Override
    public void removeUserTokens(String username) {
        List<PersistentLogin> tokens = this.rememberMeTokenRepository.findByUsername(username);
        this.rememberMeTokenRepository.deleteAll(tokens);
    }

}
