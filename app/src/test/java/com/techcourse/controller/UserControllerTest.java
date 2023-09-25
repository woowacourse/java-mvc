package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class UserControllerTest {

    @DisplayName("UserController는 /api/user?account=gugu 요청에 대해 user 객체를 반환한다.")
    @Test
    void show() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        given(request.getParameter("account")).willReturn("gugu");

        // when
        UserController userController = new UserController();
        ModelAndView modelAndView = userController.show(request, response);

        // then
        assertThat(modelAndView.getObject("user"))
                .extracting("account")
                .isEqualTo("gugu");
    }
}
