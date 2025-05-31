package com.hana.cheers_up.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    // 카카오 토큰으로 JWT 토큰 발급
    @POST("/auth/kakao")
    suspend fun getJwtToken(@Body request: KakaoTokenRequest): Response<JwtTokenResponse>

    // 주소 검색 API (JWT 토큰 필요)
    @POST("/search/address")
    suspend fun searchByAddress(
        @Header("Authorization") token: String,
        @Body request: String
    ): Response<SearchResponse>
}

data class KakaoTokenRequest(val accessToken: String)

data class JwtTokenResponse(val jwtToken: String)

data class AddressSearchRequest(val address: String)

data class SearchResponse(val results: List<Any>) // 실제 응답 구조에 맞게 수정