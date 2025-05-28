package com.hana.cheers_up.global.response;

import com.hana.cheers_up.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class APIResponse<T> {
    private final String resultCode;
    private final T result;

    public APIResponse(String resultCode, T result) {
        this.resultCode = resultCode;
        this.result = result;
    }

    public static APIResponse<String> error(Exception error) {
        if (error instanceof ApplicationException appError) {
            return new APIResponse<>(appError.getErrorCode().getStatus().name(), error.getMessage());
        }
        return new APIResponse<>(INTERNAL_SERVER_ERROR.name(), "알 수 없는 예외가 발생했습니다.");
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
