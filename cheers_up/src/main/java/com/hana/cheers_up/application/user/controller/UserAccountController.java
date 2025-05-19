package com.hana.cheers_up.application.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
// @RequiredArgsConstructor
@Controller
public class UserAccountController {

//    private final UserAccountService userAccountService;

    @RequestMapping("/api/v1/users/login")
    public String login() {
        log.info("[UserAccountController login]");
        return "/cheers/search";
    }

}

// unused API
//    @PostMapping("/users/add")
//    public String addUser(@RequestBody UserAccountDto dto) {
//        log.info("[UserAccountController addUser] ");
//        userAccountService.saveUser(dto);
//        return "/cheers/search";
//    }
