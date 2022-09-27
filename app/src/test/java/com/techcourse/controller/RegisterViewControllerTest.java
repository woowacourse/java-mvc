package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegisterViewControllerTest {

    @DisplayName("회원가입 뷰를 보여줄 수 있다.")
    @Test
    void renderRegisterView() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        final RegisterViewController registerViewController = new RegisterViewController();

        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);
        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getMethod()).thenReturn("GET");

        final ModelAndView modelAndView = registerViewController.renderRegisterView(request, response);
        final View view = modelAndView.getView();

        modelAndView.renderView(request, response);

        assertThat(view).isInstanceOf(JspView.class);
        verify(requestDispatcher).forward(request, response);
    }
}
