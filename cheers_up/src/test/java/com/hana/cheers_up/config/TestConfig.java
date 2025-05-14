//package com.hana.cheers_up.config;
//
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import java.time.Instant;
//import java.util.List;
//import java.util.Map;
//
//@TestConfiguration
//class TestConfig {
//    @Bean
//    public OAuth2UserService<OAuth2UserRequest, OAuth2User> mockOAuth2UserService() {
//        return userRequest -> {
//            Map<String, Object> profile = Map.of("nickname", "하나");
//            Map<String, Object> kakaoAccount = Map.of(
//                    "profile_nickname_needs_agreement", false,
//                    "profile", profile,
//                    "has_email", true,
//                    "email_needs_agreement", false,
//                    "is_email_valid", true,
//                    "is_email_verified", true,
//                    "email", "hanana9506@naver.com"
//            );
//            Map<String, Object> attributes = Map.of(
//                    "id", 1234567890L,
//                    "connected_at", Instant.now().toString(),
//                    "properties", Map.of("nickname", "공주하나"),
//                    "kakao_account", kakaoAccount
//            );
//
//            return new DefaultOAuth2User(
//                    List.of(new SimpleGrantedAuthority("ROLE_USER")),
//                    attributes,
//                    "id"
//            );
//        };
//    }
//}