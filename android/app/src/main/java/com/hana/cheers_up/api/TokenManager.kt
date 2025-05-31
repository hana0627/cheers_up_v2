package com.hana.cheers_up.api

import android.content.Context

class TokenManager(private val context: Context) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun saveJwtToken(token: String) {
        prefs.edit().putString("jwt_token", token).apply()
    }

    fun getJwtToken(): String? {
        return prefs.getString("jwt_token", null)
    }

    fun clearToken() {
        prefs.edit().remove("jwt_token").apply()
    }
}