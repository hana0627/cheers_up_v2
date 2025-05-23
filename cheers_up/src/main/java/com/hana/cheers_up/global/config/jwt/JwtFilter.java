package com.hana.cheers_up.global.config.jwt;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final String secretKey;
    private final JwtUtils jwtUtils;
    private final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    public boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();

        // 1) permitAll 경로
        if (uri.equals("/") || uri.startsWith("/api/v1")) {
            return true;
        }

        // 2) 이미 인증된 사용자(테스트용 @WithMockUser 포함)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken)) {
            return true;
        }

        return false;   // 이외의 요청에 대해서만 doFilterInternal 실행
    }

    @Override
    public void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer ")) {
            log.info("Missing or malformed Authorization header");
            response.sendRedirect("/api/v1/users/login");
            return;
        }
        String token = header.substring(7).trim();

        if (jwtUtils.isInvalidated(token) || jwtUtils.isExpired(token)) {
            log.error("Invalid or expired token");
            response.sendRedirect("/api/v1/users/login");
            return;
        }

        String userId = jwtUtils.getUserId(token);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
