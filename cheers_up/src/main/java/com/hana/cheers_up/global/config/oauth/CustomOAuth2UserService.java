package com.hana.cheers_up.global.config.oauth;

import com.hana.cheers_up.application.user.service.UserAccountService;
import com.hana.cheers_up.global.security.CheersUpPrincipal;
import com.hana.cheers_up.global.security.KakaoOAuth2Response;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserAccountService userAccountService;
    final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        KakaoOAuth2Response kakaoResponse = KakaoOAuth2Response.from(oAuth2User.getAttributes());


        String registrationId = userRequest.getClientRegistration().getRegistrationId(); //registrationId = "kakao";
        String providerId = String.valueOf(kakaoResponse.id()); // 카카오 스펙에서의 Long타입의 id값
        String username = registrationId + "_" + providerId;

        return CheersUpPrincipal.from(userAccountService.searchUserOrSave(username, kakaoResponse));

    }
}
