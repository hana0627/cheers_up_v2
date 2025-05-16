package com.hana.cheers_up.global.config.oauth2.userloader;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserLoader {
    OAuth2User loadUser(OAuth2UserRequest userRequest);
}
