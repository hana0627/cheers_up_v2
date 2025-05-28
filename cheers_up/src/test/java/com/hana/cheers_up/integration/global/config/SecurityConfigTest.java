package com.hana.cheers_up.integration.global.config;

import com.hana.cheers_up.application.pub.dto.response.PubResponse;
import com.hana.cheers_up.application.pub.service.PubService;
import com.hana.cheers_up.global.config.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SecurityConfigTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private PubService pubService;

    @MockitoBean
    private JwtUtils jwtUtils;


    @Test
    void permitAll_경로_허용() throws Exception {
        //given

        //when & then
        mvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    void 인증_필요_경로는_로그인페이지로_리디렉션() throws Exception {
        //given

        //when & then
        mvc.perform(get("/api/v2/search"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/api/v1/users/login"));
    }

    @Test
    @WithMockUser(username = "any", roles = "USER")
    void 인증된_사용자_접근_허용() throws Exception {
        //given
        String address = "서울시 동작구 상도로 357";
        List<PubResponse> pubs = List.of(
                new PubResponse("전부치는집 숭실대점", "서울 동작구 상도동 488",
                        "https://map.kakao.com/link/map/전부치는집,37.4969515617887,126.953549196764",
                        "https://map.kakao.com/link/roadview/37.4969515617887,126.953549196764",
                        null,
                        "음식점 > 술집 > 호프,요리주점",
                        "1.0 m"));
        given(pubService.recommendPubs(address)).willReturn(pubs);

        //when & then
        mvc.perform(get("/api/v2/search").param("address", address))
                .andExpect(status().isOk());
    }
}

