package com.hana.cheers_up.application;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String home() {
//        return "forward:/api/v1/users/login";
        return "index";
    }
}
