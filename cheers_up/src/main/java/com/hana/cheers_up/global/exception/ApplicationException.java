package com.hana.cheers_up.global.exception;

import com.hana.cheers_up.global.exception.constant.ErrorCode;

public class ApplicationException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String message;

    public ApplicationException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message != null ? message : errorCode.getMessage();
    }

}
