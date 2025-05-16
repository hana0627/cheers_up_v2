package com.hana.cheers_up.global.config;

import com.hana.cheers_up.application.user.domain.constant.RoleType;
import com.hana.cheers_up.application.user.dto.UserAccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@RequiredArgsConstructor
public class CustomUserDetails implements OAuth2User {
//public class CustomUserDetails implements OAuth2User, UserDetails {

    private final UserAccountDto userAccountDto;

    // -Oauth2 start
    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getName() {
        return userAccountDto.userId();
    }
    // -Oauth2 end
    public String getNickname() {
        return userAccountDto.nickname();
    }
    public String getEmail() {
        return userAccountDto.email();
    }
    public RoleType getRoleType() {
        return userAccountDto.roleType();
    }
}
