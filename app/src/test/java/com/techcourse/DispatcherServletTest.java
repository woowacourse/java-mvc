package com.techcourse;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.DispatcherServlet;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DispatcherServletTest {
    private jakarta.servlet.Servlet dispatcherServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        var servlet = new DispatcherServlet("com.techcourse.controller");
        // 실제 매핑과 Adapter를 초기화해서 요청 처리 흐름을 함께 검증한다.
        servlet.init();
        dispatcherServlet = servlet;

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        when(request.getContextPath()).thenReturn("");
    }

    @Test
    void forwardsHomeToIndexJsp() throws Exception {
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp"))
                .thenReturn(dispatcher);

        dispatcherServlet.service(request, response);

        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void forwardsHomeToIndexJspWithContextPath() throws Exception {
        when(request.getRequestURI()).thenReturn("/mvc/");
        when(request.getContextPath()).thenReturn("/mvc");
        when(request.getMethod()).thenReturn("GET");

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp"))
                .thenReturn(dispatcher);

        dispatcherServlet.service(request, response);

        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void returnsUserAsJson() throws Exception{
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn("gugu");

        var body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        dispatcherServlet.service(request, response);

        assertThat(body.toString()).isEqualTo("{\"account\":\"gugu\"}");
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void forwardsToRegistrationForm() throws Exception {
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("GET");

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp"))
                .thenReturn(dispatcher);

        dispatcherServlet.service(request, response);

        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void forwardsToRegistrationFormWithContextPath() throws Exception {
        when(request.getRequestURI()).thenReturn("/mvc/register");
        when(request.getContextPath()).thenReturn("/mvc");
        when(request.getMethod()).thenReturn("GET");

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp"))
                .thenReturn(dispatcher);

        dispatcherServlet.service(request, response);

        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void registersUserAndRedirectsToHome() throws Exception {
        // static 저장소의 기존 데이터와 구분할 수 있도록 새 계정을 사용한다.
        String account = "dispatcher-test-" + UUID.randomUUID();
        String password = "test-password";

        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("email")).thenReturn("test@example.com");

        dispatcherServlet.service(request, response);

        assertThat(InMemoryUserRepository.findByAccount(account))
                .hasValueSatisfying(user -> {
                    assertThat(user.getAccount()).isEqualTo(account);
                    assertThat(user.checkPassword(password)).isTrue();
                });
        verify(response).sendRedirect("/index.jsp");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void returnsNotFoundWhenNoHandlerExists() throws Exception {
        when(request.getRequestURI()).thenReturn("/unknown");
        when(request.getMethod()).thenReturn("GET");

        dispatcherServlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verify(request, never()).getRequestDispatcher(anyString());
        verify(response, never()).sendRedirect(anyString());
    }
}
