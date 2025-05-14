package com.hana.cheers_up.unit.application.user.controller;

import com.hana.cheers_up.application.user.controller.UserAccountController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(UserAccountController.class)
class UserAccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser
    void 로그인_api호출시_검색페이지로_이동한다() throws Exception {
        //given
        // nothing

        //when & then
        mvc.perform(get("/users/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("/cheers/search"));


    }
}
