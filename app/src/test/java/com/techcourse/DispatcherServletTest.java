package com.techcourse;

import com.techcourse.controller.UserSession;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    @Test
    void 기존_컨트롤러의_JSP_응답을_forward한다() throws Exception {
        // given
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.init();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        // when
        servlet.service(request, response);

        // then
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void 기존_컨트롤러의_redirect_응답을_유지한다() throws Exception {
        // given
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.init();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);

        // when
        servlet.service(request, response);

        // then
        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
        verify(request, never()).getRequestDispatcher(anyString());
    }
}
