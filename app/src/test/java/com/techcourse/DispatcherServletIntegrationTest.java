package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.DispatcherServlet;
import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.UUID;

class DispatcherServletIntegrationTest {

    private Servlet servlet;

    @BeforeEach
    void setUp() throws Exception {
        final var dispatcherServlet = new DispatcherServlet("com.techcourse.controller");
        dispatcherServlet.init();
        servlet = dispatcherServlet;
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
    @DisplayName("가입한 회원이 로그인하면 세션에 회원을 저장한다")
    void login_registered_user() throws Exception {
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
    @DisplayName("회원가입 화면 경로를 유지한다")
    void preserve_register_view() throws Exception {
        final var request = request("/register/view", "GET");
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("로그인 화면을 표시한다")
    void show_login_view() throws Exception {
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
    @DisplayName("로그아웃은 세션에서 회원을 제거하고 redirect한다")
    void logout() throws Exception {
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
    @DisplayName("회원 도메인에 다른 getter가 있어도 조회 응답은 account만 포함한다")
    void expose_only_account_in_user_response() throws Exception {
        final var account = UUID.randomUUID().toString();
        InMemoryUserRepository.save(new UserWithPassword(account, "private-password"));
        final var request = request("/api/user", "GET");
        final var response = mock(HttpServletResponse.class);
        final var output = new StringWriter();
        when(request.getParameter("account")).thenReturn(account);
        when(response.getWriter()).thenReturn(new PrintWriter(output));

        servlet.service(request, response);

        assertThat(output.toString()).isEqualTo("{\"account\":\"" + account + "\"}");
    }

    @Test
    @DisplayName("회원 조회 계정이 누락되거나 비어 있으면 400과 JSON 오류 메시지를 응답한다")
    void reject_missing_or_blank_user_account() throws Exception {
        for (String account : new String[]{null, "", "   "}) {
            final var request = request("/api/user", "GET");
            final var response = mock(HttpServletResponse.class);
            final var output = new StringWriter();
            when(request.getParameter("account")).thenReturn(account);
            when(response.getWriter()).thenReturn(new PrintWriter(output));

            servlet.service(request, response);

            verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
            verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            assertThat(output.toString()).isEqualTo("{\"message\":\"account is required\"}");
        }
    }

    @Test
    @DisplayName("조회할 회원이 없으면 404와 JSON 오류 메시지를 응답한다")
    void respond_not_found_for_missing_user() throws Exception {
        final var request = request("/api/user", "GET");
        final var response = mock(HttpServletResponse.class);
        final var output = new StringWriter();
        when(request.getParameter("account")).thenReturn(UUID.randomUUID().toString());
        when(response.getWriter()).thenReturn(new PrintWriter(output));

        servlet.service(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(output.toString()).isEqualTo("{\"message\":\"user not found\"}");
    }

    @Test
    @DisplayName("공백을 포함하는 유효한 계정 값은 변경하지 않고 조회한다")
    void preserve_valid_account_value() throws Exception {
        final var account = " " + UUID.randomUUID() + " ";
        InMemoryUserRepository.save(new User(3, account, "password", "account@example.com"));
        final var request = request("/api/user", "GET");
        final var response = mock(HttpServletResponse.class);
        final var output = new StringWriter();
        when(request.getParameter("account")).thenReturn(account);
        when(response.getWriter()).thenReturn(new PrintWriter(output));

        servlet.service(request, response);

        assertThat(output.toString()).isEqualTo("{\"account\":\"" + account + "\"}");
    }

    @Test
    @DisplayName("첫 화면은 index JSP로 요청을 전달한다")
    void show_home() throws Exception {
        final var request = request("/", "GET");
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("없는 계정으로 로그인하면 인증 실패 화면으로 redirect한다")
    void reject_login_for_unknown_account() throws Exception {
        final var request = request("/login", "POST");
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("account")).thenReturn(UUID.randomUUID().toString());

        servlet.service(request, response);

        verify(response).sendRedirect("/401.jsp");
        verify(session, never()).setAttribute(anyString(), any());
    }

    @Test
    @DisplayName("비밀번호가 틀리면 인증 실패 화면으로 redirect한다")
    void reject_login_for_wrong_password() throws Exception {
        final var request = request("/login", "POST");
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("wrong-password");

        servlet.service(request, response);

        verify(response).sendRedirect("/401.jsp");
        verify(session, never()).setAttribute(anyString(), any());
    }

    @Test
    @DisplayName("이미 로그인한 회원은 로그인 요청과 화면 요청에서 첫 화면으로 redirect한다")
    void redirect_logged_in_user() throws Exception {
        final var user = InMemoryUserRepository.findByAccount("gugu").orElseThrow();
        final var routes = Map.of("/login", "POST", "/login/view", "GET");
        for (Map.Entry<String, String> route : routes.entrySet()) {
            final var request = request(route.getKey(), route.getValue());
            final var response = mock(HttpServletResponse.class);
            final var session = mock(HttpSession.class);
            when(request.getSession()).thenReturn(session);
            when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(user);

            servlet.service(request, response);

            verify(response).sendRedirect("/index.jsp");
            verify(request, never()).getParameter(anyString());
        }
    }

    @Test
    @DisplayName("등록된 경로라도 지원하지 않는 HTTP method면 404로 응답한다")
    void reject_unsupported_route_methods() throws Exception {
        final var routes = Map.of("/", "POST", "/login", "GET", "/login/view", "POST", "/logout", "POST",
                "/register/view", "POST", "/api/user", "POST");
        for (Map.Entry<String, String> route : routes.entrySet()) {
            final var request = request(route.getKey(), route.getValue());
            final var response = mock(HttpServletResponse.class);

            servlet.service(request, response);

            verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
            verify(request, never()).getSession();
            verify(request, never()).getParameter(anyString());
        }
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

    public static class UserWithPassword extends User {

        private final String password;

        public UserWithPassword(final String account, final String password) {
            super(2, account, password, "private@example.com");
            this.password = password;
        }

        public String getPassword() {
            return password;
        }
    }

}
