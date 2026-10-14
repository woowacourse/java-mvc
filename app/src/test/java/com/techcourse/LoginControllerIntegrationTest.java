package com.techcourse;

import com.interface21.webmvc.servlet.DispatcherServlet;
import com.techcourse.controller.UserSession;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class LoginControllerIntegrationTest {

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
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession()).thenReturn(session);
    }

    @Test
    void 올바른_계정과_비밀번호이면_세션에_사용자를_저장하고_홈으로_이동한다() throws Exception {
        final var user = InMemoryUserRepository.findByAccount("gugu").orElseThrow();
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");

        dispatcherServlet.service(request, response);

        verify(session).setAttribute(UserSession.SESSION_KEY, user);
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void 비밀번호가_틀리면_세션에_사용자를_저장하지_않고_인증_실패_화면으로_이동한다() throws Exception {
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("wrong-password");

        dispatcherServlet.service(request, response);

        verify(session, never()).setAttribute(anyString(), any());
        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void 존재하지_않는_계정이면_세션에_사용자를_저장하지_않고_인증_실패_화면으로_이동한다() throws Exception {
        when(request.getParameter("account")).thenReturn("unknown-login-" + UUID.randomUUID());

        dispatcherServlet.service(request, response);

        verify(session, never()).setAttribute(anyString(), any());
        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void 이미_로그인한_사용자는_입력_정보를_검증하지_않고_홈으로_이동한다() throws Exception {
        final var user = InMemoryUserRepository.findByAccount("gugu").orElseThrow();
        when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(user);

        dispatcherServlet.service(request, response);

        verify(request, never()).getParameter(anyString());
        verify(session, never()).setAttribute(anyString(), any());
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void GET_로그인_처리_요청에는_404로_응답하고_세션에_접근하지_않는다() throws Exception {
        when(request.getMethod()).thenReturn("GET");

        dispatcherServlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verify(request, never()).getParameter(anyString());
        verifyNoInteractions(session);
    }
}
