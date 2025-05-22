//package com.hana.cheers_up.application.user.controller;
//
//import com.hana.cheers_up.application.user.service.UserAccountService;
//import com.hana.cheers_up.global.response.APIResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RequiredArgsConstructor
//@RestController
//public class UserAccountController {
//
//    private final UserAccountService userAccountService;
//
//    @RequestMapping("/api/v1/users/login")
//    public APIResponse<String> login(Authentication authentication) {
//        log.info("[UserAccountController login]");
//        String result = userAccountService.login(authentication.getName());
//
//        return APIResponse.success(result);
//    }
//
//}
//
//// unused API
////    @PostMapping("/users/add")
////    public String addUser(@RequestBody UserAccountDto dto) {
////        log.info("[UserAccountController addUser] ");
////        userAccountService.saveUser(dto);
////        return "/cheers/search";
////    }
