package com.techcourse;

import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DispatcherServletTest {
    private DispatcherServlet dispatcherServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet();
        // 실제 매핑과 Adapter를 초기화해서 요청 처리 흐름을 함께 검증한다.
        dispatcherServlet.init();

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        when(request.getContextPath()).thenReturn("");
    }

    @Test
    void handlesLegacyController() throws Exception {
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
    void handlesLegacyControllerWithContextPath() throws Exception {
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
    void handlesAnnotationGet() throws Exception {
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
    void handlesAnnotationGetWithContextPath() throws Exception {
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
    void handlesAnnotationPost() throws Exception {
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
