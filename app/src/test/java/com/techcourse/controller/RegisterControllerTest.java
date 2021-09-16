package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterControllerTest {

    HttpServletRequest request;
    HttpServletResponse response;
    RegisterController registerController;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        registerController = new RegisterController();
    }

    @DisplayName("회원가입에 성공하면 index.jsp로 리다이렉트 된다.")
    @Test
    void registerSuccess() {
        // given
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn(RequestMethod.POST.name());
        when(request.getParameter("account")).thenReturn("charlie");
        when(request.getParameter("password")).thenReturn("1234");
        when(request.getParameter("email")).thenReturn("test@test.com");

        // when
        String viewName = registerController.register(request, response);

        // then
        assertThat(viewName).isEqualTo("redirect:/index.jsp");
    }

    @DisplayName("이미 존재하는 계정으로 회원가입하면 409.jsp로 리다이렉트 된다.")
    @Test
    void registerFailed() {
        // given
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn(RequestMethod.POST.name());
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("1234");
        when(request.getParameter("email")).thenReturn("test@test.com");

        // when
        String viewName = registerController.register(request, response);

        // then
        assertThat(viewName).isEqualTo("409.jsp");
    }

    @Test
    void view() {
        // given
        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getMethod()).thenReturn(RequestMethod.GET.name());

        // when
        String viewName = registerController.view(request, response);

        // then
        assertThat(viewName).isEqualTo("/register.jsp");
    }
}