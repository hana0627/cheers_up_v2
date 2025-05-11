package com.hana.cheers_up.global.security;

import com.hana.cheers_up.application.user.domain.constant.RoleType;
import com.hana.cheers_up.application.user.dto.UserAccountDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

public record CheersUpPrincipal(
        String username,
        Collection<? extends GrantedAuthority> authorities,
        String email,
        String nickname,
        String memo,
        RoleType roleType,
        Map<String, Object> oAuth2Attributes
) implements UserDetails, OAuth2User {

    public static CheersUpPrincipal of(String username, Collection<? extends GrantedAuthority> authorities, String email, String nickname, String memo, RoleType roleType) {
        return of(username, authorities, email, nickname, memo, roleType, Collections.emptyMap());
    }


    public static CheersUpPrincipal of(String username, Collection<? extends GrantedAuthority> authorities, String email, String nickname, String memo, RoleType roleType, Map<String, Object> oAuth2Attributes) {
        return new CheersUpPrincipal(
                username,
                authorities,
                email,
                nickname,
                memo,
                roleType,
                oAuth2Attributes);
    }

    public static CheersUpPrincipal from(UserAccountDto dto) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(RoleType.USER.getRoleName()));
        authorities.add(new SimpleGrantedAuthority(RoleType.MANAGER.getRoleName()));
        authorities.add(new SimpleGrantedAuthority(RoleType.ADMIN.getRoleName()));
        return CheersUpPrincipal.of(
                dto.userId(),
                authorities,
                dto.email(),
                dto.nickname(),
                dto.memo(),
                dto.roleType()
        );
    }


    public UserAccountDto toDto() {
        return UserAccountDto.of(
                username,
                email,
                nickname,
                memo,
                RoleType.USER
        );
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(RoleType.USER.getRoleName()));
        authorities.add(new SimpleGrantedAuthority(RoleType.MANAGER.getRoleName()));
        authorities.add(new SimpleGrantedAuthority(RoleType.ADMIN.getRoleName()));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2Attributes;
    }

    @Override
    public String getName() {
        return username;
    }
}
