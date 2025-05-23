package com.hana.cheers_up.global.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class ErrorPageController {
    @RequestMapping("/api/v1/error-page/404")
    public String errorPage404() {
        log.info("errorPage 404");
        return "/errors/errorPage";
    }
    @RequestMapping("/api/v1/error-page/500")
    public String errorPage500() {
        log.info("errorPage 500");
        return "/errors/errorPage";
    }
}
