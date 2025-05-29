package com.hana.cheers_up.global.config.jwt;

import com.hana.cheers_up.global.exception.ApplicationException;
import com.hana.cheers_up.global.exception.constant.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
            throw new ApplicationException(ErrorCode.UNAUTHORIZE, "로그인이 필요한 서비스입니다.");
        }
        String token = header.substring(7).trim();

        if (jwtUtils.isInvalidated(token) || jwtUtils.isExpired(token)) {
            log.error("Invalid or expired token");
            throw new ApplicationException(ErrorCode.UNAUTHORIZE, "로그인이 필요한 서비스입니다.");
        }

        String userId = jwtUtils.getUserId(token);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
