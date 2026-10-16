package com.techcourse;

import com.techcourse.controller.UserSession;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthenticationFlowTest {

    private DispatcherServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet();
        servlet.init();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
    }

    @Test
    @DisplayName("잘못된 비밀번호로는 로그인할 수 없다")
    void rejectsWrongPassword() throws Exception {
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("wrong-password");

        servlet.service(request, response);

        verify(response).sendRedirect("/401.jsp");
        verify(session, never()).setAttribute(eq(UserSession.SESSION_KEY), any());
    }

    @Test
    @DisplayName("존재하지 않는 계정으로는 로그인할 수 없다")
    void rejectsUnknownAccount() throws Exception {
        when(request.getParameter("account")).thenReturn("unknown-account");

        servlet.service(request, response);

        verify(response).sendRedirect("/401.jsp");
        verify(session, never()).setAttribute(eq(UserSession.SESSION_KEY), any());
    }

    @Test
    @DisplayName("이미 로그인한 사용자의 로그인 요청은 홈으로 이동한다")
    void redirectsAuthenticatedLogin() throws Exception {
        authenticateSession();

        servlet.service(request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    @DisplayName("이미 로그인한 사용자가 로그인 화면에 접근하면 홈으로 이동한다")
    void redirectsAuthenticatedLoginView() throws Exception {
        authenticateSession();
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");

        servlet.service(request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    @DisplayName("로그인은 POST 요청으로만 처리한다")
    void rejectsGetLogin() throws Exception {
        when(request.getMethod()).thenReturn("GET");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void authenticateSession() {
        final var user = InMemoryUserRepository.findByAccount("gugu").orElseThrow();
        when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(user);
    }
}
