package com.techcourse;

import com.interface21.webmvc.servlet.DispatcherServlet;
import jakarta.servlet.ServletRequest;

import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;

class LoginIntegrationTest {
    private final DispatcherServlet servlet = DispatcherServletInitializer.createDispatcherServlet();
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final HttpSession session = mock(HttpSession.class);
    private final User user = new User(10, "login-" + UUID.randomUUID(), "password", "test@example.com");

    @BeforeEach
    void setUp() {
        servlet.init();
        InMemoryUserRepository.save(user);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    @DisplayName("로그인 성공 시 세션에 사용자를 저장하고 홈으로 리다이렉트한다")
    void logsIn() throws Exception {
        when(request.getParameter("account")).thenReturn(user.getAccount());
        when(request.getParameter("password")).thenReturn("password");
        perform("POST", "/login");
        verify(session).setAttribute(UserSession.SESSION_KEY, user);
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    @DisplayName("비밀번호가 틀리면 세션 저장 없이 실패 화면으로 이동한다")
    void rejectsWrongPassword() throws Exception {
        when(request.getParameter("account")).thenReturn(user.getAccount());
        when(request.getParameter("password")).thenReturn("wrong");
        assertLoginFailure();
    }

    @Test
    @DisplayName("등록되지 않은 계정은 로그인에 실패한다")
    void rejectsUnknownAccount() throws Exception {
        when(request.getParameter("account")).thenReturn("missing-" + UUID.randomUUID());
        assertLoginFailure();
    }

    @Test
    @DisplayName("계정 파라미터가 없어도 서버 오류 대신 로그인 실패로 처리한다")
    void rejectsMissingAccount() throws Exception {
        assertLoginFailure();
    }

    @Test
    @DisplayName("이미 로그인한 사용자는 인증을 반복하지 않고 홈으로 이동한다")
    void redirectsLoggedInUser() throws Exception {
        when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(user);
        perform("POST", "/login");
        verify(response).sendRedirect("/index.jsp");
        verify(session, never()).setAttribute(anyString(), any());
    }

    @Test
    @DisplayName("비로그인 사용자의 로그인 화면 요청을 JSP로 포워드한다")
    void showsLoginForm() throws Exception {
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);
        perform("GET", "/login/view");
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("로그인한 사용자의 로그인 화면 요청은 홈으로 이동한다")
    void skipsLoginForm() throws Exception {
        when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(user);
        perform("GET", "/login/view");
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    @DisplayName("로그아웃은 세션의 사용자 속성을 제거하고 루트로 이동한다")
    void logsOut() throws Exception {
        perform("GET", "/logout");
        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
    }

    @Test
    @DisplayName("GET 로그인 요청은 인증을 실행하지 않고 404를 반환한다")
    void rejectsGetLogin() throws Exception {
        perform("GET", "/login");
        verify(response).sendError(404);
        verify(session, never()).setAttribute(anyString(), any());
    }

    private void assertLoginFailure() throws Exception {
        perform("POST", "/login");
        verify(response).sendRedirect("/401.jsp");
        verify(session, never()).setAttribute(anyString(), any());
    }

    private void perform(final String method, final String path) throws Exception {
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(path);
        servlet.service((ServletRequest) request, response);
    }
}
