package com.hana.cheers_up.global.api.controller

import com.hana.cheers_up.global.config.CustomUserDetails
import com.hana.cheers_up.global.config.jwt.JwtUtils
import com.hana.cheers_up.global.config.oauth2.CustomOAuth2UserService
import com.hana.cheers_up.global.config.oauth2.provider.impl.KakaoUserInfo
import com.hana.cheers_up.global.exception.ApplicationException
import com.hana.cheers_up.global.exception.constant.ErrorCode
import com.hana.cheers_up.global.response.APIResponse
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@RestController
class AuthController(
    private val restTemplate: RestTemplate,
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val jwtUtils: JwtUtils,
) {

    @PostMapping("/api/v1/auth/kakao/mobile")
    fun kakaoLogin(@RequestBody kakaoToken: String): APIResponse<String> {
        try {
            val oauth2KakaoUserInfo = getKakaoUserInfo(kakaoToken)
            val userDetails: CustomUserDetails = customOAuth2UserService.processKakaoUser(oauth2KakaoUserInfo);

            val jwtToken = jwtUtils.generateToken(
                userDetails.name,
                userDetails.nickname ?: userDetails.name,
                userDetails.email,
                userDetails.roleType
            )
            return APIResponse.success(jwtToken)
        } catch (e: HttpClientErrorException) {
            println("카카오 API 호출 실패: ${e.message}")
            throw ApplicationException(ErrorCode.KAKAO_API_ERROR, ErrorCode.KAKAO_API_ERROR.message)
        } catch (e: Exception) {
            println("카카오 API 호출 실패: ${e.message}")
            throw ApplicationException(ErrorCode.KAKAO_API_ERROR, ErrorCode.KAKAO_API_ERROR.message)
        }
    }

    private fun getKakaoUserInfo(kakaoToken: String): KakaoUserInfo {
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $kakaoToken")
        headers.contentType = MediaType.APPLICATION_JSON

        val entity = HttpEntity("", headers)

        val kakaoResponse: ResponseEntity<Map<String, Any>> = restTemplate.exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.GET,
            entity,
            object : ParameterizedTypeReference<Map<String, Any>>() {}
        )

        val attributes = kakaoResponse.body ?: throw ApplicationException(
            ErrorCode.KAKAO_RESPONSE_EMPTY,
            ErrorCode.KAKAO_RESPONSE_EMPTY.message
        )

        return KakaoUserInfo(attributes)
    }
}
