package com.techcourse;

import com.interface21.webmvc.servlet.DispatcherServlet;
import com.techcourse.controller.UserSession;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class LoginViewControllerIntegrationTest {

    private Servlet dispatcherServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        final var servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();
        dispatcherServlet = servlet;
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);
    }

    @Test
    void 로그인하지_않은_사용자는_로그인_화면으로_전달한다() throws Exception {
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(session).getAttribute(UserSession.SESSION_KEY);
        verify(requestDispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void 이미_로그인한_사용자는_홈으로_이동한다() throws Exception {
        final var user = InMemoryUserRepository.findByAccount("gugu").orElseThrow();
        when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(user);

        dispatcherServlet.service(request, response);

        verify(response).sendRedirect("/index.jsp");
        verify(request, never()).getRequestDispatcher(anyString());
        verify(session, never()).setAttribute(anyString(), any());
    }

    @Test
    void POST_로그인_화면_요청에는_404로_응답하고_세션에_접근하지_않는다() throws Exception {
        when(request.getMethod()).thenReturn("POST");

        dispatcherServlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verify(request, never()).getRequestDispatcher(anyString());
        verifyNoInteractions(session);
    }
}
