package com.techcourse;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.DispatcherServlet;
import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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

    @Test
    @DisplayName("GET / 요청을 HomeController로 처리하고 홈 화면으로 리다이렉트한다")
    void home() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/");
        when(request.getServletPath()).thenReturn("/");

        final var dispatcherServlet = new DispatcherServlet("com.techcourse");
        dispatcherServlet.init();
        dispatcherServlet.service((ServletRequest) request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    @DisplayName("HTML 이외에 JSON으로 응답할 수 있다")
    void respondWithJson() throws Exception {
        final var account = UUID.randomUUID().toString();
        InMemoryUserRepository.save(new User(3, account, "password", "json@example.com"));
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var body = new StringWriter();
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getServletPath()).thenReturn("/api/user");
        when(request.getParameter("account")).thenReturn(account);
        when(response.getWriter()).thenReturn(new PrintWriter(body));
        final var dispatcherServlet = new DispatcherServlet("com.techcourse");
        dispatcherServlet.init();

        dispatcherServlet.service((ServletRequest) request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(body.toString()).isEqualTo("{\"account\":\"" + account + "\"}");
    }

    @Test
    @DisplayName("로그아웃 후 세션의 사용자를 제거하고 홈 요청을 처리할 수 있다")
    void logoutAndReturnHome() throws Exception {
        final var session = mock(HttpSession.class);
        final var logoutRequest = mock(HttpServletRequest.class);
        final var logoutResponse = mock(HttpServletResponse.class);
        when(logoutRequest.getMethod()).thenReturn("GET");
        when(logoutRequest.getRequestURI()).thenReturn("/logout");
        when(logoutRequest.getServletPath()).thenReturn("/logout");
        when(logoutRequest.getSession()).thenReturn(session);
        final var dispatcherServlet = new DispatcherServlet("com.techcourse");
        dispatcherServlet.init();

        dispatcherServlet.service((ServletRequest) logoutRequest, logoutResponse);

        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(logoutResponse).sendRedirect("/");

        final var homeRequest = mock(HttpServletRequest.class);
        final var homeResponse = mock(HttpServletResponse.class);
        when(homeRequest.getMethod()).thenReturn("GET");
        when(homeRequest.getRequestURI()).thenReturn("/");
        when(homeRequest.getServletPath()).thenReturn("/");

        dispatcherServlet.service((ServletRequest) homeRequest, homeResponse);

        verify(homeResponse).sendRedirect("/index.jsp");
    }
}
