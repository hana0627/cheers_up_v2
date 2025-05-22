//package com.hana.cheers_up.unit.application.user.controller;
//
//import com.hana.cheers_up.application.user.controller.UserAccountController;
//import com.hana.cheers_up.application.user.service.UserAccountService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(UserAccountController.class)
//class UserAccountControllerTest {
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private UserAccountService userAccountService;
//
//    @Test
//    @WithMockUser(username = "kakao_1234567890")
//    void 로그인_api호출시_jwt토큰을_발급한다() throws Exception {
//        //given
//        String userId = "kakao_1234567890";
//        String token = "thisistesttoken";
//        given(userAccountService.login(userId)).willReturn(token);
//
//        //when & then
//        mvc.perform(get("/api/v1/users/login"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON))
//                .andExpect(jsonPath("$.resultCode").value(HttpStatus.OK.name()))
//                .andExpect(jsonPath("$.result").value(token))
//                .andDo(print());
//
//        then(userAccountService).should().login(userId);
//    }
//
//}
