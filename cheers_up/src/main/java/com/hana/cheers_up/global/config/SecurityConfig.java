package com.hana.cheers_up.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.cheers_up.application.user.domain.constant.RoleType;
import com.hana.cheers_up.application.user.dto.UserAccountDto;
import com.hana.cheers_up.application.user.service.UserAccountService;
import com.hana.cheers_up.global.security.CheersUpPrincipal;
import com.hana.cheers_up.global.security.KakaoOAuth2Response;
import com.hana.cheers_up.global.security.jwt.config.JwtAuthFilter;
import com.hana.cheers_up.global.security.OAuth2SuccessHandler;
import com.hana.cheers_up.global.security.jwt.service.TokenService;
import com.nimbusds.oauth2.sdk.ErrorObject;
import com.nimbusds.oauth2.sdk.ErrorResponse;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.PrintWriter;

import static org.springframework.security.config.Customizer.withDefaults;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    //    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler successHandler;
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
        return username -> userAccountService
                .searchUser(username)
                .map(CheersUpPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다 - username : " + username));
    }


    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(
            UserAccountService userAccountService
    ) {
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

        return userRequest -> {
            OAuth2User oAuth2User = delegate.loadUser(userRequest);
            KakaoOAuth2Response kakaoResponse = KakaoOAuth2Response.from(oAuth2User.getAttributes());

            String registrationId = userRequest.getClientRegistration().getRegistrationId(); //registrationId = "kakao";
            String providerId = String.valueOf(kakaoResponse.id()); // 카카오 스펙에서의 Long타입의 id값
            String username = registrationId + "_" + providerId;


            return userAccountService.searchUser(username)
                    .map(CheersUpPrincipal::from)
                    .orElseGet(() ->
                            CheersUpPrincipal.from(
                                    userAccountService.saveUser(
                                            UserAccountDto.of(username, kakaoResponse.email(), kakaoResponse.nickname(), null, RoleType.USER)
                                    )
                            )
                    );
        };
    }

}
