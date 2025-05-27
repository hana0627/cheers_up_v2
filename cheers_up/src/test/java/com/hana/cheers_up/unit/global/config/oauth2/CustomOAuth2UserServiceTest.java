package com.hana.cheers_up.unit.global.config.oauth2;

import com.hana.cheers_up.application.user.domain.constant.RoleType;
import com.hana.cheers_up.application.user.dto.UserAccountDto;
import com.hana.cheers_up.application.user.service.UserAccountService;
import com.hana.cheers_up.global.config.CustomUserDetails;
import com.hana.cheers_up.global.config.oauth2.CustomOAuth2UserService;
import com.hana.cheers_up.global.config.oauth2.provider.impl.KakaoUserInfo;
import com.hana.cheers_up.global.config.oauth2.userloader.OAuth2UserLoader;
import com.hana.cheers_up.global.config.oauth2.userloader.impl.DefaultOAuth2UserLoader;
import com.hana.cheers_up.global.exception.ApplicationException;
import com.hana.cheers_up.global.exception.constant.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class CustomOAuth2UserServiceTest {

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    @Mock
    private UserAccountService userAccountService;

    @Mock
    private OAuth2UserLoader oAuth2UserLoader;

    @Mock
    private OAuth2UserRequest userRequest;

    @Mock
    private OAuth2User oAuth2User;

    private UserAccountDto userAccountDto;
    private ClientRegistration kakaoRegistration;
    private ClientRegistration googleRegistration;
    private Map<String, Object> attributes;


    @Mock
    private DefaultOAuth2UserService defaultOAuth2UserService;

    @BeforeEach
    void setUp() {
        userAccountDto = UserAccountDto.of("kakao_1234567890", "hanana9506@naver.com", "공주하나", "신세경닮음", RoleType.USER);

        kakaoRegistration = ClientRegistration.withRegistrationId("kakao")
                .clientId("client-id")
                .clientSecret("client-secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8080/api/v1/login/oauth2/code/kakao")
                .scope("profile", "email")
                .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                .tokenUri("https://kauth.kakao.com/oauth/token")
                .userInfoUri("https://kapi.kakao.com/v2/user/me")
                .userNameAttributeName("id")
                .build();

        attributes = new HashMap<>();
        attributes.put("id", 1234567890);
        attributes.put("connected_at", "2023-04-16T11:17:49Z");

        Map<String, Object> properties = new HashMap<>();
        properties.put("nickname", "하나");
        attributes.put("properties", properties);

        Map<String, Object> kakaoAccount = new HashMap<>();
        kakaoAccount.put("profile_nickname_needs_agreement", false);

        Map<String, Object> profile = new HashMap<>();
        profile.put("nickname", "하나");
        profile.put("is_default_nickname", false);
        kakaoAccount.put("profile", profile);

        kakaoAccount.put("has_email", true);
        kakaoAccount.put("email_needs_agreement", false);
        kakaoAccount.put("is_email_valid", true);
        kakaoAccount.put("is_email_verified", true);
        kakaoAccount.put("email", "shamoo1@naver.com");

        attributes.put("kakao_account", kakaoAccount);



        googleRegistration = ClientRegistration.withRegistrationId("google")
                .clientId("client-id")
                .clientSecret("client-secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8080/api/v1/login/oauth2/code/google")
                .scope("profile", "email")
                .authorizationUri("https://auth.google.com/oauth/authorize")
                .tokenUri("https://auth.google.com/oauth/token")
                .userInfoUri("https://auth.google.com/v2/user/me")
                .userNameAttributeName("id")
                .build();

    }

    @Test
    void 카카오로그인_성공시_userDatails를_반환한다() {
        //given
        given(userRequest.getClientRegistration()).willReturn(kakaoRegistration);
        given(oAuth2UserLoader.loadUser(userRequest)).willReturn(oAuth2User);
        given(oAuth2User.getAttributes()).willReturn(attributes);
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);

        given(userAccountService.searchUserOrSave(userAccountDto.userId(), kakaoUserInfo)).willReturn(userAccountDto);

        //when
        CustomUserDetails result = customOAuth2UserService.loadUser(userRequest);

        //then
        then(userRequest).should().getClientRegistration();
        then(oAuth2UserLoader).should().loadUser(userRequest);
        then(oAuth2User).should().getAttributes();
        then(userAccountService).should().searchUserOrSave(userAccountDto.userId(), kakaoUserInfo);


        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("kakao_1234567890");
    }



    @Test
    void 유저정보없이_소셜로그인시_예외가_발생한다() {
        //given
        userRequest = null;

        //when
        ApplicationException result = assertThrows(ApplicationException.class, () -> customOAuth2UserService.loadUser(userRequest));

        //then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.NULL_USER_REQUEST);
        assertThat(result.getMessage()).contains("userRequest is null");
    }


    @Test
    void 구글로그인_시도시_예외가_발생한다() {
        //given
        given(userRequest.getClientRegistration()).willReturn(googleRegistration);
        given(oAuth2UserLoader.loadUser(userRequest)).willReturn(oAuth2User);

        //when
        ApplicationException result = assertThrows(ApplicationException.class, () -> customOAuth2UserService.loadUser(userRequest));

        //then
        then(userRequest).should().getClientRegistration();
        then(oAuth2UserLoader).should().loadUser(userRequest);
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.UNSUPPORTED_LOGIN_TYPE);
        assertThat(result.getMessage()).contains("지원하지 않는 로그인 타입입니다.");
    }


    @Test
    void defaultOAuth2UserService를_통해서_loaduser메서드가_실행된다() {
        //given
        DefaultOAuth2UserLoader defaultOAuth2UserLoader = new DefaultOAuth2UserLoader(defaultOAuth2UserService);

        OAuth2UserRequest userRequest = mock(OAuth2UserRequest.class);
        OAuth2User oauth2User = mock(OAuth2User.class);

        given(defaultOAuth2UserService.loadUser(userRequest)).willReturn(oauth2User);

        //when
        OAuth2User result = defaultOAuth2UserLoader.loadUser(userRequest);

        //then
        then(defaultOAuth2UserService).should().loadUser(userRequest);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(oauth2User);
    }
    @Test
    void defaultOAuth2UserLoader_기본생성자로_생성시_DefaultOAuth2UserService를_사용한다() {
        // given
        DefaultOAuth2UserLoader defaultOAuth2UserLoader = new DefaultOAuth2UserLoader(); // 기본 생성자 사용

        // when && then
        assertThat(defaultOAuth2UserLoader).isNotNull();

    }

}

