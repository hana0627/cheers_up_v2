package com.hana.cheers_up.global.response;

import org.springframework.http.HttpStatus;

public class APIResponse<T> {
    private final String resultCode;
    private final T result;

    public APIResponse(String resultCode, T result) {
        this.resultCode = resultCode;
        this.result = result;
    }

    public String getResultCode() {
        return resultCode;
    }

    public T getResult() {
        return result;
    }

    public static <T> APIResponse<T> success(T result) {
        return new APIResponse<>(HttpStatus.OK.name(), result);
    }

}
