package com.hana.cheers_up.global.config.oauth2;

import com.hana.cheers_up.application.user.dto.UserAccountDto;
import com.hana.cheers_up.application.user.service.UserAccountService;
import com.hana.cheers_up.global.config.oauth2.provider.Oauth2UserInfo;
import com.hana.cheers_up.global.config.oauth2.provider.impl.KakaoUserInfo;
import com.hana.cheers_up.global.config.CustomUserDetails;
import com.hana.cheers_up.global.config.oauth2.userloader.OAuth2UserLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserAccountService userAccountService;
    private final OAuth2UserLoader oAuth2UserLoader;


    @Override
    public CustomUserDetails loadUser(OAuth2UserRequest userRequest) {
        if(userRequest == null) {
            throw new IllegalStateException("userRequest is null");
        }

        OAuth2User oAuth2User = oAuth2UserLoader.loadUser(userRequest);

        Oauth2UserInfo oauth2UserInfo = null;

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); //registrationId = "kakao";


        if("kakao".equals(registrationId)) {
            oauth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }
        if(oauth2UserInfo == null) {
            throw new IllegalStateException("지원하지 않는 로그인 타입입니다.");
        }

        String providerId = String.valueOf(oauth2UserInfo.getProviderId()); // 카카오 스펙에서의 Long타입의 id값
        String username = registrationId + "_" + providerId;

        UserAccountDto userDto = userAccountService.searchUserOrSave(username, oauth2UserInfo);
//        return new CustomUserDetails(userDto, oAuth2User.getAttributes());
        return new CustomUserDetails(userDto);
    }
}
