package io.security.corespringsecurity.security.filter;

import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PermitAllFilter extends FilterSecurityInterceptor {
    private static final String FILTER_APPLIED = "__spring_security_filterSecurityInterceptor_filterApplied";
    private List<RequestMatcher> permitAllRequestMatcher = new ArrayList<>();
    public PermitAllFilter(String... permitAllPattern) {
        createPermitAllPattern(permitAllPattern);
    }

    @Override
    protected InterceptorStatusToken beforeInvocation(Object object) {
        boolean permitAll = false;
        HttpServletRequest request = ((FilterInvocation) object).getRequest();
        for (RequestMatcher requestMatcher : permitAllRequestMatcher) {
            if (requestMatcher.matches(request)) {
                permitAll = true;
                break;
            }
        }
        if (permitAll) return null;
        return super.beforeInvocation(object);
    }

    @Override
    public void invoke(FilterInvocation fi) throws IOException, ServletException {
            InterceptorStatusToken token = beforeInvocation(fi);
    }
    private void createPermitAllPattern(String... permitAllPattern) {
        for (String pattern : permitAllPattern) {
            permitAllRequestMatcher.add(new AntPathRequestMatcher(pattern));
        }
    }
}
