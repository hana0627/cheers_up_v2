package com.hana.cheers_up.application.api.controller;

import com.hana.cheers_up.application.pub.dto.response.PubResponse;
import com.hana.cheers_up.application.pub.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ApiController {

//    private final KakaoSearchService kakaoSearchService;
    private final DirectionService directionService;

    @GetMapping("/search")
    public String CheersSearch(@RequestParam("address") String address, Model model) {
        log.info("[CheersController CheersSearch] - called");

        List<PubResponse> pubResponses = directionService.recommendPubs(address);

        model.addAttribute("pubs", pubResponses);
        return "cheers/pub_list";

    }
}
