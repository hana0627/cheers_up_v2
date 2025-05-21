package com.hana.cheers_up.unit.global.exception.controller;

import com.hana.cheers_up.global.exception.controller.ErrorPageController;
import com.hana.cheers_up.mock.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ErrorPageController.class)
@Import(TestSecurityConfig.class)
class ErrorPageControllerTest  {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("404페이지 호출이 정상적으로 이루어진다.")
    void errorPage404테스트() throws Exception{
        //given

        //when & then
        mvc.perform(get("/api/v1/error-page/404"))
                .andExpect(status().isOk())
                .andExpect(view().name("/errors/errorPage"));


    }

    @Test
    @DisplayName("500페이지 호출이 정상적으로 이루어진다.")
    void errorPage500테스트() throws Exception{
        //given

        //when & then
        mvc.perform(get("/api/v1/error-page/500"))
                .andExpect(status().isOk())
                .andExpect(view().name("/errors/errorPage"));


    }

}