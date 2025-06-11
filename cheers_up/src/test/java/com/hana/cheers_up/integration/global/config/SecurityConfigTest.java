package com.hana.cheers_up.integration.global.config;

import com.hana.cheers_up.application.pub.dto.response.PubResponse;
import com.hana.cheers_up.application.pub.service.PubService;
import com.hana.cheers_up.global.config.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
    void 인증_필요_경로접근시_예외발생() throws Exception {
        //given

        //when & then
        mvc.perform(get("/api/v2/search"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.resultCode").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.result").value("로그인이 필요한 서비스입니다."));
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


    /*

    @Test
void POST_요청시_인증없으면_JwtAuthenticationEntryPoint가_실행된다() throws Exception {
    //given

    //when & then
    mvc.perform(post("/api/v2/search"))
            .andExpect(status().isUnauthorized())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.resultCode").value("UNAUTHORIZED"))
            .andExpect(jsonPath("$.result").value("로그인이 필요한 서비스입니다."));
}
     */

    @Test
    void 요청시_예외가_발생하면_JwtAuthenticationEntryPoint가_실행된다() throws Exception{
        //given

        //when && then
        mvc.perform(post("/api/v2/search"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.resultCode").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.result").value("로그인이 필요한 서비스입니다."));

    }


    @Test
    void Bearer_없는_Authorization_헤더로_접근시_JwtAuthenticationEntryPoint가_실행된다() throws Exception {
        //given
        String malformedToken = "InvalidPrefix sometoken";

        //when & then
        mvc.perform(get("/api/v2/search")
                        .header(HttpHeaders.AUTHORIZATION, malformedToken))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.resultCode").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.result").value("로그인이 필요한 서비스입니다."));
    }

    @Test
    void 잘못된_JWT_토큰으로_접근시_JwtAuthenticationEntryPoint가_실행된다() throws Exception {
        //given
        String invalidToken = "Bearer invalid.jwt.token";
        given(jwtUtils.isExpired("invalid.jwt.token")).willReturn(true);

        //when & then
        mvc.perform(get("/api/v2/search")
                        .header(HttpHeaders.AUTHORIZATION, invalidToken))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.resultCode").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.result").value("로그인이 필요한 서비스입니다."));
    }


    /*

     */
}

