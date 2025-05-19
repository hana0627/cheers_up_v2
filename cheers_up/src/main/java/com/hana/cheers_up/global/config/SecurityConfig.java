package com.hana.cheers_up.global.config;

import com.hana.cheers_up.global.config.jwt.JwtFilter;
import com.hana.cheers_up.global.config.jwt.JwtUtils;
import com.hana.cheers_up.global.config.oauth2.CustomOAuth2SuccessHandler;
import com.hana.cheers_up.global.config.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtils jwtUtils;
    private final CustomOAuth2UserService oAuth2UserService;
    private final CustomOAuth2SuccessHandler oAuth2SuccessHandler;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .mvcMatchers(HttpMethod.GET,
                                "/", "/index", "/api/v1/users/login").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/api/v1/users/login")
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                )
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .addFilterBefore(new JwtFilter(secretKey, jwtUtils), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
