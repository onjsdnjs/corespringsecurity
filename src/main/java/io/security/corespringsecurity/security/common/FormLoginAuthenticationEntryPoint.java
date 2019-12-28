package io.security.corespringsecurity.security.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.security.corespringsecurity.util.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FormLoginAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private ObjectMapper mapper = new ObjectMapper();

    public FormLoginAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        if (WebUtil.isContentTypeJson(request)) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(this.mapper.writeValueAsString(ResponseEntity.status(HttpStatus.UNAUTHORIZED)));

        } else {
            super.commence(request, response, authException);
        }
    }
}
