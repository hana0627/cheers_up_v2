package com.hana.cheers_up.unit.global.config.oauth2;

import com.hana.cheers_up.application.user.domain.constant.RoleType;
import com.hana.cheers_up.application.user.dto.UserAccountDto;
import com.hana.cheers_up.global.config.CustomUserDetails;
import com.hana.cheers_up.global.config.jwt.JwtUtils;
import com.hana.cheers_up.global.config.oauth2.CustomOAuth2SuccessHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class CustomOAuth2SuccessHandlerTest {
    @InjectMocks
    private CustomOAuth2SuccessHandler oAuth2SuccessHandler;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private Authentication authentication;

    private CustomUserDetails userDetails;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @BeforeEach
    void setUp() {
        UserAccountDto userAccountDto = UserAccountDto.of("kakao_1234567890","hanana9506@naver.com","공주하나","신세경닮음", RoleType.USER);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();


        userDetails = mock(CustomUserDetails.class);
        given(userDetails.getName()).willReturn(userAccountDto.userId());
        given(userDetails.getNickname()).willReturn(userAccountDto.nickname());
        given(userDetails.getEmail()).willReturn(userAccountDto.email());
        given(userDetails.getRoleType()).willReturn(userAccountDto.roleType());

        // Authentication 객체 설정
        given(authentication.getPrincipal()).willReturn(userDetails);
    }


    @Test
    void Oauth2로그인에_성공하면_토큰을_발급하고_redirect가_일어난다() throws Exception {
        //given
        String testToken = "thisistesttoken";

        given(jwtUtils.generateToken(userDetails.getName(), userDetails.getNickname(), userDetails.getEmail(), userDetails.getRoleType()))
                .willReturn(testToken);

        //when
        oAuth2SuccessHandler.onAuthenticationSuccess(request,response, authentication);

        //then
        then(jwtUtils).should().generateToken(userDetails.getName(), userDetails.getNickname(), userDetails.getEmail(), userDetails.getRoleType());

        assertThat(response.getStatus()).isEqualTo(302);
        assertThat(response.getRedirectedUrl()).isEqualTo(frontendUrl+"?token=" + testToken);


    }
}


/*
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

import jakarta.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomOAuth2SuccessHandlerTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CustomOAuth2SuccessHandler successHandler;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private CustomUserDetails userDetails;


    @Test
    void onAuthenticationSuccess_ShouldGenerateTokenAndRedirect() throws ServletException, IOException {
        // given
        String expectedToken = "test.jwt.token";
        when(jwtUtils.generateToken(
                userDetails.getName(),
                userDetails.getNickname(),
                userDetails.getEmail(),
                userDetails.getRoleType()
        )).thenReturn(expectedToken);

        // when
        successHandler.onAuthenticationSuccess(request, response, authentication);

        // then
        verify(jwtUtils).generateToken(
                userDetails.getName(),
                userDetails.getNickname(),
                userDetails.getEmail(),
                userDetails.getRoleType()
        );

        // 리디렉션 검증
        assertEquals(302, response.getStatus());
        assertEquals("/api/v1/users/login?token=" + expectedToken, response.getRedirectedUrl());
    }

    @Test
    void onAuthenticationSuccess_WithDifferentUserDetails_ShouldGenerateCorrectToken() throws ServletException, IOException {
        // given
        // 다른 사용자 정보로 설정
        when(userDetails.getName()).thenReturn("anotherUser");
        when(userDetails.getNickname()).thenReturn("다른유저");
        when(userDetails.getEmail()).thenReturn("another@example.com");
        when(userDetails.getRoleType()).thenReturn(RoleType.ADMIN);

        String expectedToken = "another.jwt.token";
        when(jwtUtils.generateToken(
                userDetails.getName(),
                userDetails.getNickname(),
                userDetails.getEmail(),
                userDetails.getRoleType()
        )).thenReturn(expectedToken);

        // when
        successHandler.onAuthenticationSuccess(request, response, authentication);

        // then
        verify(jwtUtils).generateToken(
                "anotherUser",
                "다른유저",
                "another@example.com",
                RoleType.ADMIN
        );

        assertEquals("/api/v1/users/login?token=" + expectedToken, response.getRedirectedUrl());
    }

    @Test
    void onAuthenticationSuccess_WhenPrincipalIsNotCustomUserDetails_ShouldHandleClassCastException() {
        // given
        // 다른 타입의 Principal 설정
        when(authentication.getPrincipal()).thenReturn(new Object());

        // when & then
        try {
            successHandler.onAuthenticationSuccess(request, response, authentication);
        } catch (Exception e) {
            assertEquals(ClassCastException.class, e.getClass());
        }
    }
}
 */