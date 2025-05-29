package com.hana.cheers_up.global.exception.constant;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum
ErrorCode {
    SAMPLE_ERROR_CODE(HttpStatus.INTERNAL_SERVER_ERROR, "예시용 에러코드 입니다."),

    INVALID_TOKEN_FORMAT(HttpStatus.BAD_REQUEST, "토큰 형식이 올바르지 않습니다."),
    JWT_SIGNATURE_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "JWT 시그니처 생성 중 오류가 발생했습니다."),
    JWT_SECRET_KEY_NOT_CONFIGURED(HttpStatus.INTERNAL_SERVER_ERROR, "JWT 비밀키 또는 만료시간이 설정되지 않았습니다."),
    JWT_TOKEN_PARSING_FAILED(HttpStatus.BAD_REQUEST, "JWT 토큰 파싱 중 오류가 발생했습니다."),
    UNSUPPORTED_LOGIN_TYPE(HttpStatus.BAD_REQUEST,"지원하지 않는 로그인 타입입니다."),
    NULL_USER_REQUEST(HttpStatus.BAD_REQUEST,"userRequest가 null 입니다."),
    UNAUTHORIZE(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),


    // 카카오 관련
    KAKAO_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 API호출 중 예상하지 못한 예외가 발생했습니다."),
    KAKAO_CLIENT_ERROR(HttpStatus.BAD_REQUEST, "카카오 클라이언트 오류"),
    KAKAO_UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED, "카카오 통신 권한 없음"),
    KAKAO_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 서버 오류"),
    KAKAO_RESPONSE_EMPTY(HttpStatus.NO_CONTENT, "카카오 API 응답이 비어있습니다"),

    // 네트워크 관련
    NETWORK_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "네트워크 연결에 문제가 발생했습니다");

    private final HttpStatus status;
    private final String message;


    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
