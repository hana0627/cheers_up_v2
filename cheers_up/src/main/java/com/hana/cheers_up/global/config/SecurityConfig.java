package com.hana.cheers_up.global.config;

import com.hana.cheers_up.application.user.service.UserAccountService;
import com.hana.cheers_up.global.security.CheersUpPrincipal;
import com.hana.cheers_up.global.security.jwt.config.JwtAuthFilter;
import com.hana.cheers_up.global.security.jwt.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    //    private final CustomOAuth2UserService oAuth2UserService;
//    private final OAuth2SuccessHandler successHandler;
    private final TokenService tokenService;


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .mvcMatchers(HttpMethod.GET,
                                "/", "/index", "/users/login").permitAll()
                        .anyRequest().authenticated()
                ).formLogin(withDefaults())
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .oauth2Login(oAuth -> oAuth
                                .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                        //.successHandler(successHandler)
                )
                .csrf(csrf -> csrf.ignoringAntMatchers("/search"))
                .addFilterBefore(new JwtAuthFilter(tokenService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserAccountService userAccountService) {
        return username -> CheersUpPrincipal.from(userAccountService.searchUser(username));
    }
}

