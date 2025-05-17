package com.hana.cheers_up.unit.application.user.service;

import com.hana.cheers_up.application.user.domain.UserAccount;
import com.hana.cheers_up.application.user.domain.constant.RoleType;
import com.hana.cheers_up.application.user.dto.UserAccountDto;
import com.hana.cheers_up.application.user.repository.UserAccountRepository;
import com.hana.cheers_up.application.user.service.UserAccountService;
import com.hana.cheers_up.global.config.jwt.JwtUtils;
import com.hana.cheers_up.global.config.oauth2.provider.impl.KakaoUserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private UserAccountService userAccountService;



    private Map<String, Object> attributes;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    void 로그인에_성공하면_jwt_토큰을_반환한다() {
        //given
        String testToken = "thisistesttoken";

        UserAccountDto userAccountDto = UserAccountDto.of("kakao_1234567890", "hanana9506@naver.com", "공주하나", "신세경닮음", RoleType.USER);
        UserAccount userAccount = userAccountDto.toEntity();
        given(userAccountRepository.findById(userAccountDto.userId())).willReturn(Optional.of(userAccount));
        given(jwtUtils.generateToken(userAccount.getUserId(),
                userAccount.getNickname(),
                userAccount.getEmail(),
                userAccount.getRoleType())).willReturn(testToken);

        //when
        String result = userAccountService.login(userAccountDto);

        //then
        then(userAccountRepository).should().findById(userAccountDto.userId());
        then(jwtUtils).should().generateToken(userAccount.getUserId(),
                userAccount.getNickname(),
                userAccount.getEmail(),
                userAccount.getRoleType());

        assertThat(result).isEqualTo(testToken);

    }



    @Test
    void username으로_조회시_값이있다면_해당값을_반환한다() {
        //given
        UserAccount userAccount = UserAccount.of("kakao_1234567890", "hanana9506@naver.com", "공주하나", "신세경닮음", RoleType.USER);
        String username = "kakao_1234567890";

        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);

        given(userAccountRepository.findById(username)).willReturn(Optional.of(userAccount));


        //when
        UserAccountDto result = userAccountService.searchUserOrSave(username, kakaoUserInfo);

        //then
        then(userAccountRepository).should().findById(username);
        then(userAccountRepository).should(times(0)).save(userAccount);
        assertThat(result.userId()).isEqualTo(userAccount.getUserId());
        assertThat(result.email()).isEqualTo(userAccount.getEmail());
        assertThat(result.nickname()).isEqualTo(userAccount.getNickname());
        assertThat(result.memo()).isEqualTo(userAccount.getMemo());
        assertThat(result.roleType()).isEqualTo(userAccount.getRoleType());

    }

    @Test
    void username으로_조회시_값이_없다면_회원정보를_저장한다() {
        //given
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);

        UserAccount userAccount = UserAccount.of("kakao_1234567890", kakaoUserInfo.getEmail(), kakaoUserInfo.getNickname(), null, RoleType.USER);
        String username = "kakao_1234567890";

        given(userAccountRepository.findById(username)).willReturn(Optional.empty());
        given(userAccountRepository.save(userAccount)).willReturn(userAccount);

        //when
        UserAccountDto result = userAccountService.searchUserOrSave(username, kakaoUserInfo);

        //then
        then(userAccountRepository).should().findById(username);
        then(userAccountRepository).should().save(userAccount);
        assertThat(result.userId()).isEqualTo(userAccount.getUserId());
        assertThat(result.email()).isEqualTo(userAccount.getEmail());
        assertThat(result.nickname()).isEqualTo(userAccount.getNickname());
        assertThat(result.memo()).isEqualTo(userAccount.getMemo());
        assertThat(result.roleType()).isEqualTo(userAccount.getRoleType());
        assertThat(result.roleType().getRoleName()).isEqualTo(userAccount.getRoleType().getRoleName());
        assertThat(result.roleType().getDescription()).isEqualTo(userAccount.getRoleType().getDescription());

    }
}


//    @Test
//    void 회원가입에_성공한다() {
//        //given
//        UserAccountDto userAccountDto = UserAccountDto.of("kakao_1234567890","hanana9506@naver.com","공주하나","신세경닮음", RoleType.USER);
//        UserAccount userAccount = userAccountDto.toEntity();
//
//        given(userAccountRepository.save(userAccount)).willReturn(userAccount);
//
//        //when
//        userAccountService.saveUser(userAccountDto);
//
//
//        //then
//        then(userAccountRepository).should().save(userAccount);
//
//    }

//
//    @Test
//    void username으로_회원_조회에_성공한다() {
//        //given
//        UserAccount userAccount = UserAccount.of("kakao_1234567890","hanana9506@naver.com","공주하나","신세경닮음", RoleType.USER);
//        String username = "kakao_1234567890";
//
//        given(userAccountRepository.findById(username)).willReturn(Optional.of(userAccount));
//
//        //when
//        UserAccountDto result = userAccountService.searchUser(username);
//
//        //then
//        then(userAccountRepository).should().findById(username);
//        assertThat(result.userId()).isEqualTo(userAccount.getUserId());
//        assertThat(result.email()).isEqualTo(userAccount.getEmail());
//        assertThat(result.nickname()).isEqualTo(userAccount.getNickname());
//        assertThat(result.memo()).isEqualTo(userAccount.getMemo());
//        assertThat(result.roleType()).isEqualTo(userAccount.getRoleType());
//    }
//
//    @Test
//    void 존재하지_username으로_회원_조회시_예외가_발생한다() {
//        //given
//        String username = "wrong-user";
//
//        given(userAccountRepository.findById(username)).willReturn(Optional.empty());
//
//        //when
//        UsernameNotFoundException result = assertThrows(UsernameNotFoundException.class, () -> userAccountService.searchUser(username));
//
//        //then
//        then(userAccountRepository).should().findById(username);
//        assertThat(result.getMessage()).contains("회원을 찾을 수 없습니다");
//        assertThat(result.getMessage()).isEqualTo("회원을 찾을 수 없습니다 - username : " + username);
//    }

