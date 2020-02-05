package io.security.corespringsecurity.security.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.security.corespringsecurity.repository.JpaPersistentTokenRepository;
import io.security.corespringsecurity.repository.RememberMeTokenRepository;
import io.security.corespringsecurity.security.authentication.handler.*;
import io.security.corespringsecurity.security.authentication.provider.AjaxAuthenticationProvider;
import io.security.corespringsecurity.security.authentication.provider.FormAuthenticationProvider;
import io.security.corespringsecurity.security.authentication.services.FormRememberMeServices;
import io.security.corespringsecurity.security.authentication.services.FormWebAuthenticationDetailsSource;
import io.security.corespringsecurity.security.factory.UrlResourcesMapFactoryBean;
import io.security.corespringsecurity.security.filter.AjaxLoginProcessingFilter;
import io.security.corespringsecurity.security.filter.PermitAllFilter;
import io.security.corespringsecurity.security.metaDataSource.UrlSecurityMetadataSource;
import io.security.corespringsecurity.security.voter.IpAddressVoter;
import io.security.corespringsecurity.service.SecurityResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private String[] ignoredMatcherPattern = {"/static/**", "/css/**", "/js/**", "/static/css/images/**", "/webjars/**", "/**/favicon.ico"};
    private String[] permitAllPattern = {"/", "/home", "/users", "/login", "/errorpage/**"};

    public static final String AUTHENTICATION_HEADER_NAME = "Authorization";
    public static final String AUTHENTICATION_URL = "/api/auth/login";
    public static final String REFRESH_TOKEN_URL = "/api/auth/token";
    public static final String API_ROOT_URL = "/api/**";

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private FormAuthenticationProvider commonAuthenticationProvider;
    @Autowired
    private AjaxAuthenticationProvider ajaxAuthenticationProvider;
    @Autowired
    private FormAuthenticationSuccessHandler formAuthenticationSuccessHandler;
    @Autowired
    private FormAuthenticationFailureHandler formAuthenticationFailureHandler;
    @Autowired
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;
    @Autowired
    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;
    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    @Autowired
    private AccessDecisionManager accessDecisionManager;
    @Autowired
    private FormWebAuthenticationDetailsSource authenticationDetailsSource;
    @Autowired
    private FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;
    @Autowired
    private RememberMeServices rememberMeServices;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    private SecurityResourceService securityResourceService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) {
//        auth.authenticationProvider(commonAuthenticationProvider);
//        auth.authenticationProvider(ajaxAuthenticationProvider);
//    }

    public AuthenticationManager ajaxAuthenticationManager() {
        List<AuthenticationProvider> authProviderList = new ArrayList<>();
        authProviderList.add(ajaxAuthenticationProvider);
        ProviderManager providerManager = new ProviderManager(authProviderList);
        return providerManager;
    }

    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> authProviderList = new ArrayList<>();
        authProviderList.add(commonAuthenticationProvider);
        authProviderList.add(ajaxAuthenticationProvider);
        ProviderManager providerManager = new ProviderManager(authProviderList);
        return providerManager;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(ignoredMatcherPattern);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        http
                .authorizeRequests()
                .anyRequest().authenticated()
        .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                .accessDeniedPage("/denied")
                .accessDeniedHandler(accessDeniedHandler)

        .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_proc")
                .defaultSuccessUrl("/index")
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(formAuthenticationSuccessHandler)
                .failureHandler(formAuthenticationFailureHandler)
                .authenticationDetailsSource(authenticationDetailsSource)
//                .permitAll()

        .and()
                .sessionManagement()
//                .invalidSessionUrl("/users/invalidSession.html")
                .maximumSessions(1) // -1 : 무제한 로그인 세션 허용
                .maxSessionsPreventsLogin(true) // false : 동시 로그인을 하지 못하도록 차단함
                //.expiredUrl("/login?expired=true")
                .sessionRegistry(sessionRegistry()).and()
                .sessionFixation().migrateSession()

        .and()
                .logout()
                .logoutSuccessHandler(logoutSuccessHandler)
                //.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/index")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("SESSION", "JSESSIONID", "remember-me")
//                .permitAll()

        .and()
                .rememberMe()
                .alwaysRemember(true)
                .rememberMeServices(rememberMeServices)
                .tokenValiditySeconds(3600)
                .key("anymobi")

        .and()
                .addFilterBefore(buildAjaxLoginProcessingFilter(AUTHENTICATION_URL), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(filter, CsrfFilter.class)
                .addFilterBefore(permitAllFilter(), FilterSecurityInterceptor.class)
                .csrf().disable();

        customConfigurer(http);
    }

    private void customConfigurer(HttpSecurity http) throws Exception {
        http
                .apply(new AjaxLoginConfigurer<>())
                .successHandlerAjax(ajaxAuthenticationSuccessHandler)
                .failureHandlerAjax(ajaxAuthenticationFailureHandler)
                .loginProcessingUrl(AUTHENTICATION_URL)
                .setAuthenticationManager(ajaxAuthenticationManager())
                .readAndWriteMapper(objectMapper);


    }

    protected AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter(String loginEntryPoint){
        AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter();
        filter.setAuthenticationManager(ajaxAuthenticationManager());
        return filter;
    }

    @Bean
    public PermitAllFilter permitAllFilter() {
        PermitAllFilter permitAllFilter = new PermitAllFilter(permitAllPattern);
        //commonFilterSecurityInterceptor.setAuthenticationManager(authenticationManager());
        permitAllFilter.setAccessDecisionManager(accessDecisionManager);
        permitAllFilter.setSecurityMetadataSource(filterInvocationSecurityMetadataSource);
        permitAllFilter.setRejectPublicInvocations(false);
        return permitAllFilter;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(permitAllFilter());
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        CommonAccessDeniedHandler commonAccessDeniedHandler = new CommonAccessDeniedHandler();
        commonAccessDeniedHandler.setErrorPage("/denied");
        return commonAccessDeniedHandler;
    }

    @Bean
    public RememberMeServices rememberMeServices(PersistentTokenRepository ptr) {
        FormRememberMeServices rememberMeServices = new FormRememberMeServices("anymobi", userDetailsService, ptr);
        return rememberMeServices;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(RememberMeTokenRepository rmtr) {
        return new JpaPersistentTokenRepository(rmtr);
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public FilterInvocationSecurityMetadataSource urlSecurityMetadataSource(SecurityResourceService securityResourceService) {
        return new UrlSecurityMetadataSource(urlResourcesMapFactoryBean(securityResourceService).getObject(),securityResourceService);
    }

    @Bean
    public UrlResourcesMapFactoryBean urlResourcesMapFactoryBean(SecurityResourceService securityResourceService){
        UrlResourcesMapFactoryBean urlResourcesMapFactoryBean = new UrlResourcesMapFactoryBean();
        urlResourcesMapFactoryBean.setSecurityResourceService(securityResourceService);
        return urlResourcesMapFactoryBean;
    }

    @Bean
    public AccessDecisionManager affirmativeBased(SecurityResourceService securityResourceService) {
        AffirmativeBased accessDecisionManager = new AffirmativeBased(getAccessDecisionVoters(securityResourceService));
        accessDecisionManager.setAllowIfAllAbstainDecisions(false); // 접근 승인 거부 보류시 접근 허용은 true 접근 거부는 false
        return accessDecisionManager;
    }

    private List<AccessDecisionVoter<?>> getAccessDecisionVoters(SecurityResourceService securityResourceService) {

        AuthenticatedVoter authenticatedVoter = new AuthenticatedVoter();
        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        IpAddressVoter ipAddressVoter = new IpAddressVoter(securityResourceService);

        List<AccessDecisionVoter<? extends Object>> accessDecisionVoterList = Arrays.asList(ipAddressVoter, authenticatedVoter, webExpressionVoter, roleVoter());
        return accessDecisionVoterList;
    }

    @Bean
    public RoleHierarchyVoter roleVoter() {
        RoleHierarchyVoter roleHierarchyVoter = new RoleHierarchyVoter(roleHierarchy());
        roleHierarchyVoter.setRolePrefix("ROLE_");
        return roleHierarchyVoter;
    }
    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        return roleHierarchy;
    }
}
