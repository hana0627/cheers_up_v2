package com.hana.cheers_up.api

import android.content.Context

class TokenManager(private val context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private companion object {
        const val PREFS_NAME = "app_prefs"
        const val JWT_TOKEN_KEY = "jwt_token"
//        const val KAKAO_TOKEN_KEY = "kakao_token"
    }


    fun saveJwtToken(token: String) {
        prefs.edit().putString(JWT_TOKEN_KEY, token).apply()
    }

    fun getJwtToken(): String? {
        return prefs.getString(JWT_TOKEN_KEY, null)
    }

    fun clearToken() {
        prefs.edit().remove(JWT_TOKEN_KEY).apply()
    }

//    fun getKakaoToken(): String? {
//        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//        return sharedPref.getString(KAKAO_TOKEN_KEY, null)
//    }
//
//    suspend fun refreshJwtToken(): String? {
//        return try {
//            // 저장된 카카오 토큰으로 JWT 재발급
//            val kakaoToken = getKakaoToken()
//            if (kakaoToken != null) {
//                val response = apiService.getJwtToken(kakaoToken)
//                if (response.isSuccessful) {
//                    response.body()?.let { apiResponse ->
//                        if (apiResponse.resultCode == "OK" && apiResponse.result != null) {
//                            saveJwtToken(apiResponse.result)
//                            return apiResponse.result
//                        }
//                    }
//                }
//            }
//            null
//        } catch (e: Exception) {
//            Log.e("TokenManager", "JWT 토큰 재발급 실패", e)
//            null
//        }
//    }


}