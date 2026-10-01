package com.techcourse;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private DispatcherServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet();
        servlet.init();
    }

    @Test
    @DisplayName("기존 컨트롤러가 반환한 JSP 경로로 포워드한다")
    void forwardsLegacyControllerView() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        // when
        servlet.service(request, response);

        // then
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("기존 컨트롤러가 반환한 redirect 경로로 리다이렉트한다")
    void redirectsLegacyControllerView() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getSession()).thenReturn(mock(HttpSession.class));

        // when
        servlet.service(request, response);

        // then
        verify(response).sendRedirect("/");
    }
}
