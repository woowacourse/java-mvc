package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.web.support.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LogoutControllerTest {

    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession session;
    LogoutController logoutController;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);

        logoutController = new LogoutController();
    }

    @DisplayName("로그아웃을 성공하면 index.jsp로 리다이렉트된다.")
    @Test
    void execute() {
        // given
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getMethod()).thenReturn(RequestMethod.POST.name());
        when(request.getSession()).thenReturn(session);
        doNothing().when(session).removeAttribute(anyString());

        // when
        String viewName = logoutController.execute(request, response);

        // then
        assertThat(viewName).isEqualTo("redirect:/");
    }
}