package com.techcourse;

import com.interface21.webmvc.servlet.DispatcherServlet;
import jakarta.servlet.ServletRequest;

import com.techcourse.repository.InMemoryUserRepository;
import com.techcourse.domain.User;
import com.techcourse.controller.UserSession;
import com.interface21.web.http.MediaType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.anyString;

class DispatcherServletTest {

    private DispatcherServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();
    }

    @Test
    void rendersUserApiAsJson() throws Exception {
        final String account = UUID.randomUUID().toString();
        InMemoryUserRepository.save(new User(3, account, "secret", "json@example.com"));
        final var request = apiRequest(account);
        final var response = mock(HttpServletResponse.class);
        final var body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        servlet.service((ServletRequest) request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(body.toString()).isEqualTo("{\"user\":{\"account\":\"" + account + "\"}}");
    }

    @Test
    void rendersInvalidAccountAsBadRequestJson() throws Exception {
        for (final String account : new String[]{null, "", " "}) {
            final var response = mock(HttpServletResponse.class);
            final var body = new StringWriter();
            when(response.getWriter()).thenReturn(new PrintWriter(body));

            servlet.service((ServletRequest) apiRequest(account), response);

            verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
            verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            assertThat(body.toString()).contains("\"error\"", "\"code\":\"INVALID_ACCOUNT\"",
                    "\"message\":\"account is required\"").doesNotContain("<html");
        }
    }

    @Test
    void rendersUnknownUserAsNotFoundJson() throws Exception {
        final var response = mock(HttpServletResponse.class);
        final var body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        servlet.service((ServletRequest) apiRequest(UUID.randomUUID().toString()), response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(body.toString()).contains("\"error\"", "\"code\":\"USER_NOT_FOUND\"",
                "\"message\":\"user not found\"").doesNotContain("<html");
    }

    private HttpServletRequest apiRequest(String account) {
        final var request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getContextPath()).thenReturn("");
        when(request.getParameter("account")).thenReturn(account);
        return request;
    }

    @Test
    @DisplayName("홈 요청은 브라우저 주소를 유지하며 JSP로 포워드한다")
    void forwardsHomeView() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getContextPath()).thenReturn("");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        // when
        servlet.service((ServletRequest) request, response);

        // then
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    @DisplayName("로그아웃은 세션 사용자를 제거하고 홈으로 리다이렉트한다")
    void redirectsLogoutView() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getMethod()).thenReturn("GET");
        when(request.getContextPath()).thenReturn("");
        when(request.getSession()).thenReturn(session);

        // when
        servlet.service((ServletRequest) request, response);

        // then
        verify(response).sendRedirect("/");
        verify(session).removeAttribute(UserSession.SESSION_KEY);
    }

    @Test
    @DisplayName("어노테이션 컨트롤러의 GET 회원가입 요청을 JSP로 포워드한다")
    void forwardsAnnotatedRegisterForm() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getContextPath()).thenReturn("");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        // when
        servlet.service((ServletRequest) request, response);

        // then
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("어노테이션 컨트롤러의 POST 회원가입 요청은 사용자를 저장하고 리다이렉트한다")
    void registersWithAnnotatedController() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final String account = "stage2-register-user";
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getContextPath()).thenReturn("");
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn("secret");
        when(request.getParameter("email")).thenReturn("stage2@example.com");

        // when
        servlet.service((ServletRequest) request, response);

        // then
        assertThat(InMemoryUserRepository.findByAccount(account)).isPresent();
        assertThat(InMemoryUserRepository.findByAccount(account).orElseThrow().checkPassword("secret")).isTrue();
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void forwardsCanonicalAndLegacyLoginViews() throws Exception {
        for (final String path : new String[]{"/login", "/login/view"}) {
            final var request = pageRequest("GET", path);
            final var response = mock(HttpServletResponse.class);
            final var dispatcher = mock(RequestDispatcher.class);
            when(request.getSession()).thenReturn(mock(HttpSession.class));
            when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

            servlet.service((ServletRequest) request, response);

            verify(dispatcher).forward(request, response);
        }
    }

    @Test
    void redirectsLoggedInUserFromLoginPage() throws Exception {
        final var request = pageRequest("GET", "/login");
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(UserSession.SESSION_KEY))
                .thenReturn(InMemoryUserRepository.findByAccount("gugu").orElseThrow());

        servlet.service((ServletRequest) request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void logsInAndRedirectsAfterPost() throws Exception {
        final var request = pageRequest("POST", "/login");
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");

        servlet.service((ServletRequest) request, response);

        verify(session).setAttribute(UserSession.SESSION_KEY,
                InMemoryUserRepository.findByAccount("gugu").orElseThrow());
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void redirectsFailedLoginWithoutSettingSessionUser() throws Exception {
        final var request = pageRequest("POST", "/login");
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("wrong");

        servlet.service((ServletRequest) request, response);

        verify(session, never()).setAttribute(
                org.mockito.ArgumentMatchers.eq(UserSession.SESSION_KEY),
                org.mockito.ArgumentMatchers.any());
        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void keepsLegacyRegisterViewWorking() throws Exception {
        final var request = pageRequest("GET", "/register/view");
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service((ServletRequest) request, response);

        verify(dispatcher).forward(request, response);
    }

    private HttpServletRequest pageRequest(String method, String path) {
        final var request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(path);
        when(request.getContextPath()).thenReturn("");
        return request;
    }
}
