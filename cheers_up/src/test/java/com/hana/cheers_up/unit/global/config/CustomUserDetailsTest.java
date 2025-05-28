package com.hana.cheers_up.unit.global.config;

import com.hana.cheers_up.application.user.domain.constant.RoleType;
import com.hana.cheers_up.application.user.dto.UserAccountDto;
import com.hana.cheers_up.global.config.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsTest {

    @InjectMocks
    private CustomUserDetails userDetails;
    @MockitoBean
    private UserAccountDto userAccountDto;

    @BeforeEach
    void setUp() {
        userAccountDto = UserAccountDto.of("kakao_1234567890", "hanana9506@naver.com", "공주하나", "신세경닮음", RoleType.USER);
        userDetails = new CustomUserDetails(userAccountDto);
    };

    @Test
    @DisplayName("getAttributes()호출시_emptyMap()을_반환한다")
    void getAttributes호출시_emptyMap을_반환한다() {
        //given

        // nothing

        //when
        Map<String, Object> result = userDetails.getAttributes();

        //then
        assertThat(result).isEmpty();
        assertThat(result).isEqualTo(Map.of());
    }

    @Test
    @DisplayName("getAuthorities()호출시_emptyList()을_반환한다")
    void getAuthorities호출시_emptyList를_반환한다() {
        //given

        // nothing

        //when
        Collection<? extends GrantedAuthority> result = userDetails.getAuthorities();

        //then
        assertThat(result).isEmpty();
        assertThat(result).isInstanceOf(List.class);
        assertThat(result).isEqualTo(List.of());
    }

    @Test
    @DisplayName("getNam()호출시_userId를_반환한다")
    void getName호출시_userId를_반환한다() {
        //given
        userAccountDto = UserAccountDto.of("kakao_1234567890", "hanana9506@naver.com", "공주하나", "신세경닮음", RoleType.USER);
        // nothing

        //when
        String result = userDetails.getName();

        //then
        assertThat(result).isEqualTo(userAccountDto.userId());
    }

    @Test
    @DisplayName("getNickname()호출시_nickname을_반환한다")
    void getNickname호출시_nickname을_반환한다() {
        //given
        userAccountDto = UserAccountDto.of("kakao_1234567890", "hanana9506@naver.com", "공주하나", "신세경닮음", RoleType.USER);
        // nothing

        //when
        String result = userDetails.getNickname();

        //then
        assertThat(result).isEqualTo(userAccountDto.nickname());
    }

    @Test
    @DisplayName("getEmail()호출시_email을_반환한다")
    void getEmail호출시_email을_반환한다() {
        //given
        userAccountDto = UserAccountDto.of("kakao_1234567890", "hanana9506@naver.com", "공주하나", "신세경닮음", RoleType.USER);
        // nothing

        //when
        String result = userDetails.getEmail();

        //then
        assertThat(result).isEqualTo(userAccountDto.email());
    }

    @Test
    @DisplayName("getRoleType()호출시_roleType을_반환한다")
    void getRoleType호출시_roleType을_반환한다() {
        //given
        userAccountDto = UserAccountDto.of("kakao_1234567890", "hanana9506@naver.com", "공주하나", "신세경닮음", RoleType.USER);
        // nothing

        //when
        RoleType result = userDetails.getRoleType();

        //then
        assertThat(result).isEqualTo(userAccountDto.roleType());
    }

}
