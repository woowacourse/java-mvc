package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.web.http.MediaType;
import com.techcourse.controller.UserSession;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.NoSuchElementException;
import java.util.UUID;

class DispatcherServletIntegrationTest {

    private DispatcherServlet servlet;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new DispatcherServlet();
        servlet.init();
    }

    @Test
    @DisplayName("GET 회원가입 요청은 화면을 표시하고 회원을 저장하지 않는다")
    void show_register_without_saving_user() throws Exception {
        final var request = request("/register", "GET");
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        final var account = UUID.randomUUID().toString();
        when(request.getParameter("account")).thenReturn(account);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
        assertThat(InMemoryUserRepository.findByAccount(account)).isEmpty();
    }

    @Test
    @DisplayName("POST 회원가입 요청은 회원을 저장하고 첫 화면으로 redirect한다")
    void save_register_and_redirect() throws Exception {
        final var account = UUID.randomUUID().toString();
        final var request = registrationRequest(account);
        final var response = mock(HttpServletResponse.class);

        servlet.service(request, response);

        final var user = InMemoryUserRepository.findByAccount(account).orElseThrow();
        assertThat(user.checkPassword("password")).isTrue();
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    @DisplayName("어노테이션으로 가입한 회원이 기존 로그인 컨트롤러에서 로그인할 수 있다")
    void login_registered_user_with_legacy_controller() throws Exception {
        final var account = UUID.randomUUID().toString();
        servlet.service(registrationRequest(account), mock(HttpServletResponse.class));
        final var request = request("/login", "POST");
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn("password");

        servlet.service(request, response);

        final var user = InMemoryUserRepository.findByAccount(account).orElseThrow();
        verify(session).setAttribute(UserSession.SESSION_KEY, user);
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    @DisplayName("기존 회원가입 화면 경로를 유지한다")
    void preserve_legacy_register_view() throws Exception {
        final var request = request("/register/view", "GET");
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("기존 로그인 화면을 표시한다")
    void show_legacy_login_view() throws Exception {
        final var request = request("/login/view", "GET");
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("기존 로그아웃은 세션에서 회원을 제거하고 redirect한다")
    void logout_with_legacy_controller() throws Exception {
        final var request = request("/logout", "GET");
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        servlet.service(request, response);

        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
    }

    @Test
    @DisplayName("회원가입의 미지원 HTTP method는 회원을 저장하지 않고 404로 응답한다")
    void reject_unsupported_register_methods() throws Exception {
        for (String method : new String[]{"PUT", "CONNECT"}) {
            final var request = request("/register", method);
            final var response = mock(HttpServletResponse.class);

            servlet.service(request, response);

            verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
            verify(request, never()).getParameter("account");
        }
    }

    @Test
    @DisplayName("두 매핑에 없는 경로는 404로 응답한다")
    void respond_not_found_for_unknown_path() throws Exception {
        final var response = mock(HttpServletResponse.class);

        servlet.service(request("/missing", "GET"), response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("회원 조회 API는 회원 정보만 감싸지 않은 JSON으로 응답한다")
    void find_user_as_json() throws Exception {
        final var request = request("/api/user", "GET");
        final var response = mock(HttpServletResponse.class);
        final var output = new StringWriter();
        when(request.getParameter("account")).thenReturn("gugu");
        when(response.getWriter()).thenReturn(new PrintWriter(output));

        servlet.service(request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(output.toString()).isEqualTo("{\"account\":\"gugu\"}");
    }

    @Test
    @DisplayName("조회할 회원이 없으면 회원 조회 실패의 원인을 전달한다")
    void preserve_missing_user_failure() {
        final var request = request("/api/user", "GET");
        when(request.getParameter("account")).thenReturn(UUID.randomUUID().toString());

        assertThatThrownBy(() -> servlet.service(request, mock(HttpServletResponse.class)))
                .isInstanceOf(ServletException.class)
                .hasRootCauseInstanceOf(NoSuchElementException.class);
    }

    private HttpServletRequest registrationRequest(final String account) {
        final var request = request("/register", "POST");
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("registered@example.com");
        return request;
    }

    private HttpServletRequest request(final String uri, final String method) {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(uri);
        when(request.getMethod()).thenReturn(method);
        return request;
    }
}
