package com.hana.cheers_up.unit.global.config;

import com.hana.cheers_up.application.user.domain.constant.RoleType;
import com.hana.cheers_up.application.user.dto.UserAccountDto;
import com.hana.cheers_up.global.config.CustomUserDetails;
import com.hana.cheers_up.global.config.JpaConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JpaConfigTest {


    @InjectMocks
    private JpaConfig jpaConfig;

    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }




    @Test
    void 인증된_사용자가_있을때_사용자_이름을_반환한다() throws Exception{
        //given
        String username = "kakao_1234567890";
        UserAccountDto userAccountDto = UserAccountDto.of("kakao_1234567890", "hanana9506@naver.com", "공주하나", "신세경닮음", RoleType.USER);

        userDetails = new CustomUserDetails(userAccountDto);

        Authentication authentication = new TestingAuthenticationToken(userDetails, null, "USER");
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        //when
        AuditorAware<String> auditorAware = jpaConfig.auditorAware();
        Optional<String> result = auditorAware.getCurrentAuditor();

        //then
        assertThat(result).isNotNull();
        assertThat(result.get()).isEqualTo(username);

    }
}