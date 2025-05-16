package com.hana.cheers_up.global.config.oauth2.userloader.impl;

import com.hana.cheers_up.global.config.oauth2.userloader.OAuth2UserLoader;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class DefaultOAuth2UserLoader implements OAuth2UserLoader {
    private DefaultOAuth2UserService delegate; // 테스트시에 final 붙이면 리플랙션 해야댐...

    public DefaultOAuth2UserLoader() {
        this.delegate = new DefaultOAuth2UserService();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        return delegate.loadUser(userRequest);
    }


    // 테스트를 위한 생성자
    public DefaultOAuth2UserLoader(DefaultOAuth2UserService delegate) {
        this.delegate = delegate;
    }

}