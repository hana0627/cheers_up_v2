package com.hana.cheers_up.unit.application.pub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.cheers_up.application.pub.controller.PubController;
import com.hana.cheers_up.application.pub.dto.response.PubResponse;
import com.hana.cheers_up.application.pub.service.PubService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PubController.class)
class PubControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockBean
    private PubService pubService;

    @Test
    @WithMockUser(username = "user", roles = "USER")  // 인증된 사용자로 설정
    void 주소검색에_성공한다() throws Exception {
        // given
        String address = "서울 동작구 상도로 357";

        List<PubResponse> pubs = List.of(
                new PubResponse("전부치는집 숭실대점", "서울 동작구 상도동 488",
                        "https://map.kakao.com/link/map/전부치는집,37.4969515617887,126.953549196764",
                        "https://map.kakao.com/link/roadview/37.4969515617887,126.953549196764",
                        null,
                        "음식점 > 술집 > 호프,요리주점",
                        "1.0 m"),
                new PubResponse("생활맥주 숭실대입구역점", "서울 동작구 상도동 486",
                        "https://map.kakao.com/link/map/생활맥주,37.49763435656411,126.953123570314",
                        "https://map.kakao.com/link/roadview/37.49763435656411,126.953123570314",
                        null,
                        "음식점 > 술집 > 호프,요리주점 > 생활맥주",
                        "85.0 m"),
                new PubResponse("쿠시야끼하루 상도점", "서울 동작구 상도동 490-5",
                        "https://map.kakao.com/link/map/쿠시야끼하루,37.4960252005324,126.953228613056",
                        "https://map.kakao.com/link/roadview/37.4960252005324,126.953228613056",
                        null,
                        "음식점 > 술집 > 일본식주점",
                        "105.0 m")
        );

        given(pubService.recommendPubs(address)).willReturn(pubs);

        // when & then
        mvc.perform(get("/api/v2/search").param("address", address))
                .andExpect(status().isOk())
                .andExpect(view().name("cheers/pub_list"))
                .andExpect(model().attributeExists("pubs"))
                .andExpect(model().attribute("pubs", pubs));
    }


}