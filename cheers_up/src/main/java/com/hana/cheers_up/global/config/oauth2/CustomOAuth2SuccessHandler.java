package com.hana.cheers_up.global.config.oauth2;

import com.hana.cheers_up.global.config.CustomUserDetails;
import com.hana.cheers_up.global.config.jwt.JwtUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

    public CustomOAuth2SuccessHandler(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateToken(
                principal.getName(),
                principal.getNickname(),
                principal.getEmail(),
                principal.getRoleType()
        );
        response.sendRedirect("/users/login?token=" + jwtToken);
    }
}