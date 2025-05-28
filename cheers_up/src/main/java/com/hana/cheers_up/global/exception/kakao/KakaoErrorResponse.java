package com.hana.cheers_up.global.exception.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Getter
public class KakaoErrorResponse {
    private int code;
    private String msg;

    public KakaoErrorResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /*
    ex) 토큰이 잘못되었을 경우
        HTTP/1.1 401 Unauthorized
        WWW-Authenticate: Bearer error=invalid_token
        {
          "code":-401,
          "msg":"InvalidTokenException"
        }
     */
}
