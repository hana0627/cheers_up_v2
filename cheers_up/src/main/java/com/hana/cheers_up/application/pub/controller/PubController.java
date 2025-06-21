package com.hana.cheers_up.application.pub.controller;

import com.hana.cheers_up.application.pub.controller.response.PubResponse;
import com.hana.cheers_up.application.pub.service.PubService;
import com.hana.cheers_up.global.response.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PubController {

    private final PubService pubService;

    @GetMapping("/api/v2/search")
    public APIResponse<List<PubResponse>> CheersSearch(@RequestParam("address") String address) {
        log.info("[CheersController CheersSearch] - called");

        List<PubResponse> result = pubService.recommendPubs(address);
        return APIResponse.success(result);

    }
}
