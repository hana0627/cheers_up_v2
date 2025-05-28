package com.hana.cheers_up.global.exception;


import com.hana.cheers_up.global.response.APIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(RestControllerAdvice.class);

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<APIResponse<String>> applicationExceptionHandler(ApplicationException error) {
        log.error("=== Application Exception occurred !! error : {} ===", error.getMessage());
        APIResponse<String> response = APIResponse.error(error);
        return ResponseEntity.status(error.getErrorCode().getStatus())
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<String>> ExceptionHandler(Exception error) {
        log.error("=== Application Exception occurred !! error : {} ===", error.getMessage());

        APIResponse<String> response = APIResponse.error(error);  // else 분기 실행됨

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);

    }
}
