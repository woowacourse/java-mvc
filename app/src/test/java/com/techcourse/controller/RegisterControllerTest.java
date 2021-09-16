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
    void executeSuccess() {
        // given
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn(RequestMethod.POST.name());
        when(request.getParameter("account")).thenReturn("charlie");
        when(request.getParameter("password")).thenReturn("1234");
        when(request.getParameter("email")).thenReturn("test@test.com");

        // when
        String viewName = registerController.execute(request, response);

        // then
        assertThat(viewName).isEqualTo("redirect:/index.jsp");
    }

    @DisplayName("이미 존재하는 계정으로 회원가입하면 409.jsp로 리다이렉트 된다.")
    @Test
    void executeFailed() {
        // given
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn(RequestMethod.POST.name());
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("1234");
        when(request.getParameter("email")).thenReturn("test@test.com");

        // when
        String viewName = registerController.execute(request, response);

        // then
        assertThat(viewName).isEqualTo("409.jsp");
    }
}