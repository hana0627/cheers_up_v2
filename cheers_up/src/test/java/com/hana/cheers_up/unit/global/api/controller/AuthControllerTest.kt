package com.hana.cheers_up.unit.global.api.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.hana.cheers_up.application.pub.infrastructure.kakao.dto.KakaoResponse
import com.hana.cheers_up.application.user.domain.constant.RoleType
import com.hana.cheers_up.application.user.dto.UserAccountDto
import com.hana.cheers_up.global.api.controller.AuthController
import com.hana.cheers_up.global.config.CustomUserDetails
import com.hana.cheers_up.global.config.jwt.JwtUtils
import com.hana.cheers_up.global.config.oauth2.CustomOAuth2UserService
import com.hana.cheers_up.global.config.oauth2.provider.impl.KakaoUserInfo
import com.hana.cheers_up.global.exception.constant.ErrorCode
import com.hana.cheers_up.mock.TestSecurityConfig
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@WebMvcTest(AuthController::class)
@Import(TestSecurityConfig::class)
class AuthControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var om: ObjectMapper

    @MockitoBean
    private lateinit var restTemplate: RestTemplate

    @MockitoBean
    private lateinit var customOAuth2UserService: CustomOAuth2UserService

    @MockitoBean
    private lateinit var jwtUtils: JwtUtils

    @Test
    fun 안드로이드로_카카오로그인이_성공한다_닉네임정보_있는경우() {
        //given
        val clientToken = "Bearer thisistesttoken"

        val json = om.writeValueAsString(clientToken)
        val serverToken = "Bearer thisisnewtesttoken"

        val userAccountDto = UserAccountDto.of("kakao_1234567890", "hanana9506@naver.com", "공주하나", "신세경닮음", RoleType.USER);
        val userDetails = CustomUserDetails(userAccountDto)

        val kakaoResponse = mapOf(
            "id" to 1234567890L,
            "kakao_account" to mapOf(
                "email" to "hanana9506@naver.com",
                "profile" to mapOf(
                    "nickname" to "공주하나"
                )
            )
        )

        given(restTemplate.exchange(
            eq("https://kapi.kakao.com/v2/user/me"),
            eq(HttpMethod.GET),
            any(HttpEntity::class.java),
            any(ParameterizedTypeReference::class.java)
        )).willReturn(ResponseEntity.ok(kakaoResponse))

        val kakaoUserInfo = KakaoUserInfo(kakaoResponse)

        given(customOAuth2UserService.processKakaoUser(kakaoUserInfo)).willReturn(userDetails)

        given(jwtUtils.generateToken(userDetails.name, userDetails.nickname ?: userDetails.name, userDetails.email, userDetails.roleType))
            .willReturn(serverToken)

        //when & then
        mvc.perform(
            post("/api/v1/auth/kakao/mobile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.resultCode").value(HttpStatus.OK.name))
            .andExpect(jsonPath("$.result").value(serverToken))
            .andDo(print())


        then(restTemplate).should().exchange(
            eq("https://kapi.kakao.com/v2/user/me"),
            eq(HttpMethod.GET),
            any(HttpEntity::class.java),
            any(ParameterizedTypeReference::class.java))

        then(customOAuth2UserService).should().processKakaoUser(kakaoUserInfo)
        then(jwtUtils).should().generateToken(userDetails.name, userDetails.nickname ?: userDetails.name, userDetails.email, userDetails.roleType)

    }

    @Test
    fun 안드로이드로_카카오로그인이_성공한다_닉네임정보_없는경우() {
        //given
        val clientToken = "Bearer thisistesttoken"

        val json = om.writeValueAsString(clientToken)
        val serverToken = "Bearer thisisnewtesttoken"

        val userAccountDto = UserAccountDto.of("kakao_1234567890", "hanana9506@naver.com", null, "신세경닮음", RoleType.USER);
        val userDetails = CustomUserDetails(userAccountDto)

        val kakaoResponse = mapOf(
            "id" to 1234567890L,
            "kakao_account" to mapOf(
                "email" to "hanana9506@naver.com",
            )
        )

        given(restTemplate.exchange(
            eq("https://kapi.kakao.com/v2/user/me"),
            eq(HttpMethod.GET),
            any(HttpEntity::class.java),
            any(ParameterizedTypeReference::class.java)
        )).willReturn(ResponseEntity.ok(kakaoResponse))

        val kakaoUserInfo = KakaoUserInfo(kakaoResponse)

        given(customOAuth2UserService.processKakaoUser(kakaoUserInfo)).willReturn(userDetails)

        given(jwtUtils.generateToken(userDetails.name, userDetails.nickname ?: userDetails.name, userDetails.email, userDetails.roleType))
            .willReturn(serverToken)

        //when & then
        mvc.perform(
            post("/api/v1/auth/kakao/mobile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.resultCode").value(HttpStatus.OK.name))
            .andExpect(jsonPath("$.result").value(serverToken))
            .andDo(print())


        then(restTemplate).should().exchange(
            eq("https://kapi.kakao.com/v2/user/me"),
            eq(HttpMethod.GET),
            any(HttpEntity::class.java),
            any(ParameterizedTypeReference::class.java))

        then(customOAuth2UserService).should().processKakaoUser(kakaoUserInfo)
        then(jwtUtils).should().generateToken(userDetails.name, userDetails.nickname ?: userDetails.name, userDetails.email, userDetails.roleType)

    }


    @Test
    fun 카카오_내정보찾기_결과가_없으면_예외가_발생한다() {
        //given
        val clientToken = "Bearer thisistesttoken"

        val json = om.writeValueAsString(clientToken)

        given(restTemplate.exchange(
            eq("https://kapi.kakao.com/v2/user/me"),
            eq(HttpMethod.GET),
            any(HttpEntity::class.java),
            any(ParameterizedTypeReference::class.java)
        )).willReturn(ResponseEntity.ok(null))

        //when & then
        mvc.perform(
            post("/api/v1/auth/kakao/mobile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().is5xxServerError)
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.KAKAO_API_ERROR.status.name))
            .andExpect(jsonPath("$.result").value(ErrorCode.KAKAO_API_ERROR.message))
            .andDo(print())


        then(restTemplate).should().exchange(
            eq("https://kapi.kakao.com/v2/user/me"),
            eq(HttpMethod.GET),
            any(HttpEntity::class.java),
            any(ParameterizedTypeReference::class.java))
    }

    @Test
    fun 카카오_내정보찾기시도중_예상하지못한_예외가_발생할_수_있다() {
        //given
        val clientToken = "Bearer thisistesttoken"

        val json = om.writeValueAsString(clientToken)

        given(restTemplate.exchange(
            eq("https://kapi.kakao.com/v2/user/me"),
            eq(HttpMethod.GET),
            any(HttpEntity::class.java),
            any(ParameterizedTypeReference::class.java)
        )).willThrow(HttpClientErrorException(HttpStatus.UNAUTHORIZED))

        //when & then
        mvc.perform(
            post("/api/v1/auth/kakao/mobile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().is5xxServerError)
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.KAKAO_API_ERROR.status.name))
            .andExpect(jsonPath("$.result").value(ErrorCode.KAKAO_API_ERROR.message))
            .andDo(print())


        then(restTemplate).should().exchange(
            eq("https://kapi.kakao.com/v2/user/me"),
            eq(HttpMethod.GET),
            any(HttpEntity::class.java),
            any(ParameterizedTypeReference::class.java))
    }



}
