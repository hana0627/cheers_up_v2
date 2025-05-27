package com.hana.cheers_up.global.exception;


import com.hana.cheers_up.global.response.APIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(RestControllerAdvice.class);

    @ExceptionHandler(ApplicationException.class)
    public APIResponse<String> applicationExceptionHandler(ApplicationException error) {
        log.error("=== Application Exception occurred !! error : {} ===", error.getMessage());
        return APIResponse.error(error);
    }
}
