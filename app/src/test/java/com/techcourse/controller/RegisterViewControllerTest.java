package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

import static org.mockito.Mockito.mock;

class RegisterViewControllerTest {

    private RegisterViewController registerViewController = new RegisterViewController();

    @Test
    void 회원가입_뷰를_반환한다() {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        //when
        final var modelAndView = registerViewController.execute(request, response);

        //then
        final var expected = new ModelAndView(new JspView("/register.jsp"));
        Assertions.assertThat(modelAndView)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
