package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterViewControllerTest {

    HttpServletRequest request;
    HttpServletResponse response;
    RegisterViewController registerViewController;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        registerViewController = new RegisterViewController();
    }

    @Test
    void execute() {
        // given
        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getMethod()).thenReturn(RequestMethod.GET.name());

        // when
        String viewName = registerViewController.execute(request, response);

        // then
        assertThat(viewName).isEqualTo("/register.jsp");
    }
}