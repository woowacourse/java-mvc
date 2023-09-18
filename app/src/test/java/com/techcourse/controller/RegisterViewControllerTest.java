package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterViewControllerTest {
    private final RegisterViewController registerViewController = new RegisterViewController();

    @Test
    @DisplayName("가입 뷰를 보여준다.")
    void registerViewController() {
        //given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        //when
        final ModelAndView modelAndView = registerViewController.execute(request, response);

        //then
        assertThat(modelAndView)
                .usingRecursiveComparison()
                .isEqualTo(new ModelAndView(new JspView("/register.jsp")));
    }
}
