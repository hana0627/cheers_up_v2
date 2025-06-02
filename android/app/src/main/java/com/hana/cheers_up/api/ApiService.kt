package com.hana.cheers_up.api

import com.hana.cheers_up.api.response.APIResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    // 카카오 토큰으로 JWT 토큰 발급
    @POST("/api/v1/auth/kakao/mobile")
    suspend fun getJwtToken(@Body request: String): Response<APIResponse<String>>

    // 주소 검색 API (JWT 토큰 필요)
    @POST("/search/address")
    suspend fun searchByAddress(
        @Header("Authorization") token: String,
        @Body request: String
    ): Response<Any>
}
