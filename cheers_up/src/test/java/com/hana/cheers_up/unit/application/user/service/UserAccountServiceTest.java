package com.hana.cheers_up.unit.application.user.service;

import com.hana.cheers_up.application.user.domain.UserAccount;
import com.hana.cheers_up.application.user.domain.constant.RoleType;
import com.hana.cheers_up.application.user.dto.UserAccountDto;
import com.hana.cheers_up.application.user.repository.UserAccountRepository;
import com.hana.cheers_up.application.user.service.UserAccountService;
import com.hana.cheers_up.global.security.KakaoOAuth2Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private UserAccountService userAccountService;

    @Test
    void 회원가입에_성공한다() {
        //given
        UserAccountDto userAccountDto = UserAccountDto.of("kakako-hana","hanana9506@naver.com","공주하나","신세경닮음", RoleType.USER);
        UserAccount userAccount = userAccountDto.toEntity();

        given(userAccountRepository.save(userAccount)).willReturn(userAccount);

        //when
        userAccountService.saveUser(userAccountDto);


        //then
        then(userAccountRepository).should().save(userAccount);

    }

    @Test
    void username으로_회원_조회에_성공한다() {
        //given
        UserAccount userAccount = UserAccount.of("kakako-hana","hanana9506@naver.com","공주하나","신세경닮음", RoleType.USER);
        String username = "kakako-hana";

        given(userAccountRepository.findById(username)).willReturn(Optional.of(userAccount));

        //when
        UserAccountDto result = userAccountService.searchUser(username);

        //then
        then(userAccountRepository).should().findById(username);
        assertThat(result.userId()).isEqualTo(userAccount.getUserId());
        assertThat(result.email()).isEqualTo(userAccount.getEmail());
        assertThat(result.nickname()).isEqualTo(userAccount.getNickname());
        assertThat(result.memo()).isEqualTo(userAccount.getMemo());
        assertThat(result.roleType()).isEqualTo(userAccount.getRoleType());
    }

    @Test
    void 존재하지_username으로_회원_조회시_예외가_발생한다() {
        //given
        String username = "wrong-user";

        given(userAccountRepository.findById(username)).willReturn(Optional.empty());

        //when
        UsernameNotFoundException result = assertThrows(UsernameNotFoundException.class, () -> userAccountService.searchUser(username));

        //then
        then(userAccountRepository).should().findById(username);
        assertThat(result.getMessage()).contains("회원을 찾을 수 없습니다");
        assertThat(result.getMessage()).isEqualTo("회원을 찾을 수 없습니다 - username : " + username);
    }


    @Test
    void username으로_조회시_값이있다면_해당값을_반환한다() {
        //given
        KakaoOAuth2Response.KakaoAccount.Profile profile = new KakaoOAuth2Response.KakaoAccount.Profile("공주하나");
        KakaoOAuth2Response.KakaoAccount kakaoAccount = new KakaoOAuth2Response.KakaoAccount(
                false, // profileNicknameNeedsAgreement
                profile,
                true,  // hasEmail
                false, // emailNeedsAgreement
                true,  // isEmailValid
                true,  // isEmailVerified
                "hanana9506@naver.com"
        );

        KakaoOAuth2Response kakaoOAuth2Response = new KakaoOAuth2Response(
                123456L,
                LocalDateTime.now(),
                Map.of("nickname", "공주하나"),
                kakaoAccount
        );


        UserAccount userAccount = UserAccount.of("kakako-hana", "hanana9506@naver.com", "공주하나", "신세경닮음", RoleType.USER);
        String username = "kakako-hana";

        given(userAccountRepository.findById(username)).willReturn(Optional.of(userAccount));

        //when
        UserAccountDto result = userAccountService.searchUserOrSave(username, kakaoOAuth2Response);

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
        KakaoOAuth2Response.KakaoAccount.Profile profile = new KakaoOAuth2Response.KakaoAccount.Profile("공주하나");
        KakaoOAuth2Response.KakaoAccount kakaoAccount = new KakaoOAuth2Response.KakaoAccount(
                false, // profileNicknameNeedsAgreement
                profile,
                true,  // hasEmail
                false, // emailNeedsAgreement
                true,  // isEmailValid
                true,  // isEmailVerified
                "hanana9506@naver.com"
        );

        KakaoOAuth2Response kakaoOAuth2Response = new KakaoOAuth2Response(
                123456L,
                LocalDateTime.now(),
                Map.of("nickname", "공주하나"),
                kakaoAccount
        );


        UserAccount userAccount = UserAccount.of("kakako-hana", kakaoOAuth2Response.email(), kakaoOAuth2Response.nickname(), null, RoleType.USER);
        String username = "kakako-hana";

        given(userAccountRepository.findById(username)).willReturn(Optional.empty());
        given(userAccountRepository.save(userAccount)).willReturn(userAccount);

        //when
        UserAccountDto result = userAccountService.searchUserOrSave(username, kakaoOAuth2Response);

        //then
        then(userAccountRepository).should().findById(username);
        then(userAccountRepository).should().save(userAccount);
        assertThat(result.userId()).isEqualTo(userAccount.getUserId());
        assertThat(result.email()).isEqualTo(userAccount.getEmail());
        assertThat(result.nickname()).isEqualTo(userAccount.getNickname());
        assertThat(result.memo()).isEqualTo(userAccount.getMemo());
        assertThat(result.roleType()).isEqualTo(userAccount.getRoleType());

    }
}
