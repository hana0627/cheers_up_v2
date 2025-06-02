package com.hana.cheers_up.global.config.oauth2;

import com.hana.cheers_up.application.user.dto.UserAccountDto;
import com.hana.cheers_up.application.user.service.UserAccountService;
import com.hana.cheers_up.global.config.oauth2.provider.Oauth2UserInfo;
import com.hana.cheers_up.global.config.oauth2.provider.impl.KakaoUserInfo;
import com.hana.cheers_up.global.config.CustomUserDetails;
import com.hana.cheers_up.global.config.oauth2.userloader.OAuth2UserLoader;
import com.hana.cheers_up.global.exception.ApplicationException;
import com.hana.cheers_up.global.exception.constant.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserAccountService userAccountService;
    private final OAuth2UserLoader oAuth2UserLoader;


    @Override
    public CustomUserDetails loadUser(OAuth2UserRequest userRequest) {
        if(userRequest == null) {
            throw new ApplicationException(ErrorCode.NULL_USER_REQUEST, "userRequest is null");
        }

        OAuth2User oAuth2User = oAuth2UserLoader.loadUser(userRequest);

        Oauth2UserInfo oauth2UserInfo = null;

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); //registrationId = "kakao";


        if("kakao".equals(registrationId)) {
            oauth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
            return processOAuth2User("kakao", oauth2UserInfo);
        }
//        if(oauth2UserInfo == null) {
            throw new ApplicationException(ErrorCode.UNSUPPORTED_LOGIN_TYPE, "지원하지 않는 로그인 타입입니다.");
//        }


//        String providerId = String.valueOf(oauth2UserInfo.getProviderId()); // 카카오 스펙에서의 Long타입의 id값
//        String username = registrationId + "_" + providerId;

//        UserAccountDto userDto = userAccountService.searchUserOrSave(username, oauth2UserInfo);
//        return new CustomUserDetails(userDto);
    }

    @Transactional
    public CustomUserDetails processKakaoUser(KakaoUserInfo kakaoUserInfo) {
        if(kakaoUserInfo == null) {
            throw new ApplicationException(ErrorCode.NULL_USER_REQUEST, "kakaoUserInfo is null");
        }

        return processOAuth2User("kakao", kakaoUserInfo);
    }

    private CustomUserDetails processOAuth2User(String provider, Oauth2UserInfo oauth2UserInfo) {
        String providerId = String.valueOf(oauth2UserInfo.getProviderId());
        String username = provider + "_" + providerId;

        UserAccountDto userDto = userAccountService.searchUserOrSave(username, oauth2UserInfo);
        return new CustomUserDetails(userDto);
    }
}
