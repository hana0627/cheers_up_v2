package com.hana.cheers_up.unit.application.pub.controller;

import com.hana.cheers_up.application.pub.controller.PubController;
import com.hana.cheers_up.application.pub.dto.response.PubResponse;
import com.hana.cheers_up.application.pub.service.PubService;
import com.hana.cheers_up.global.exception.ApplicationException;
import com.hana.cheers_up.global.exception.constant.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PubController.class)
class PubControllerTest {

    @Autowired
    private MockMvc mvc;

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
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result.length()").value(pubs.size()))
                .andExpect(jsonPath("$.result[0].pubName").value(pubs.get(0).pubName()))
                .andExpect(jsonPath("$.result[0].pubAddress").value(pubs.get(0).pubAddress()))
                .andExpect(jsonPath("$.result[1].pubName").value(pubs.get(1).pubName()))
                .andExpect(jsonPath("$.result[2].pubName").value(pubs.get(2).pubName()))
                .andDo(print());
        then(pubService).should().recommendPubs(address);
    }


    @Test
    @WithMockUser(username = "user", roles = "USER")  // 인증된 사용자로 설정
    void 카카오_주소검색_실패시_예외가_발생한다() throws Exception {
        // given
        String address = "서울 동작구 상도로 357";

        given(pubService.recommendPubs(address)).willThrow(new ApplicationException(ErrorCode.KAKAO_API_ERROR, ErrorCode.KAKAO_API_ERROR.getMessage()));

        // when & then
        mvc.perform(get("/api/v2/search").param("address", address))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ErrorCode.KAKAO_API_ERROR.getStatus().name()))
                .andExpect(jsonPath("$.result").value(ErrorCode.KAKAO_API_ERROR.getMessage()))
                .andDo(print());

        then(pubService).should().recommendPubs(address);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void 카카오_주소검색시_예상하지_못한_예외가_발생할_수_있다() throws Exception {
        // given
        String address = "서울 동작구 상도로 357";

        // RestTemplate 내부에서 발생할 수 있는 예외
        given(pubService.recommendPubs(address))
                .willThrow(new org.springframework.web.client.RestClientException("Connection reset"));

        // when & then
        mvc.perform(get("/api/v2/search").param("address", address))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.resultCode").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.result").value("알 수 없는 예외가 발생했습니다."))
                .andDo(print());
    }

}
