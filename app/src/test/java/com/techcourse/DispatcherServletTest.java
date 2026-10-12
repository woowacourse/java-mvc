package com.techcourse;

import com.techcourse.repository.InMemoryUserRepository;
import com.techcourse.domain.User;
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

class DispatcherServletTest {

    private DispatcherServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet();
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

        servlet.service(request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(body.toString()).isEqualTo("{\"user\":{\"account\":\"" + account + "\"}}");
    }

    @Test
    void rendersInvalidAccountAsBadRequestJson() throws Exception {
        for (final String account : new String[]{null, "", " "}) {
            final var response = mock(HttpServletResponse.class);
            final var body = new StringWriter();
            when(response.getWriter()).thenReturn(new PrintWriter(body));

            servlet.service(apiRequest(account), response);

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

        servlet.service(apiRequest(UUID.randomUUID().toString()), response);

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
    @DisplayName("기존 컨트롤러가 반환한 JSP 경로로 포워드한다")
    void forwardsLegacyControllerView() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getContextPath()).thenReturn("");
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
        when(request.getMethod()).thenReturn("GET");
        when(request.getContextPath()).thenReturn("");
        when(request.getSession()).thenReturn(mock(HttpSession.class));

        // when
        servlet.service(request, response);

        // then
        verify(response).sendRedirect("/");
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
        servlet.service(request, response);

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
        servlet.service(request, response);

        // then
        assertThat(InMemoryUserRepository.findByAccount(account)).isPresent();
        assertThat(InMemoryUserRepository.findByAccount(account).orElseThrow().checkPassword("secret")).isTrue();
        verify(response).sendRedirect("/index.jsp");
    }
}
