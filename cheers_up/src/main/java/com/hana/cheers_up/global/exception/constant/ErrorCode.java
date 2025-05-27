package com.hana.cheers_up.global.exception.constant;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    SAMPLE_ERROR_CODE(HttpStatus.INTERNAL_SERVER_ERROR, "예시용 에러코드 입니다."),

    INVALID_TOKEN_FORMAT(HttpStatus.BAD_REQUEST, "토큰 형식이 올바르지 않습니다."),
    JWT_SIGNATURE_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "JWT 시그니처 생성 중 오류가 발생했습니다."),
    JWT_SECRET_KEY_NOT_CONFIGURED(HttpStatus.INTERNAL_SERVER_ERROR, "JWT 비밀키 또는 만료시간이 설정되지 않았습니다."),
    JWT_TOKEN_PARSING_FAILED(HttpStatus.BAD_REQUEST, "JWT 토큰 파싱 중 오류가 발생했습니다."),
    UNSUPPORTED_LOGIN_TYPE(HttpStatus.BAD_REQUEST,"지원하지 않는 로그인 타입입니다."),
    NULL_USER_REQUEST(HttpStatus.BAD_REQUEST,"userRequest가 null 입니다."),


    ;

    private final HttpStatus status;
    private final String message;


    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
