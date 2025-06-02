package com.hana.cheers_up.api.response

data class APIResponse<T>(
    val resultCode: String,
    val result: T?,
    val message: String? = null
)