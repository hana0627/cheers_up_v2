package com.hana.cheers_up.api

import com.hana.cheers_up.api.response.APIResponse
import com.hana.cheers_up.api.response.PubResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    // 카카오 토큰으로 JWT 토큰 발급
    @POST("/api/v1/auth/kakao/mobile")
    suspend fun getJwtToken(@Body request: String): Response<APIResponse<String>>

    // 주소 검색 API (JWT 토큰 필요)
    @GET("api/v2/search")
    suspend fun searchPubs(
        @Header("Authorization") authorization: String,  // JWT 토큰
        @Query("address") address: String               // 검색할 주소
    ): Response<APIResponse<List<PubResponse>>>
}
