package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginControllerTest {

    private final LoginController loginController = new LoginController();

    @Test
    @DisplayName("올바른 정보로 로그인 하면 index.jsp가 반환된다.")
    void login() throws Exception {
        //given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");

        HttpSession httpSession = mock(HttpSession.class);
        doNothing().when(httpSession).setAttribute(any(), any());
        when(request.getSession()).thenReturn(httpSession);

        //when
        final ModelAndView modelAndView = loginController.login(request, response);

        //then
        assertThat(modelAndView)
                .usingRecursiveComparison()
                .isEqualTo(new ModelAndView(new JspView("redirect:" + "/index.jsp")));
    }
}
